package webapp.dso.db2;

import webapp.dso.LKOBJ;
import webapp.models.bcf.ResourceLinkedModel;
import webapp.models.bcf.ResourceModel;
import webapp.models.bcf.UserLinkedModel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * BCF 权限接口
 * */
public class DbPermissionApi extends DbApiBase{
    /**
     * 获取组的资源列表
     * */
    public List<ResourceModel> getResourceListByGroup(int pgid) throws SQLException{
        return DbResourceApi.getResourceListBy(LKOBJ.bcf_group,pgid, null);
    }

    /*
    * 获取用户所有的组列表
    * */
    protected List<UserLinkedModel> getGroupListByUser(int puid) throws SQLException{
        return getUserLinksBy(puid, LKOBJ.bcf_group);
    }


    /**
     * 获取用户的资源列表
     *
     * @incGroup 是否包括所在组的资源
     * */
    public List<ResourceModel> getResourceListByUser(int puid, boolean incGroup) throws SQLException{
        Set<Object> rsidS = new HashSet<>();

        //获取个人资源
        List<ResourceLinkedModel> list = DbResourceApi.getResourceLinksByObjt(LKOBJ.bcf_user, puid);
        rsidS.addAll(list.stream().map(m->m.rsid).collect(Collectors.toList()));

        if(incGroup){
            //查询所在组列表
            List<UserLinkedModel> links = getGroupListByUser(puid);
            Integer[] pgidS = (Integer[])links.stream().map(m->m.lk_objt_id).toArray();

            //获取所在组资源
            List<ResourceLinkedModel> list2 = DbResourceApi.getResourceLinksByObjt(LKOBJ.bcf_user, pgidS);
            rsidS.addAll(list2.stream().map(m->m.rsid).collect(Collectors.toList()));
        }

        //查询并佃出资源
        return
        db().table("bcf_resource")
                .whereIn("rsid", rsidS)
                .build(tb-> {
                    tb.orderByAsc("r.order_index");
                })
                .select("r.*")
                .getList(new ResourceModel());
    }

    /**
     * 获取资源连接列表
     * */
    public static List<ResourceLinkedModel> getUserLinksByObjt(int lk_objt, int... lk_objt_idS) throws SQLException {
        return db().table("bcf_user_linked l")
                .where("l.lk_objt=?", lk_objt)
                .andIn("l.lk_objt_id ", Arrays.asList(lk_objt_idS))
                .select("r.*")
                .getList(new ResourceLinkedModel());
    }

    protected static List<UserLinkedModel> getUserLinksBy(int puid, int lk_objt) throws SQLException {
        return db().table("bcf_user_linked l")
                .where("l.puid=?", puid)
                .and("l.lk_objt=?", lk_objt)
                .select("r.*")
                .getList(new UserLinkedModel());
    }
}
