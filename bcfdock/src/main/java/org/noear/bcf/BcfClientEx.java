package org.noear.bcf;

import org.noear.okldap.LdapClient;
import org.noear.okldap.LdapSession;
import org.noear.okldap.entity.LdapPerson;
import org.noear.snack.ONode;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.bcf.models.BcfGroupModelEx;
import org.noear.bcf.models.BcfResourceModelEx;
import org.noear.bcf.models.BcfUserModel;
import org.noear.bcf.models.OBJT;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class BcfClientEx {
    private static DbContext res_db;
    private static ICacheServiceEx res_cache;
    private static LdapClient ldapClient;

    public static void tryInit(ICacheServiceEx bcf_cache, DbContext bcf_db) {
        res_db = bcf_db;
        res_cache = bcf_cache;
    }

    public static void tryInit(ICacheServiceEx bcf_cache, DbContext db, LdapClient ldap){
        res_db = db;
        res_cache = bcf_cache;
        ldapClient = ldap;
    }


    public static DbContext db() {
        return res_db;
    }

    //==================


    private int res_root_id = -1;
    private String res_root_code;
    private BcfGroupModelEx res_root;


    public BcfClientEx system(String bcf_root_code) throws SQLException {
        res_root_code = bcf_root_code;
        res_root_id = -1;
        tryInitResRootId();

        return this;
    }

    public BcfGroupModelEx getRoot() throws SQLException {
        tryInitResRootId();

        return res_root;
    }

    private void tryInitResRootId() throws SQLException {
        if (res_root_id < 0 && BcfUtilEx.isEmpty(res_root_code) == false) {
            res_root = db().table("bcf_group")
                    .where("pg_code=?", res_root_code)
                    .select("*")
                    .caching(res_cache)
                    .getItem(new BcfGroupModelEx());

            res_root_id = res_root.pgid;
        }
    }


    //////////////////////////////////////////////

    /*注册用户*/
    public static BcfUserModel register(String userID, String password, String cn_name, int out_objt, int out_objt_id, String tags) throws SQLException {
        return register(userID, password, cn_name, "", "", out_objt, out_objt_id, tags);
    }

    /*注册用户*/
    public static BcfUserModel register(String userID, String password, String cn_name, String en_name, String pw_mail, int out_objt, int out_objt_id, String tags) throws SQLException {
        String secretPassWd = BcfUtilEx.buildBcfPassWd(userID, password);

        long puid = db().table("bcf_user")
                .set("User_Id", userID)
                .set("Pass_Wd", secretPassWd)
                .set("Is_Disabled", 0)
                .set("cn_name", cn_name)
                .set("en_name", en_name)
                .set("pw_mail", en_name)
                .set("out_objt", out_objt)
                .set("out_objt_id", out_objt_id)
                .set("tags", tags)
                .insert();

        return db().table("bcf_user").where("puid=?", puid).select("*").getItem(new BcfUserModel());
    }

    /*登录*/
    public static BcfUserModel login(String userID, String password) throws Exception {
        if (ldapClient != null) {
            //尝试用ldap登录
            LdapPerson person = null;
            try (LdapSession session = ldapClient.open()) {
                person = session.findPersonOne(userID, password);
            }

            if (person != null) {
                //ldap登录成功后，直接查出用户信息
                return db().table("bcf_user")
                        .where("User_Id=? AND Is_Disabled=0", userID)
                        .log(true)
                        .selectItem("*", BcfUserModel.class);
            } else {
                return new BcfUserModel();
            }
        } else {
            //如果ldap失败，用原生账号登录
            String secretPassWd = BcfUtil.buildBcfPassWd(userID, password);

            return db().table("bcf_user")
                    .where("User_Id=? AND Pass_Wd=? AND Is_Disabled=0", userID, secretPassWd)
                    .log(true)
                    .selectItem("*", BcfUserModel.class);
        }
    }

    public static BcfUserModel login(int puid) throws SQLException {
        return db().table("bcf_user")
                .where("puid=? AND Is_Disabled=0", puid)
                .log(false)
                .selectItem("*", BcfUserModel.class);
    }


    //////////////////////////////////////////////

    /*
     * 设置用户的密码
     * return::0:出错；1：旧密码不对；2：修改成功
     * */
    public static int setUserPassword(String userID, String password, String newPassword) throws SQLException {

        String secretPassWd = BcfUtilEx.buildBcfPassWd(userID, password);

        int puid = db().table("bcf_user")
                .where("user_id=? AND pass_wd=? AND is_disabled=0", userID, secretPassWd)
                .select("puid")
                .getValue(0);

        if (puid > 0) {
            String secretNewPassWd = BcfUtilEx.buildBcfPassWd(userID, newPassword);

            return db().table("bcf_user")
                    .set("pass_Wd", secretNewPassWd)
                    .where("puid=?", puid)
                    .update() > 0 ? 2 : 0;
        } else {
            return 1;
        }
    }


    //////////////////////////////////////////////
    /*获取一个用户，的信息*/
    public static BcfUserModel getUser(int puid) throws SQLException {
        return db().table("bcf_user").where("puid = ?", puid)
                .select("*")
                .caching(res_cache)
                .getItem(new BcfUserModel());
    }


    /*获取有网址的包*/
    public static List<BcfGroupModelEx> getSystems() throws SQLException {
        return db().table("bcf_group").where("Is_Disabled=0 AND Is_Branch=1")
                .orderBy("order_index")
                .select("*")
                .caching(res_cache)
                .getList(new BcfGroupModelEx());
    }


    /*获取根下的所有包*/
    public List<BcfGroupModelEx> getModules() throws SQLException {
        tryInitResRootId();

        if (res_root_id < 0) {
            return new ArrayList<>();
        }

        //3.找出相关组的诚意情
        return db().table("bcf_group").where("Is_Disabled=0 AND R_PGID=4")
                .build((tb) -> {
                    if (res_root_id > 0) {
                        tb.and("P_PGID=?", res_root_id);
                    }
                })
                .orderBy("Order_Index")
                .select("*")
                .caching(res_cache)
                .getList(new BcfGroupModelEx());

    }

    /*包、部门、角色、公司，都属于组*/
    public static BcfGroupModelEx getGroupByCode(String groupCode) throws SQLException {
        return db().table("bcf_group")
                .where("pg_code=?", groupCode)
                .select("*")
                .caching(res_cache)
                .getItem(new BcfGroupModelEx());
    }

    /*找出在某个包下的资源*/
    public static List<BcfResourceModelEx> getResourcesByPack(String packCode) throws SQLException {
        BcfGroupModelEx pack = getGroupByCode(packCode);

        return getResourcesByPack(pack.pgid);
    }

    public static List<BcfResourceModelEx> getResourcesByPack(int packID) throws SQLException {
        if (packID < 1) {
            return new ArrayList<>();
        } else {
            return db().table("bcf_resource r")
                    .innerJoin("bcf_resource_linked rl").on("r.rsid=rl.rsid")
                    .where("rl.LK_OBJT=? AND rl.LK_OBJT_ID=?", OBJT.OBJT_Group, packID)
                    .orderBy("r.Order_Index ASC")
                    .select("r.*")
                    .caching(res_cache)
                    .getList(new BcfResourceModelEx());
        }
    }

    public static BcfResourceModelEx getResourceByPath(String path) throws SQLException {

        if (path == null || path.length() == 1) {
            return new BcfResourceModelEx();
        }

        return db().table("bcf_resource r")
                .where("r.Uri_Path=? AND Is_Disabled=0", path)
                .limit(1)
                .select("r.*")
                .caching(res_cache)
                .getItem(new BcfResourceModelEx());
    }

    public static BcfResourceModelEx getResourceByID(int rsid) throws SQLException {

        if (rsid < 1) {
            return new BcfResourceModelEx();
        }

        return db().table("bcf_resource r")
                .where("r.RSID=? AND Is_Disabled=0", rsid)
                .limit(1)
                .select("r.*")
                .caching(res_cache)
                .getItem(new BcfResourceModelEx());
    }


    public static ONode getUserOpsx(int puid) throws Exception {
        String tmp = db().table("bcf_opsx").where("lk_objt=? AND lk_objt_id=?", OBJT.OBJT_User, puid)
                .select("opsx")
                .caching(res_cache)
                .getValue("");

        if (TextUtils.isEmpty(tmp)) {
            return new ONode();
        } else {
            return ONode.load(tmp);
        }
    }

    //////////////////////////////////////////////
    /*获取一个用户的资源包*/
    public List<BcfGroupModelEx> getUserPacks(int puid) throws SQLException {
        tryInitResRootId();

        if (res_root_id < 0) {
            return new ArrayList<>();
        }

        //1.找出我所有的资源(注意uri_path<>'')
        List<Integer> rids = db().table("bcf_resource r")
                .innerJoin("bcf_resource_linked rl").on("r.RSID=rl.RSID")
                .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.uri_path<>'' AND r.is_disabled=0", puid, OBJT.OBJT_User)
                .select("r.rsid")
                .caching(res_cache)
                .getArray("rsid");

        if (rids.size() == 0) {
            return new ArrayList<>();
        }

        //2.找出资源相关的组id
        List<Integer> pids = db().table("bcf_resource_linked rl")
                .where("rl.LK_OBJT=? AND rl.RSID IN (?...)", OBJT.OBJT_Group, rids)
                .select("DISTINCT rl.lk_objt_id")
                .caching(res_cache)
                .getArray("lk_objt_id");

        if (pids.size() == 0) {
            return new ArrayList<>();
        }

        //3.找出相关组的诚意情
        return db().table("bcf_group")
                .where("PGID IN (?...) AND Is_Disabled=0 AND Is_Visibled=1", pids)
                .build((tb) -> {
                    if (res_root_id > 0) {
                        tb.and("P_PGID=?", res_root_id);
                    }
                })
                .orderBy("order_index")
                .select("*")
                .caching(res_cache)
                .getList(new BcfGroupModelEx());
    }

    /*获取一个用户在某个包下的资源*/
    public List<BcfResourceModelEx> getUserResourcesByPack(int puid, int packID) throws SQLException {
        //1.找出这个包下的资源id
        List<Integer> ids = db().table("bcf_resource_linked rl")
                .where("rl.LK_OBJT=? AND rl.LK_OBJT_ID=?", OBJT.OBJT_Group, packID)
                .select("rl.rsid")
                .caching(res_cache)
                .getArray("rsid");

        if (ids.size() == 0) {
            return new ArrayList<>();
        }


        //2.找出在某个包下的我的资源
        List<BcfResourceModelEx> resList = db().table("bcf_resource r")
                .innerJoin("bcf_resource_linked rl").on("r.RSID=rl.RSID")
                .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.Is_Disabled=0 AND r.RSID IN(?...)", puid, OBJT.OBJT_User, ids)
                .orderBy("r.Order_Index ASC")
                .select("r.*")
                .caching(res_cache)
                .getList(new BcfResourceModelEx());

        for (BcfResourceModelEx res : resList) {
            res.root = res_root;
        }

        return resList;
    }

    /*获取一个用户在某个包下的资源*/
    public List<BcfResourceModelEx> getUserResourcesByPack(int puid, String packCode) throws SQLException {
        BcfGroupModelEx pack = getGroupByCode(packCode);

        return getUserResourcesByPack(puid, pack.pgid);
    }

    /*获取一个用户在某个包下的第一个资源*/
    public BcfResourceModelEx getUserFirstResourceByPack(int puid, int packID) throws SQLException {
        //1.找出这个包下的资源id
        List<Integer> ids = db().table("bcf_resource_linked rl")
                .where("rl.LK_OBJT=? AND rl.LK_OBJT_ID=?", OBJT.OBJT_Group, packID)
                .select("rl.rsid")
                .caching(res_cache)
                .getArray("rsid");

        if (ids.size() == 0) {
            return new BcfResourceModelEx();
        }

        //2.找出在某个包下的我的资源
        BcfResourceModelEx res = db().table("bcf_resource r")
                .innerJoin("bcf_resource_linked rl").on("r.RSID=rl.RSID")
                .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.Is_Disabled=0 AND r.Uri_Path <> '' AND r.RSID IN(?...)", puid, OBJT.OBJT_User, ids)
                .orderBy("r.Order_Index ASC")
                .limit(1)
                .select("r.*")
                .caching(res_cache)
                .getItem(new BcfResourceModelEx());

        res.root = res_root;

        return res;
    }

    /*获取一个用户的第一个资源*/
    public BcfResourceModelEx getUserFirstResource(int puid) throws SQLException {
        if (puid == 0)
            return new BcfResourceModelEx();

        tryInitResRootId();


        if (res_root_id < 0) {
            return new BcfResourceModelEx();
        }

        List<BcfGroupModelEx> glist = getUserPacks(puid);
        if (glist.size() == 0) {
            return new BcfResourceModelEx();
        } else {
            for (BcfGroupModelEx gm : glist) { //仅当前资源组的有效
                if (gm.p_pgid == res_root_id) {
                    return getUserFirstResourceByPack(puid, gm.pgid);
                }
            }

            return new BcfResourceModelEx();
        }

    }


    /////////////////////////////////////


    /*检查url是否在BCF存在*/
    public static boolean hasUrlpath(String path) throws SQLException {

        return db().table("bcf_resource r")
                .where("r.Uri_Path=? AND Is_Disabled=0", path)
                .select("r.rsid")
                .caching(res_cache)
                .getValue(0) > 0;
    }

    /*根据用户检查URL情况*/
    public static boolean hasUrlpathByUser(int puid, String path) throws SQLException {

        if (puid < 1) {
            return false;
        }

        return db().table("bcf_resource r")
                .innerJoin("bcf_resource_linked rl").on("r.RSID=rl.RSID")
                .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.Uri_Path=? AND Is_Disabled=0", puid, OBJT.OBJT_User, path)
                .select("r.rsid")
                .caching(res_cache)
                .getValue(0) > 0;
    }

    /*根据用户检查资源情况*/
    public static boolean hasResourceByUser(int puid, String code) throws SQLException {
        return db().table("bcf_resource r")
                .innerJoin("bcf_resource_linked rl").on("r.RSID=rl.RSID")
                .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.RS_Code=? AND Is_Disabled=0", puid, OBJT.OBJT_User, code)
                .select("r.rsid")
                .caching(res_cache)
                .getValue(0) > 0;
    }
}
