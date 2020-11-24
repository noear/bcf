package webapp.dso.db;

import org.noear.weed.DbContext;
import webapp.Config;
import webapp.models.bcf.OpsxModel;

import java.sql.SQLException;

public class DbBcfOpsxApi {

    private static DbContext db() {
        return Config.bcf_db;
    }

    public static OpsxModel getBcfOpsxViaCriteria(int lk_objt_id, int lk_obj) throws SQLException {
        return db().table("`bcf_opsx`")
                .where("`lk_objt` = ?", lk_obj)
                .and("`lk_objt_id` = ?", lk_objt_id)
                .limit(1)
                .select("*")
                .getItem(new OpsxModel());
    }
    public static long getBcfOpsxViaCriteriaCount(int lk_objt_id, int lk_obj) throws SQLException {
        return db().table("`bcf_opsx`")
                .where("`lk_objt` = ?", lk_obj)
                .and("`lk_objt_id` = ?", lk_objt_id)
                .count();
    }
    public static Boolean setBcfOpsxViaCriteria(OpsxModel bcfOpsxModel) throws SQLException {
        return db().table("`bcf_opsx`")
                .set("lk_objt", bcfOpsxModel.lk_objt)
                .set("lk_objt_id", bcfOpsxModel.lk_objt_id)
                .set("opsx", bcfOpsxModel.opsx)
                .set("tags", bcfOpsxModel.tags)
                .insert() > 0;
    }
    public static void updateBcfOpsxViaCriteria(OpsxModel bcfOpsxModel) throws SQLException {
                db().table("`bcf_opsx`")
                .where("`lk_objt` = ?", bcfOpsxModel.lk_objt)
                .and("`lk_objt_id` = ?", bcfOpsxModel.lk_objt_id)
                .set("opsx", bcfOpsxModel.opsx)
                .update();
    }
}
