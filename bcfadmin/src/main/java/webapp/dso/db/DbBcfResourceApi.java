package webapp.dso.db;

import org.noear.weed.DbContext;
import webapp.Config;
import webapp.models.bcf.GroupModel;
import webapp.models.bcf.ResourceLinkedModel;
import webapp.models.bcf.ResourceModel;
import webapp.models.bcf.vo.ResourceExVo;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class DbBcfResourceApi {


    private static DbContext db() {
        return Config.bcf_db;
    }

    public static void linkResourceByOpt(Integer lk_objt_id, Integer rsid, Integer lk_objt) throws SQLException {

        DbBcfResourceApi.db().table("bcf_resource_linked")
                .set("rsid", rsid)
                .set("lk_objt", lk_objt)
                .set("lk_objt_id", lk_objt_id)
                .set("lk_operate", "+")
                .set("p_express", 2)
                .insert();
    }


    public static ResourceLinkedModel getResourceLinkedModelByOpt(Integer lk_objt, Integer columnId, String columnName) throws SQLException {

        return DbBcfResourceApi.db().table("bcf_resource_linked")
                .where("lk_objt=?", lk_objt)
                .and(columnName + "=?", columnId)// columnName+"=?"
                .select("*")
                .getItem(new ResourceLinkedModel());
    }

    public static List<ResourceModel> getResourceListByOpt(Integer lk_objt, Integer lk_objt_id) throws Exception {
        //获取对象，直接关联的资源
        return
        db().table("bcf_resource_linked l")
                .innerJoin("bcf_resource r").on("r.rsid = l.rsid")
                .where("l.lk_objt=?", lk_objt)
                .and("l.lk_objt_id=?", lk_objt_id)
                .select("r.*")
                .getList(new ResourceModel());

    }

    // inner usage
    public static List<ResourceLinkedModel> getResourceLinkedModelsByOpt(Integer lk_objt, Integer columnId, String columnName) throws SQLException {

        return db().table("bcf_resource_linked")
                .where("lk_objt=?", lk_objt)
                .and(columnName + "=?", columnId)// columnName+"=?"
                .select("*")
                .getList(new ResourceLinkedModel());

    }

    public static List<ResourceExVo> getResourceLinkedModelsByOpt2(Integer pgid) throws SQLException {

        List<ResourceExVo> list = DbBcfResourceApi.db().table("bcf_resource_linked AS a")
                .innerJoin("bcf_group AS b")
                .on("a.lk_objt_id=b.pgid")
                .leftJoin("bcf_resource AS c")
                .on("a.rsid = c.rsid")
                .where("a.lk_objt=?", 2)
                .and("b.r_pgid=?", 1)
                .and("b.pgid=?", pgid)
                .and("b.is_disabled=?", 0)
                .and("c.is_disabled=?", 0)
                .select("a.rsid,a.lk_objt,c.cn_name,a.p_express")
                .getList(new ResourceExVo());
        return list;

    }
    public static List<ResourceExVo> getResourceLinkedModelsByOpt4(Integer puid) throws SQLException {


        List<ResourceExVo> list = DbBcfResourceApi.db().table("bcf_resource_linked AS a")
                .leftJoin("bcf_resource AS c")
                .on("a.rsid = c.rsid")
                .where("a.lk_objt=?", 7)
                .and("a.lk_objt_id=?", puid)
                .and("c.is_disabled =?", 0)
                .select("a.rsid,a.lk_objt,c.cn_name,c.order_index,c.en_name,a.p_express")
                .getList(new ResourceExVo());
        return list;

    }

    // 组拥有的资源
    public static List<ResourceExVo> getResourceLinkedModelsByOpt5(Integer pgid) throws SQLException {

        List<ResourceExVo> list = DbBcfResourceApi.db().table("bcf_resource_linked AS a")
                .innerJoin("bcf_group AS b")
                .on("a.lk_objt_id=b.pgid")
                .leftJoin("bcf_resource AS c")
                .on("a.rsid = c.rsid")
                .where("a.lk_objt=?", 2)
                .and("b.pgid=?", pgid)
                .and("c.is_disabled =?", 0)
                .and("b.is_disabled =?", 0)
                .select("a.rsid,a.lk_objt,c.cn_name,c.order_index,c.en_name,a.p_express")
                .getList(new ResourceExVo());
        return list;

    }

    public static ResourceExVo getResourceLinkedModelsByOpt3(Integer rsid) throws SQLException {

        return DbBcfResourceApi.db().table("bcf_resource_linked AS a")
                .innerJoin("bcf_group AS b")
                .on("b.pgid = a.lk_objt_id")
                .where(" a.lk_objt =?", Config.lk_objt_group)
                .and("a.rsid =?", rsid)
                .and("b.r_pgid =?", 4)
                .and("b.is_disabled =?", 0)
                .limit(1)
                .select("a.rsid,b.order_index,b.cn_name,a.p_express")
//                .caching(DbBcfResourceApi.res_cache).usingCache(60 * 10).cacheTag("bcf_resource_linked_lk_objt_" + lk_objt + "_column_id_" + columnId).cacheTag("bcf_resource_linked_all")
                .getItem(new ResourceExVo());

    }

    /**
     * @description
     * @author wjn
     * @date 2019/5/14 2:09 PM
     * @param 
     * @return java.util.List<webapp.models.bcf.vo.BcfResourceExVoModel>
     */
    public static List<ResourceExVo> getResourceExView() throws SQLException {

        return DbBcfResourceApi.db().table("bcf_resource AS a")
                .innerJoin("bcf_resource_linked AS b")
                .on("a.rsid = b.rsid")
                .innerJoin("bcf_group AS c")
                .on("c.pgid=b.lk_objt_id")
                .where("c.r_pgid=?", 4)
                .and("a.is_disabled=?",0)
                .and("c.is_disabled=?",0)
                .and("b.lk_objt=?", 2)
//                .orderBy("c.order_index,a.order_index")
                .select("a.rsid,a.order_index,a.cn_name,a.en_name,a.is_disabled,b.lk_objt,b.p_express,c.cn_name AS pg_cn_name,c.en_name AS pg_en_name,c.is_disabled AS pg_is_disabled,c.order_index AS pg_order_index")
                .getList(new ResourceExVo());
    }

    /**
     * @description 获取用户所在组
     * @author wjn
     * @date 2019/5/14 2:00 PM
     * @param puid
     * @return java.util.List<webapp.models.bcf.BcfGroupModel>
     */
    public static List<GroupModel> getInGroup(Integer puid) throws SQLException {

        return DbBcfResourceApi.db().table("bcf_group AS a")
                .innerJoin("bcf_user_linked AS b")
                .on("a.pgid = b.lk_objt_id")
                .where("b.puid=?", puid)
                // 此处稍有不同，vs未加lk_objt=2
//                .and("b.lk_objt=?", 2)
                .and("a.is_disabled=?", 0)
                .select("*")
                .getList(new GroupModel());
    }

    public static List<ResourceLinkedModel> getUserOwenRsid(Integer puid) throws SQLException {

        return DbBcfResourceApi.db().table("bcf_resource_linked AS a")
                .where("a.lk_objt=?", 7)
                .and("a.lk_objt_id=?", puid)
                .select("*")
                .getList(new ResourceLinkedModel());
    }

    public static List<ResourceLinkedModel> getUserDepartRsid(Integer pgid) throws SQLException {

        // getInGroup得出的list pgid字段遍历该方法，获取部门权限, 用户权限与部门权限合并即为最终资源
        // 最终资源与getResourceExView list，比对rsid，得出，list，按照pg_order_index,order_index排序
        return DbBcfResourceApi.db().table("bcf_resource_linked AS a")
                .where("a.lk_objt=?", 2)
                .and("a.lk_objt_id=?", pgid)
                .select("*")
                .getList(new ResourceLinkedModel());
    }
    // avalibale for both table `bcf_resource_linked` & `bcf_user_linked`
    public static void removeResourceLinkByOpt(Integer lk_objt, Integer lk_objt_id, Integer firstColumnId, String tableName) throws SQLException {

        DbBcfResourceApi.db().table(tableName)
                .where(Config.BCF_RESOURCE_LINKED.equalsIgnoreCase(tableName) ? "rsid=?" : "puid=?", firstColumnId)
                .and("lk_objt=?", lk_objt)
                .and("lk_objt_id=?", lk_objt_id)
                .delete();

    }

    public static long insertBcfResource(ResourceModel resourceModel) throws SQLException {

        long id = DbBcfResourceApi.db().table("bcf_resource")
                .set("rs_code", resourceModel.rs_code)
                .set("cn_name", resourceModel.cn_name)
                .set("en_name", resourceModel.en_name)
                .set("uri_path", resourceModel.uri_path)
                .set("uri_target", resourceModel.uri_target)
                .set("ico_path", resourceModel.ico_path)
                .set("order_index", resourceModel.order_index)
                .set("note", resourceModel.note)
                .set("tags", resourceModel.tags)
                .set("is_disabled", resourceModel.is_disabled)
                .set("create_time", new Date())
                .set("last_update", new Date())
                .insert();

        return id;
    }

    public static boolean updateBcfResource(ResourceModel resourceModel) throws SQLException {

        boolean flag = DbBcfResourceApi.db().table("bcf_resource")
                .set("rs_code", resourceModel.rs_code)
                .set("cn_name", resourceModel.cn_name)
                .set("en_name", resourceModel.en_name)
                .set("uri_path", resourceModel.uri_path)
                .set("uri_target", resourceModel.uri_target)
                .set("ico_path", resourceModel.ico_path)
                .set("order_index", resourceModel.order_index)
                .set("note", resourceModel.note)
                .set("tags", resourceModel.tags)
                .set("is_disabled", resourceModel.is_disabled)
                .set("last_update", new Date())
                .where("rsid=?", resourceModel.rsid)
                .update() > 0;

        return flag;
    }

    public static ResourceModel selectBcfResource(Integer rsid) throws SQLException {

        ResourceModel bcfResourceModel = DbBcfResourceApi.db().table("bcf_resource")
                .where("rsid=?", rsid)
                .select("*")
                .getItem(new ResourceModel());

        return bcfResourceModel;
    }

    public static long getResourceLinkedCountByOpt(Integer lk_objt, Integer rsid, Integer lk_objt_id) throws SQLException {

        return DbBcfResourceApi.db().table("bcf_resource_linked")
                .where("lk_objt=?", lk_objt)
                .and("lk_objt_id=?", lk_objt_id)// columnName+"=?"
                .and("rsid=?", rsid)
                .count();

    }
}
