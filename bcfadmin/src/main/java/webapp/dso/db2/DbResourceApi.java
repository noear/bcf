package webapp.dso.db2;

import webapp.dso.LKOBJ;
import webapp.models.bcf.ResourceLinkedModel;
import webapp.models.bcf.ResourceModel;
import webapp.utils.TextUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * BCF 资源接口
 * */
public class DbResourceApi  extends DbApiBase {
    /**
     * 分获资源列表
     *
     * @sort default=null; name="null";
     * */
    public static List<ResourceModel> getResourceListBy(int lk_objt, int lk_objt_id, String sort) throws SQLException{
        return db().table("bcf_resource_linked l")
                .innerJoin("bcf_resource r").on("l.rsid = r.rsid")
                .where("l.lk_objt=?",lk_objt)
                .and("l.lk_objt_id=?",lk_objt_id)
                .build(tb->{
                    if(TextUtils.isEmpty(sort)){
                        tb.orderByAsc("r.order_index");
                    }else if("name".equals(sort)){
                        tb.orderByAsc("r.cn_name");
                    }
                })
                .select("r.*")
                .getList(new ResourceModel());
    }

    /**
     * 获取资源连接列表
     * */
    public static List<ResourceLinkedModel> getResourceLinksByObjt(int lk_objt, Integer... lk_objt_idS) throws SQLException {
        return db().table("bcf_resource_linked l")
                .where("l.lk_objt=?", lk_objt)
                .andIn("l.lk_objt_id ", Arrays.asList(lk_objt_idS))
                .select("r.*")
                .getList(new ResourceLinkedModel());
    }

    /**
     * 获取资源详情
     * */
    public static ResourceModel getResource(int rsid) throws SQLException{
        return db().table("bcf_resource")
                   .whereEq("rsid",rsid)
                   .select("*")
                   .getItem(new ResourceModel());
    }

    /**
     * 获取资源赋属列表
     * */
    public static List<ResourceLinkedModel> getResourceAscriptionBy(int rsid) throws SQLException {
        return
        db().table("bcf_resource_linked l")
                .innerJoin("bcf_user u").on("l.lk_objt_id = u.puid").and("l.lk_objt=?", LKOBJ.bcf_user)
                .select("l.lk_objt, l.lk_objt_id, l.p_express, u.cn_name lk_objt_name")
                .getList(new ResourceLinkedModel());

    }

    /**
     * 添加资源关系
     * */
    public static void addResourceLink(int rsid, int lk_objt, int lk_objt_id) throws SQLException{
        db().table("bcf_resource_linked")
                .set("rsid",rsid)
                .set("lk_objt",lk_objt)
                .set("lk_objt_id",lk_objt_id)
                .set("lk_operate","+")
                .set("p_express",2)
                .insert();
    }

    /**
     * 删除资源关系
     * */
    public static void delResourceLink(int rsid, int lk_objt, int lk_objt_id) throws SQLException{
        db().table("bcf_resource_linked")
                .whereEq("rsid",rsid)
                .andEq("lk_objt",lk_objt)
                .andEq("lk_objt_id",lk_objt_id)
                .delete();
    }
}
