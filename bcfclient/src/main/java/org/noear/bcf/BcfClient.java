package org.noear.bcf;

import org.noear.bcf.models.*;
import org.noear.okldap.LdapClient;
import org.noear.okldap.LdapSession;
import org.noear.okldap.entity.LdapPerson;
import org.noear.snack.ONode;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;


import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class BcfClient {
    private static int    res_root_id   = -1;
    private static String res_root_code;

    private static DbContext res_db;
    private static ICacheServiceEx res_cache;
    private static LdapClient ldapClient;

    public static void tryInit(String bcf_root_code, ICacheServiceEx bcf_cache, DbContext db){
        res_root_code = bcf_root_code;
        res_db = db;
        res_cache = bcf_cache;
    }

    public static void tryInit(String bcf_root_code, ICacheServiceEx bcf_cache, DbContext db, LdapClient ldap){
        res_root_code = bcf_root_code;
        res_db = db;
        res_cache = bcf_cache;
        ldapClient = ldap;
    }

    private static void tryInitResRootId() throws SQLException {
        if (res_root_id < 0 && BcfUtil.isEmpty(res_root_code) == false) {
            res_root_id = db().table("bcf_group")
                    .where("pg_code=?", res_root_code)
                    .select("pgid")
                    .getValue(0);
        }
    }

    public static DbContext db(){
        return res_db;
    }

    //////////////////////////////////////////////

    /*注册用户*/
    public static BcfUserModel register(String userID, String password, String cn_name, int out_objt, int out_objt_id, String tags) throws SQLException {
        return register(userID,password,cn_name,"","",out_objt,out_objt_id,tags);
    }

    /*注册用户*/
    public static BcfUserModel register(String userID,String password, String cn_name,String en_name, String pw_mail, int out_objt, int out_objt_id, String tags) throws SQLException {
        String secretPassWd = BcfUtil.buildBcfPassWd(userID, password);

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

        return db().table("bcf_user").where("puid=?",puid).select("*").getItem(new BcfUserModel());
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
                return login(userID);
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

    public static BcfUserModel login(String userID) throws SQLException {

        return db().table("bcf_user")
                .where("User_Id=? AND Is_Disabled=0", userID)
                .log(true)
                .selectItem("*", BcfUserModel.class);
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

        String secretPassWd = BcfUtil.buildBcfPassWd(userID, password);

        int puid = db().table("bcf_user")
                .where("user_id=? AND pass_wd=? AND is_disabled=0", userID, secretPassWd)
                .select("puid")
                .getValue(0);

        if (puid > 0) {
            String secretNewPassWd = BcfUtil.buildBcfPassWd(userID, newPassword);

            return db().table("bcf_user")
                    .set("pass_Wd", secretNewPassWd)
                    .where("puid=?", puid)
                    .update() > 0 ? 2 : 0;
        } else {
            return 1;
        }
    }

    /*设置用户的通用状态*/
    public static int setUserState(int puid, int state) throws SQLException{
        return
                db().table("bcf_user").set("state",state)
                        .where("puid=?",puid).update();
    }
    /*设置用户的禁用状态*/
    public static int setUserDisabled(int puid, boolean isDisabled) throws SQLException {
        return db().table("bcf_user")
                .set("is_disabled", (isDisabled ? 1 : 0))
                .where("puid=?", puid).update();
    }
    /*设置用户的可见状态*/
    public static int setUserVisibled(int puid, boolean isVisibled) throws SQLException {
        return db().table("bcf_user")
                .set("is_visibled", (isVisibled ? 1 : 0))
                .where("puid=?", puid).update();
    }

//    public static void setUserOPSx(int puid,  ONode opsx) throws SQLException {
//        setUserOPSx(puid, "S", opsx);
//    }
//
//    public static void setUserOPSx(int puid, String tags, ONode opsx) throws SQLException {
//        db().table("bcf_opsx")
//                .set("opsx", opsx.toJson())
//                .set("lk_objt", OBJT.OBJT_User)
//                .set("lk_objt_id", puid)
//                .set("tags", tags)
//                .updateExt("lk_objt,lk_objt_id,tags");
//    }


    public static ONode getUserOPSx(int puid) throws SQLException {
        return getUserOPSx(puid, "S");
    }

    public static ONode getUserOPSx(int puid, String tags) throws SQLException {
        String temp = db().table("bcf_opsx")
                .where("lk_objt=? AND lk_objt_id=? AND tags=?", OBJT.OBJT_User, puid, tags)
                .caching(res_cache)
                .select("opsx")
                .getValue("");

        return ONode.load(temp);
    }

    //////////////////////////////////////////////

    /*是否存在一个用户*/
    public static boolean isUserExist(String userID) throws SQLException {
        return db().table("bcf_user")
                .where("User_Id=?", userID)
                .exists();
    }

    /*是否在一个组里*/
    public static boolean isUserInGroup(int puid, String pgCode) throws SQLException{
        if(pgCode == null || pgCode.length()==0){
            return false;
        }

        int pgid = getGroupByCode(pgCode).pgid;

        return isUserInGroup(puid,pgid);
    }

    /*是否在一个组里*/
    public static boolean isUserInGroup(int puid, int pgid) throws SQLException {
        if(pgid<=0) {
            return false;
        }

        return db().table("bcf_user_linked")
                .where("puid=? AND lk_objt=? AND lk_objt_id=?", puid, OBJT.OBJT_Group, pgid)
                .caching(res_cache)
                .exists();
    }

    //////////////////////////////////////////////
    /*获取一个用户，的信息*/
    public static BcfUserModel getUser(int puid) throws SQLException{
        return db().table("bcf_user").where("puid = ?", puid)
                .select("*")
                .caching(res_cache)
                .getItem(new BcfUserModel());
    }

    /*获取一批用户，根据PUIDs*/
    public static List<BcfUserModel> getUsers(List<Integer> puids) throws SQLException {
        return db().table("bcf_user").where("puid in (?...)", puids)
                .select("*")
                .caching(res_cache)
                .getList(new BcfUserModel());
    }

    /*获取一批用户，根据PUIDs，并根据name筛选*/
    public static List<BcfUserModel> getUsersByName(List<Integer> puids, String name) throws SQLException{
        return db().table("bcf_user")
                .where("puid in (?...)", puids)
                .and("cn_name=?",name)
                .select("*")
                .caching(res_cache)
                .getList(new BcfUserModel());
    }

    /*获取一批用户，根据一个组*/
    public static List<BcfUserModel> getUsersByGroup(String pgCode) throws SQLException {
        if(pgCode == null || pgCode.length()==0){
            return new ArrayList<>();
        }

        int pgid = getGroupByCode(pgCode).pgid;

        return getUsersByGroup(pgid);
    }

    /*获取一批用户，根据一个组，根据可见性过滤*/
    public static List<BcfUserModel> getUsersByGroup(String pgCode, boolean isVisibled) throws SQLException {
        if(pgCode == null || pgCode.length()==0){
            return new ArrayList<>();
        }

        int pgid = getGroupByCode(pgCode).pgid;

        return db().table("bcf_user u")
                .innerJoin("bcf_user_linked ul")
                .on("u.puid = ul.puid").and("ul.lk_objt=? AND ul.lk_objt_id=?", OBJT.OBJT_Group, pgid)
                .where("is_visibled=?", (isVisibled ? 1 : 0))
                .select("u.*")
                .caching(res_cache)
                .getList(new BcfUserModel());
    }
    /*获取一批用户，根据一个组，根据状态过滤*/
    public static List<BcfUserModel> getUsersByGroup(String pgCode, int state) throws SQLException {
        if(pgCode == null || pgCode.length()==0){
            return new ArrayList<>();
        }

        int pgid = getGroupByCode(pgCode).pgid;

        return db().table("bcf_user u")
                .innerJoin("bcf_user_linked ul")
                .on("u.puid = ul.puid").and("ul.lk_objt=? AND ul.lk_objt_id=?", OBJT.OBJT_Group, pgid)
                .where("state=?", state)
                .select("u.*")
                .caching(res_cache)
                .getList(new BcfUserModel());
    }

    /*获取一批用户，根据一个组*/
    public static List<BcfUserModel> getUsersByGroup(int pgid) throws SQLException {
        return db().table("bcf_user u")
                .innerJoin("bcf_user_linked ul")
                .on("u.puid = ul.puid").and("ul.lk_objt=? AND ul.lk_objt_id=?",OBJT.OBJT_Group, pgid)
                .select("u.*")
                .caching(res_cache)
                .getList(new BcfUserModel());
    }

    /*获取有网址的包*/
    public static List<BcfGroupModel> getSysPacks() throws SQLException{
        return db().table("bcf_group").where("Is_Disabled=0 AND R_PGID=4")
                .and("uri_path LIKE '%://%'")
                .select("*")
                .caching(res_cache)
                .getList(new BcfGroupModel());
    }

    /*获取根下的所有包*/
    public static List<BcfGroupModel> getAllPacks() throws SQLException {
        tryInitResRootId();

        //3.找出相关组的诚意情
        return db().table("bcf_group").where("Is_Disabled=0 AND R_PGID=4")
                .expre((tb) -> {
                    if (res_root_id > 0) {
                        tb.and("P_PGID=?", res_root_id);
                    }
                })
                .select("*")
                .caching(res_cache)
                .getList(new BcfGroupModel());

    }

    /*包、部门、角色、公司，都属于组*/
    public static BcfGroupModel getGroupByCode(String groupCode) throws SQLException{
        return db().table("bcf_group")
                .where("pg_code=?", groupCode)
                .select("*")
                .caching(res_cache)
                .getItem(new BcfGroupModel());
    }

    /*找出在某个包下的资源*/
    public static List<BcfResourceModel> getResourcesByPack(String packCode) throws SQLException{
        BcfGroupModel pack = getGroupByCode(packCode);

        return getResourcesByPack(pack.pgid);
    }

    public static List<BcfResourceModel> getResourcesByPack(int packID) throws SQLException {
        if (packID < 1) {
            return new ArrayList<>();
        } else {
            return db().table("bcf_resource r")
                    .innerJoin("bcf_resource_linked rl").on("r.RSID=rl.RSID")
                    .where("rl.LK_OBJT=? AND rl.LK_OBJT_ID=?", OBJT.OBJT_Group, packID)
                    .orderBy("r.Order_Index ASC")
                    .select("r.*")
                    .caching(res_cache)
                    .getList(new BcfResourceModel());
        }
    }

    public static BcfResourceModel getResourceByPath(String path) throws SQLException {

        if (path == null || path.length()==1) {
            return new BcfResourceModel();
        }

        return db().table("bcf_resource r")
                .where("r.Uri_Path=? AND Is_Disabled=0", path)
                .limit(1)
                .select("r.*")
                .caching(res_cache)
                .getItem(new BcfResourceModel());
    }

    //////////////////////////////////////////////

    /*获取一个用户的关系组*/
    public static List<BcfGroupModel> getUserGroups(int puid) throws SQLException{
        //1.找出我关系的组
        return db().table("bcf_group g")
                .innerJoin("bcf_user_linked ul").on("g.pgid=ul.lk_objt_id")
                .and("ul.puid=? AND ul.lk_objt=?", puid, OBJT.OBJT_Group)
                .select("g.*")
                .caching(res_cache)
                .getList(new BcfGroupModel());
    }

    /*获取一个用户的关系人员*/
    public static List<BcfUserModel> getUserPersons(int puid) throws SQLException {
        //1.找出我关系的人
        return db().table("bcf_user u")
                .innerJoin("bcf_user_linked ul").on("u.puid=ul.puid")
                .where("ul.lk_objt_id=? AND ul.lk_objt=?", puid, OBJT.OBJT_User)
                .select("u.*")
                .caching(res_cache)
                .getList(new BcfUserModel());
    }

    //////////////////////////////////////////////
    /*获取一个用户的资源包*/
    public static List<BcfGroupModel> getUserPacks(int puid) throws SQLException {
        tryInitResRootId();

        //1.找出我所有的资源
        List<Integer> rids = db().table("bcf_resource r")
                .innerJoin("bcf_resource_linked rl").on("r.RSID=rl.RSID")
                .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.uri_path<>'' AND r.is_disabled=0", puid, OBJT.OBJT_User)
                .select("r.rsid")
                .caching(res_cache)
                .getArray("rsid");

        if(rids.size()==0){
            return new ArrayList<>();
        }

        //2.找出资源相关的组id
        List<Integer> pids = db().table("bcf_resource_linked rl")
                .where("rl.LK_OBJT=? AND rl.RSID IN (?...)", OBJT.OBJT_Group, rids)
                .select("DISTINCT rl.lk_objt_id")
                .caching(res_cache)
                .getArray("lk_objt_id");

        if(pids.size()==0){
            return new ArrayList<>();
        }

        //3.找出相关组的诚意情
        return db().table("bcf_group")
                .where("pgid IN (?...) AND Is_Disabled=0 AND Is_Visibled=1", pids)
                .expre((tb) -> {
                    if (res_root_id > 0) {
                        tb.and("P_PGID=?", res_root_id);
                    }
                })
                .orderBy("Order_Index")
                .select("*")
                .caching(res_cache)
                .getList(new BcfGroupModel());
    }
    /*获取一个用户在某个包下的资源*/
    public static List<BcfResourceModel> getUserResourcesByPack(int puid, int packID) throws SQLException {
        //1.找出这个包下的资源id
        List<Integer> ids = db().table("bcf_resource_linked rl")
                .where("rl.LK_OBJT=? AND rl.LK_OBJT_ID=?", OBJT.OBJT_Group, packID)
                .select("rl.rsid")
                .caching(res_cache)
                .getArray("rsid");

        if(ids.size()==0){
            return new ArrayList<>();
        }


        //2.找出在某个包下的我的资源
        return db().table("bcf_resource r")
                .innerJoin("bcf_resource_linked rl").on("r.rsid=rl.rsid")
                .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.Is_Disabled=0 AND r.RSID IN(?...)", puid, OBJT.OBJT_User, ids)
                .orderBy("r.Order_Index ASC")
                .select("r.*")
                .caching(res_cache)
                .getList(new BcfResourceModel());
    }
    /*获取一个用户在某个包下的资源*/
    public static List<BcfResourceModel> getUserResourcesByPack(int puid, String packCode) throws SQLException {
        BcfGroupModel pack = getGroupByCode(packCode);

        return getUserResourcesByPack(puid, pack.pgid);
    }
    /*获取一个用户在某个包下的第一个资源*/
    public static BcfResourceModel getUserFirstResourceByPack(int puid, int packID) throws SQLException {
        //1.找出这个包下的资源id
        List<Integer> ids = db().table("bcf_resource_linked rl")
                .where("rl.LK_OBJT=? AND rl.LK_OBJT_ID=?", OBJT.OBJT_Group, packID)
                .select("rl.rsid")
                .caching(res_cache)
                .getArray("rsid");

        if(ids.size()==0){
            return new BcfResourceModel();
        }

        //2.找出在某个包下的我的资源
        return db().table("bcf_resource r")
                .innerJoin("bcf_resource_linked rl").on("r.rsid=rl.rsid")
                .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.uri_path <> '' AND r.Is_Disabled=0 AND r.RSID IN(?...)", puid, OBJT.OBJT_User, ids)
                .orderBy("r.Order_Index ASC")
                .limit(1)
                .select("r.*")
                .caching(res_cache)
                .getItem(new BcfResourceModel());
    }
    /*获取一个用户的第一个资源*/
    public static BcfResourceModel getUserFirstResource(int puid) throws SQLException{
        if (puid == 0)
            return new BcfResourceModel();

        tryInitResRootId();

        if(res_root_id>0){
            List<BcfGroupModel> glist =  getUserPacks(puid);
            if(glist.size()==0) {
                return new BcfResourceModel();
            }else {
                for(BcfGroupModel gm : glist){ //仅当前资源组的有效
                    if(gm.p_pgid == res_root_id){
                        return getUserFirstResourceByPack(puid, gm.pgid);
                    }
                }

                return new BcfResourceModel();
            }
        }else {
            return db().table("bcf_resource r")
                    .innerJoin("bcf_resource_linked rl").on("r.RSID=rl.RSID")
                    .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.Is_Disabled=0", puid, OBJT.OBJT_User)
                    .orderBy("r.Order_Index ASC")
                    .limit(1)
                    .select("r.*")
                    .caching(res_cache)
                    .getItem(new BcfResourceModel());
        }
    }


    /////////////////////////////////////
    /*获取一个角色的资源包*/
    public static List<BcfGroupModel> getRolePacks(int pgid) throws SQLException {
        tryInitResRootId();

        //1.找出我所有的资源
        List<Integer> rids = db().table("bcf_resource r")
                .innerJoin("bcf_resource_linked rl").on("r.RSID=rl.RSID")
                .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=?", pgid, OBJT.OBJT_Group)
                .select("r.rsid")
                .caching(res_cache)
                .getArray("rsid");

        if(rids.size()==0){
            return new ArrayList<>();
        }


        //2.找出资源相关的组id
        List<Integer> pids = db().table("bcf_resource_linked rl")
                .where("rl.LK_OBJT=? AND rl.RSID IN (?...)", OBJT.OBJT_Group, rids)
                .select("DISTINCT rl.lk_objt_id")
                .caching(res_cache)
                .getArray("lk_objt_id");

        if(pids.size()==0){
            return new ArrayList<>();
        }

        //3.找出相关组的诚意情
        return db().table("bcf_group")
                .where("pgid IN (?...) AND Is_Disabled=0 AND Is_Visibled=1", pids)
                .build((tb) -> {
                    if (res_root_id > 0) {
                        tb.and("P_PGID=?", res_root_id);
                    }
                })
                .orderBy("Order_Index")
                .select("*")
                .caching(res_cache)
                .getList(new BcfGroupModel());
    }
    /*获取一个角色在某个包下的资源*/
    public static List<BcfResourceModel> getRoleResourcesByPack(int pgid, int packID) throws SQLException {
        //1.找出这个包下的资源id
        List<Integer> ids = db().table("bcf_resource_linked rl")
                .where("rl.LK_OBJT=? AND rl.LK_OBJT_ID=?", OBJT.OBJT_Group, packID)
                .select("rl.rsid")
                .caching(res_cache)
                .getArray("rsid");

        if(ids.size()==0){
            return new ArrayList<>();
        }


        //2.找出在某个包下的我的资源
        return db().table("bcf_resource r")
                .innerJoin("bcf_resource_linked rl").on("r.rsid=rl.rsid")
                .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.Is_Disabled=0 AND r.RSID IN(?...)", pgid, OBJT.OBJT_Group, ids)
                .orderBy("r.Order_Index ASC")
                .select("r.*")
                .caching(res_cache)
                .getList(new BcfResourceModel());
    }
    /*获取一个角色在某个包下的资源*/
    public static List<BcfResourceModel> getRoleResourcesByPack(int pgid, String packCode) throws SQLException {
        BcfGroupModel pack = getGroupByCode(packCode);

        return getRoleResourcesByPack(pgid, pack.pgid);
    }
    /*获取一个角色在某个包下的第一个资源*/
    public static BcfResourceModel getRoleFirstResourceByPack(int pgid, int packID) throws SQLException {
        //1.找出这个包下的资源id
        List<Integer> ids = db().table("bcf_resource_linked rl")
                .where("rl.LK_OBJT=? AND rl.LK_OBJT_ID=?", OBJT.OBJT_Group, packID)
                .select("rl.rsid")
                .caching(res_cache)
                .getArray("rsid");

        if(ids.size()==0){
            return new BcfResourceModel();
        }


        //2.找出在某个包下的我的资源
        return db().table("bcf_resource r")
                .innerJoin("bcf_resource_linked rl").on("r.RSID=rl.RSID")
                .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.Is_Disabled=0 AND r.RSID IN(?...)", pgid, OBJT.OBJT_Group, ids)
                .orderBy("r.Order_Index ASC")
                .limit(1)
                .select("r.*")
                .caching(res_cache)
                .getItem(new BcfResourceModel());
    }
    /*获取一个角色的第一个资源*/
    public static BcfResourceModel getRoleFirstResource(int pgid) throws SQLException{
        if (pgid == 0)
            return new BcfResourceModel();

        tryInitResRootId();

        if(res_root_id>0){
            List<BcfGroupModel> glist =  getRolePacks(pgid);
            if(glist.size()==0) {
                return new BcfResourceModel();
            }else {
                for(BcfGroupModel gm : glist){ //仅当前资源组的有效
                    if(gm.p_pgid == res_root_id){
                        return getRoleFirstResourceByPack(pgid, gm.pgid);
                    }
                }

                return new BcfResourceModel();
            }
        }else {
            return db().table("bcf_resource r")
                    .innerJoin("bcf_resource_linked rl").on("r.RSID=rl.RSID")
                    .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.Is_Disabled=0", pgid, OBJT.OBJT_Group)
                    .orderBy("r.Order_Index ASC")
                    .limit(1)
                    .select("r.*")
                    .caching(res_cache)
                    .getItem(new BcfResourceModel());
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

        if(puid<1){
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

    /////////////////////////////////

    /*根据角色检查URL情况*/
    public static boolean hasUrlpathByRole(int pgid, String path) throws SQLException {

        if(pgid<1){
            return false;
        }

        return db().table("bcf_resource r")
                .innerJoin("bcf_resource_linked rl").on("r.RSID=rl.RSID")
                .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.Uri_Path=? AND Is_Disabled=0", pgid, OBJT.OBJT_Group, path)
                .select("r.rsid")
                .caching(res_cache)
                .getValue(0) > 0;
    }

    /*根据角色检查资源情况*/
    public static boolean hasResourceByRole(int pgid, String code) throws SQLException {
        return db().table("bcf_resource r")
                .innerJoin("bcf_resource_linked rl").on("r.rsid=rl.rsid")
                .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=? AND r.RS_Code=? AND Is_Disabled=0", pgid, OBJT.OBJT_Group, code)
                .select("r.rsid")
                .caching(res_cache)
                .getValue(0) > 0;
    }

}
