package webapp.dso.db;

import org.noear.weed.DbContext;
import org.noear.weed.IBinder;
import webapp.Config;
import webapp.models.bcf.vo.ExtraInfoVo;

import java.sql.SQLException;
import java.util.List;

public class DbBcfGeneralApi {

    private static DbContext db() {
        return Config.bcf_db;
    }


    /**
     * 获取赋属信息
     * */
    public static List<ExtraInfoVo> getExtraInfoByOpt(Integer lk_objt, Integer rsid) throws SQLException {
        return db().table("bcf_resource_linked l")
                .innerJoin("bcf_user u").on("l.lk_objt_id = u.puid")
                .andEq("l.lk_objt", lk_objt)
                .andEq("l.rsid", rsid)
                .select("l.*,u.cn_name lk_objt_name")
                .getList(ExtraInfoVo.class);
    }

    public static <T extends IBinder> List<T> getModelsByKeyList(T obj, List<Integer> ids, String tableName, String columnName) throws IllegalAccessException, InstantiationException, SQLException {

        if (ids == null || ids.size() == 0) return null;

        StringBuilder sb = new StringBuilder().append("(");
        ids.forEach(id -> sb.append(id + ","));
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");

        return db().table(tableName)
                .where(columnName + " IN " + sb.toString())
                .select("*")
                .getList(obj);
    }


}
