package webapp.dso.db2;

import org.noear.weed.DbContext;
import webapp.Config;
import webapp.models.bcf.GroupModel;

import java.sql.SQLException;
import java.util.List;

public class DbApiBase {
    public static DbContext db(){
        return Config.bcf_db;
    }

    /**
     * 获取组列表（树级获取），根据 @p_pgid
     * */
    public static List<GroupModel> getGroupListBy(int p_pgid) throws SQLException {
        return
        db().table("bcf_group")
                .whereEq("p_pgid",p_pgid)
                .orderByAsc("order_index")
                .select("*")
                .getList(new GroupModel());
    }

    /**
     * 获到组详情
     * */
    public GroupModel getGroup(int pgid) throws SQLException{
        return
        db().table("bcf_group")
                .whereEq("pgid",pgid)
                .select("*")
                .getItem(new GroupModel());
    }
}
