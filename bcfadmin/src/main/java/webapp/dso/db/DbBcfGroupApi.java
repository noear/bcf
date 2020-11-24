package webapp.dso.db;

import org.noear.weed.DbContext;
import webapp.Config;
import webapp.models.bcf.GroupModel;
import webapp.models.bcf.ResourceLinkedModel;
import webapp.models.bcf.UserLinkedModel;
import webapp.models.bcf.vo.GroupVo;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DbBcfGroupApi {

    private static DbContext db() {
        return Config.bcf_db;
    }

    public static List<GroupModel> getBcfGroupListByPid(Integer pgroupId) throws SQLException {

        return DbBcfGroupApi.db().table("bcf_group")
                .where("p_pgid=?", pgroupId)
                .orderBy("order_index ASC")
                .select("*")
                //.caching(DbBcfGroupApi.res_cache).usingCache(60 * 10).cacheTag("bcf_group_id_" + pgroupId).cacheTag("bcf_group_all")

                .getList(new GroupModel());

    }

    public static List<GroupModel> getGroupListByUserId(Integer puid) throws SQLException, InstantiationException, IllegalAccessException {

        List<UserLinkedModel> userLinkedList = DbBcfUserApi.getUserLinkedListByPuid(puid, Config.lk_objt_group);
        List<Integer> uGroupIds = userLinkedList.stream().map(m -> m.lk_objt_id).collect(Collectors.toList());
        return DbBcfGeneralApi.getModelsByKeyList(new GroupModel(), uGroupIds, "bcf_group", "pgid");
    }

    public static GroupModel getBcfGroupById(int pgid) throws SQLException {

        return DbBcfGroupApi.db().table("bcf_group")
                .where("pgid=?", pgid)
                .limit(1)
                .select("*")
                .getItem(new GroupModel());
    }

    public static GroupModel getGroupByResId(int lk_objt_group, int rsid) throws SQLException {

        ResourceLinkedModel linkedModel = DbBcfResourceApi.getResourceLinkedModelByOpt(lk_objt_group, rsid, "rsid");

        return getBcfGroupById(linkedModel.lk_objt_id);
    }

    public static List<GroupVo> getGroupListByResIds(int lk_objt, List<Integer> rsids) throws SQLException {

        List<ResourceLinkedModel> resourceLinkedModels = DbBcfGroupApi.db().table("bcf_resource_linked")
                .where("rsid IN (?...)", rsids)
                .and("lk_objt=?", lk_objt)
                .select("*")
                .getList(new ResourceLinkedModel());

        List group_ids = resourceLinkedModels.stream().map(ResourceLinkedModel::getLk_objt_id).collect(Collectors.toList());

        return DbBcfGroupApi.db().table("bcf_group g")
                .innerJoin("bcf_resource_linked l")
                .on("g.pgid=l.lk_objt_id")
                .where("g.pgid in (?...)", group_ids)
                .and("l.lk_objt=?", lk_objt)
                .select("*")
                .getList(new GroupVo());
    }

    public static void insertBcfGroup(GroupModel groupModel) throws SQLException {

        DbBcfGroupApi.db().table("bcf_group")
                .set("pgid", groupModel.pgid)
                .set("p_pgid", groupModel.p_pgid)
                .set("r_pgid", groupModel.r_pgid)
                .set("pg_code", groupModel.pg_code)
                .set("cn_name", groupModel.cn_name)
                .set("en_name", groupModel.en_name)
                .set("uri_path", groupModel.uri_path)
                .set("in_level", groupModel.in_level)
                .set("is_branch", groupModel.is_branch)
                .set("tags", groupModel.tags)
                .set("order_index", groupModel.order_index)
                .set("is_disabled", groupModel.is_disabled)
                .set("is_visibled", groupModel.is_visibled)
                .set("create_time", new Date())
                .set("last_update", new Date())
                .insert();
    }

    public static void updateBcfGroup(GroupModel groupModel) throws SQLException {

        DbBcfGroupApi.db().table("bcf_group")
                .where("pgid = ?", groupModel.pgid)
                .set("p_pgid", groupModel.p_pgid)
                .set("r_pgid", groupModel.r_pgid)
                .set("pg_code", groupModel.pg_code)
                .set("cn_name", groupModel.cn_name)
                .set("en_name", groupModel.en_name)
                .set("uri_path", groupModel.uri_path)
                .set("in_level", groupModel.in_level)
                .set("is_branch", groupModel.is_branch)
                .set("tags", groupModel.tags)
                .set("order_index", groupModel.order_index)
                .set("is_disabled", groupModel.is_disabled)
                .set("is_visibled", groupModel.is_visibled)
                .set("last_update", new Date())
                .update();
    }
}
