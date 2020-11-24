package webapp.dso.db;

import org.noear.solon.Utils;
import org.noear.weed.DbContext;
import webapp.Config;
import webapp.models.bcf.UserLinkedModel;
import webapp.models.bcf.UserModel;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class DbBcfUserApi {


    private static DbContext db() {
        return Config.bcf_db;
    }

    public static void linkUserByOpt(Integer lk_objt_id, Integer puid, Integer lk_objt) throws SQLException {

        DbBcfUserApi.db().table("bcf_user_linked")
                .set("puid", puid)
                .set("lk_objt", lk_objt)
                .set("lk_objt_id", lk_objt_id)
                .set("lk_operate", "+")
                .insert();
    }

    public static List<UserLinkedModel> getUserLinkedModelByOpt(Integer lk_objt, Integer lk_objt_id) throws SQLException {

        return DbBcfUserApi.db().table("bcf_user_linked AS bul").innerJoin("`bcf_user` AS u").on("bul.puid=u.puid")
                .where("lk_objt=?", lk_objt)
                .and("lk_objt_id=?", lk_objt_id)
                .orderBy("cn_name ASC")
                .select("*")
                .getList(new UserLinkedModel());

    }

    public static void deleteUserById(Integer puid) throws SQLException {

        // / 1. delete the user  2. delete the link
        DbBcfUserApi.db().table("bcf_user").where("puid=?", puid)
                .limit(1).delete();

        DbBcfUserApi.db().table("bcf_user_linked").where("puid=?", puid)
                .delete();
    }

    public static long addBcfUser(UserLinkedModel userLinkedModel) throws SQLException {

        long id = DbBcfUserApi.db().table("bcf_user")
                .set("user_id", userLinkedModel.user_id)
                .set("cn_name", userLinkedModel.cn_name)
                .set("en_name", userLinkedModel.en_name)
                .set("out_objt", userLinkedModel.out_objt)
                .set("out_objt_id", userLinkedModel.out_objt_id)
                .set("pw_mail", userLinkedModel.pw_mail)
                .set("pass_wd", userLinkedModel.pass_wd)
                .set("note", userLinkedModel.note)
                .set("tags", userLinkedModel.tags)
                .set("is_disabled", userLinkedModel.is_disabled)
                .set("is_visibled", userLinkedModel.is_visibled)
                .set("state", userLinkedModel.state)
                .set("create_time", new Date())
                .set("last_update", new Date())
                .insert();
        return id;
    }

    public static boolean updateBcfUser(UserLinkedModel userLinkedModel) throws SQLException {

        boolean flag = DbBcfUserApi.db().table("bcf_user")
                .set("cn_name", userLinkedModel.cn_name)
                .set("en_name", userLinkedModel.en_name)
                .set("out_objt", userLinkedModel.out_objt)
                .set("out_objt_id", userLinkedModel.out_objt_id)
                .set("pw_mail", userLinkedModel.pw_mail)
                .set("note", userLinkedModel.note)
                .set("tags", userLinkedModel.tags)
                .set("is_disabled", userLinkedModel.is_disabled)
                .set("is_visibled", userLinkedModel.is_visibled)
                .set("state", userLinkedModel.state)
                .set("last_update", new Date())
                .build((tb) -> {
                    if (Utils.isNotEmpty(userLinkedModel.pass_wd)) {
                        tb.set("pass_wd", userLinkedModel.pass_wd);
                    }
                })
                .where("puid = ?", userLinkedModel.puid)
                .update() > 0;
        return flag;
    }

    public static List<UserLinkedModel> getUserLinkedListByPuid(Integer puid, Integer lk_objt) throws SQLException {

        return DbBcfUserApi.db().table("bcf_user_linked")
                .where("puid=?", puid)
                .and("lk_objt=?", lk_objt)
                .select("*")
                .getList(new UserLinkedModel());
    }

    public static List<UserLinkedModel> getAllUserConn() throws SQLException {
        return DbBcfUserApi.db().table("bcf_user_linked")
                .where("lk_objt=?", 7)
                .select("*")
                .getList(new UserLinkedModel());
    }

    public static UserModel getUserModelById(int puid) throws SQLException {
        return DbBcfUserApi.db().table("bcf_user")
                .where("puid=?", puid)
                .limit(1)
                .select("*")
                .getItem(new UserModel());
    }
}
