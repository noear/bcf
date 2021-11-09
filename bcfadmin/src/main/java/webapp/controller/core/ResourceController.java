package webapp.controller.core;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import webapp.Config;
import webapp.controller.BaseController;
import webapp.controller.ViewModel;
import webapp.dso.db.DbBcfResourceApi;
import webapp.models.bcf.ResourceModel;

import java.sql.SQLException;

/**
 * Date: 2018/11/28
 * Time: 11:25
 * Author: Yukai
 **/
@Mapping("/bcf")
@Controller
public class ResourceController extends BaseController {

    @Mapping("resource/update")
    public ViewModel updateResource(ResourceModel resourceModel) {

        long rsid = 0;

        if (resourceModel.rsid == 0) {
            // build a new resource to the table of the database
            try {
                rsid = DbBcfResourceApi.insertBcfResource(resourceModel);
            } catch (Exception e) {
                return viewModel.set("code", 0).set("msg", "未知错误");
            }

            if (rsid == 0) {
                return viewModel.set("code", 0).set("msg", "传入参数有误");
            }
        } else {

            boolean flag = true;

            try {
                flag = DbBcfResourceApi.updateBcfResource(resourceModel);
            } catch (Exception e) {
                return viewModel.set("code", 0).set("msg", "未知错误");
            }
            if (!flag) {
                return viewModel.set("code", 0).set("msg", "传入参数有误");
            }
        }
        return viewModel.set("code", 1).set("msg", "成功").set("rsid", rsid);
    }

    @Mapping("resource/update")
    public ViewModel updateResource(ResourceModel resourceModel, Integer pgid) throws SQLException {

        long rsid = 0;

        if (resourceModel.rsid == 0) {
            // build a new resource to the table of the database
            if (pgid == null) {
                return viewModel.set("code", 0).set("msg", "参数缺失pgid");
            }
            try {
                rsid = DbBcfResourceApi.insertBcfResource(resourceModel);
                // 添加bcf_resource_linked关联
                if (rsid == 0) {
                    return viewModel.set("code", 0).set("msg", "资源添加失败");
                }
                DbBcfResourceApi.linkResourceByOpt(pgid, (int) rsid, 2);
                return viewModel.set("code", 1).set("msg", "成功").set("rsid", rsid);
            } catch (Exception e) {
                return viewModel.set("code", 0).set("msg", "未知错误");
            }


        } else {

            boolean flag = true;

            try {
                flag = DbBcfResourceApi.updateBcfResource(resourceModel);
            } catch (Exception e) {
                return viewModel.set("code", 0).set("msg", "未知错误");
            }
            if (!flag) {
                return viewModel.set("code", 0).set("msg", "传入参数有误");
            }
        }
        return viewModel.set("code", 1).set("msg", "成功").set("rsid", rsid);
    }


    /**
     *
     * 此为20190419新增-wjn
     *
     */
    /**
     * 获取产品详情
     *
     * @param rsid
     * @return
     * @throws SQLException
     */
    @Mapping("resource/detail")
    public ViewModel getResourceDetail(Integer rsid) throws SQLException {

        if (rsid == null) {
            return viewModel.set("code", 0).set("msg", "参数缺失rsid");
        }
        ResourceModel bcfResourceModel = DbBcfResourceApi.selectBcfResource(rsid);
        return viewModel.set("code", 1).set("msg", "成功")
                .set("data", bcfResourceModel);
    }

    /**
     * @description 资源与用户接触绑定
     * @author wjn
     * @date 2019/5/7 10:35 AM
     * @param puids,rsid
     * @return webapp.controller.ViewModel
     */
    @Mapping("link/remove/resource_user")
    public ViewModel removeUserFromResource( String puids, Integer rsid) throws Exception {

        for (String puid : puids.split(",")) {

            DbBcfResourceApi.removeResourceLinkByOpt(Config.lk_objt_user, Integer.parseInt(puid), rsid, Config.BCF_RESOURCE_LINKED);

        }

        return viewModel.set("code", 1).set("msg", "成功");
    }
}
