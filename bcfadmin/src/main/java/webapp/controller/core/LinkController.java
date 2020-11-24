package webapp.controller.core;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import webapp.Config;
import webapp.controller.BaseController;
import webapp.controller.ViewModel;
import webapp.dso.db.DbBcfGeneralApi;
import webapp.dso.db.DbBcfResourceApi;
import webapp.models.bcf.ResourceModel;
import webapp.models.bcf.vo.ExtraInfoVo;
import webapp.models.bcf.vo.ResourceExVo;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Date: 2018/11/26
 * Time: 17:40
 * Author: Yukai
 * Description : for manipulating the resources with the groups etc.
 **/
@Mapping("/bcf")
@Controller
public class LinkController extends BaseController {
    public static DbContext db(){
        return Config.bcf_db;
    }

    @Mapping("link/list")
    public ViewModel showList(Integer lk_objt, Integer lk_objt_id) throws Exception {

        List<ResourceModel> resourceList = DbBcfResourceApi.getResourceListByOpt(lk_objt, lk_objt_id);

        viewModel.set("list", resourceList);

        return viewModel.set("code", 1).set("msg", "成功");
    }

    //显示用户关联的资源信息
    @Mapping("link/list/user_resource_all")
    public ViewModel showExtraList(Integer puid) throws Exception {

        if (puid == 0) {
            return viewModel.set("code", 0).set("msg", "无效puid");
        }

        // 用户有的资源
        List<ResourceExVo> allLists = new ArrayList<>();

        List<ResourceExVo> userList = db().table("bcf_resource_linked AS a")
                .leftJoin("bcf_resource AS c")
                .on("a.rsid = c.rsid")
                .where("a.lk_objt=?", 7)
                .and("a.lk_objt_id=?", puid)
                .and("c.is_disabled =?", 0)
                .select("a.rsid,a.lk_objt,c.cn_name,c.order_index,c.en_name,a.p_express")
                .getList(new ResourceExVo());
        allLists.addAll(userList);

        // 用户所在组有的资源
        List<Object> groupIDs = db().table("bcf_group AS a")
                .innerJoin("bcf_user_linked AS b")
                .on("a.pgid = b.lk_objt_id")
                .where("b.puid=?", puid)
                .and("a.is_disabled=?", 0)
                .select("a.pgid")
                .getArray(0);

        List<ResourceExVo> list2 = db().table("bcf_resource_linked AS a")
                .innerJoin("bcf_group AS b")
                .on("a.lk_objt_id=b.pgid")
                .leftJoin("bcf_resource AS c")
                .on("a.rsid = c.rsid")
                .where("a.lk_objt=?", 2)
                .andIn("b.pgid", groupIDs)
                .and("c.is_disabled =?", 0)
                .and("b.is_disabled =?", 0)
                .select("a.rsid,a.lk_objt,c.cn_name,c.order_index,c.en_name,a.p_express")
                .getList(new ResourceExVo());


        allLists.addAll(list2);


        // 去重
        List<ResourceExVo> departList = allLists.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.rsid))),
                        ArrayList::new));

        List<Object> idList = departList.stream().map(m->m.rsid).collect(Collectors.toList());

        Map<String,Object> departMap = db().table("bcf_resource_linked AS a")
                .innerJoin("bcf_group AS b")
                .on("b.pgid = a.lk_objt_id")
                .where(" a.lk_objt =?", Config.lk_objt_group)
                .andIn("a.rsid", idList)
                .and("b.r_pgid =?", 4)
                .and("b.is_disabled =?", 0)
                .select("a.rsid,b.order_index,b.cn_name,a.p_express")
                .getDataList()
                .toMap("rsid");

        //过滤
        List<ResourceExVo> newList = departList.stream().filter((val) -> {
            try {
                DataItem b = (DataItem) departMap.get(""+val.rsid);  //DbBcfResourceApi.getResourceLinkedModelsByOpt3(val.rsid);
                // 资源没有附属在组下面，属于无效的资源，不给显示
                if (b.getString("cn_name") == null) {
                    return false;
                }
                val.pg_cn_name = b.getString("cn_name");
                val.pg_order_index = b.getInt("order_index");
                return true;
            } catch (Exception e) {
                return false;
            }
        }).collect(Collectors.toList());

        newList.sort(Comparator.comparingInt((ResourceExVo o) -> o.pg_order_index).thenComparingInt(o -> o.order_index));

        return viewModel.set("code", 1)
                        .set("msg", "成功")
                        .set("data", newList);
    }


    /**
     * @description 资源与组绑定
     * @author wjn
     * @date 2019/5/7 10:26 AM
     * @param lk_objt_id,rsids,lk_objt
     * @return webapp.controller.ViewModel
     */
    @Mapping("link/resource")
    public ViewModel linkResource(Integer lk_objt_id, String[] rsids, Integer lk_objt) throws SQLException {

        for (String rsid : rsids) {
            // 此处改过20190419-wjn
            // 如果有记录存在，则跳过
            long rowCount = DbBcfResourceApi.getResourceLinkedCountByOpt(lk_objt, Integer.parseInt(rsid), lk_objt_id);
            if (rowCount == 0) {
                DbBcfResourceApi.linkResourceByOpt(lk_objt_id, Integer.parseInt(rsid), lk_objt);
            }
        }

        return viewModel.set("code", 1).set("msg", "成功");

    }

    /**
     * @description 用户与资源绑定
     * @author wjn
     * @date 2019/5/7 10:25 AM
     * @param puids,rsid
     * @return webapp.controller.ViewModel
     */
    @Mapping("link/userToResource")
    public ViewModel linkResource(String[] puids, Integer rsid) throws SQLException {

        for (String puid : puids) {
            // 此处改过20190507-wjn
            // 如果有记录存在，则跳过
            long rowCount = DbBcfResourceApi.getResourceLinkedCountByOpt(Config.lk_objt_user, rsid, Integer.parseInt(puid));
            if (rowCount == 0) {
                DbBcfResourceApi.linkResourceByOpt(Integer.parseInt(puid), rsid, Config.lk_objt_user);
            }
        }

        return viewModel.set("code", 1).set("msg", "成功");

    }

    // 资源信息
    @Mapping("link/extra")
    public ModelAndView showExtraInfo(Integer lk_objt, Integer rsid) throws Exception {

        List<ExtraInfoVo> infoList = DbBcfGeneralApi.getExtraInfoByOpt(lk_objt, rsid);

        viewModel.set("list", infoList);
        viewModel.set("rsid", rsid);

        return view("/bcf/resource_attach");

    }


    //columnIds in this case is the array of the rsid(s)
    @Mapping("link/remove/resource")
    public ViewModel removeLink(Integer lk_objt, Integer lk_objt_id, String[] columnIds) throws Exception {

        for (String firstColumnId : columnIds) {

            DbBcfResourceApi.removeResourceLinkByOpt(lk_objt, lk_objt_id, Integer.parseInt(firstColumnId), Config.BCF_RESOURCE_LINKED);

        }

        return viewModel.set("code", 1).set("msg", "成功");
    }
}
