package webapp.controller.core;


import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import webapp.Config;
import webapp.controller.BaseController;
import webapp.controller.ViewModel;
import webapp.dso.BcfUtil;
import webapp.dso.db.DbBcfGroupApi;
import webapp.dso.db.DbBcfResourceApi;
import webapp.dso.db.DbBcfUserApi;
import webapp.models.bcf.GroupModel;
import webapp.models.bcf.ResourceLinkedModel;
import webapp.models.bcf.UserLinkedModel;
import webapp.utils.EncryptUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zxj
 * Date: 2018-11-28
 * Time: 14:25
 * Description: BCF权限.
 */

@Mapping("/bcf")
@Controller

public class PermissionController extends BaseController {

    @Mapping("permission/list")
    public ViewModel listPermission(Integer pGroupId) throws SQLException {

        List<GroupModel> list;
        if (pGroupId == null || pGroupId == 0) {
            list = DbBcfGroupApi.getBcfGroupListByPid(0);
            list.removeIf(o -> o.pgid == 4);
        } else {
            list = DbBcfGroupApi.getBcfGroupListByPid(pGroupId);
        }

        viewModel.set("list", list);

        return viewModel.set("code", 1).set("msg", "成功");
    }

    /**
     * @param pgid
     * @return webapp.controller.ViewModel
     * @description 部门详情
     * @author wjn
     * @date 2019/5/7 5:51 PM
     */
    @Mapping("permission/list_detail")
    public ViewModel listDetail(Integer pgid) throws SQLException {

        GroupModel bcfGroupModel = DbBcfGroupApi.getBcfGroupById(pgid);

        return viewModel.set("code", 1).set("msg", "成功").set("data", bcfGroupModel);
    }

    /**
     * @param pgid
     * @return webapp.controller.ViewModel
     * @description 部门资源信息
     * @author wjn
     * @date 2019/5/7 5:51 PM
     */
    @Mapping("permission/list_resource")
    public ViewModel listResource(Integer pgid) throws SQLException {

        List<ResourceLinkedModel> list = DbBcfResourceApi.getResourceLinkedModelsByOpt(2, pgid, "lk_objt_id");

        return viewModel.set("code", 1).set("msg", "成功").set("data", list);
    }

    @Mapping("permission/user/list")
    public ViewModel listUser(Integer lk_objt_id) throws SQLException {
        List<UserLinkedModel> userLinkedList = DbBcfUserApi.getUserLinkedModelByOpt(2, lk_objt_id);

        viewModel.set("list", userLinkedList);
        return viewModel.set("code", 1).set("msg", "成功");
    }

    @Mapping("link/user")
    public ViewModel linkResource(Integer lk_objt_id, String psids, Integer lk_objt) throws SQLException {

        for (String psid : psids.split(",")) {
            DbBcfUserApi.linkUserByOpt(lk_objt_id, Integer.parseInt(psid), lk_objt);
        }

        return viewModel.set("code", 1).set("msg", "成功");
    }


    /**
     * @param lk_objt,lk_objt_id,columnIds
     * @return webapp.controller.ViewModel
     * @description 用户移除资源
     * @author wjn
     * @date 2019/5/7 3:50 PM
     */
    @Mapping("link/remove/user")
    public ViewModel removeLink(Integer lk_objt, Integer lk_objt_id, String columnIds) throws Exception {

        for (String firstColumnId : columnIds.split(",")) {

            DbBcfResourceApi.removeResourceLinkByOpt(lk_objt, lk_objt_id, Integer.parseInt(firstColumnId), Config.BCF_RESOURCE_LINKED);

        }

        return viewModel.set("code", 1).set("msg", "成功");
    }

    /**
     * @param lk_objt,lk_objt_id,columnIds
     * @return webapp.controller.ViewModel
     * @description 从group移除用户
     * @author wjn
     * @date 2019/5/7 2:50 PM
     */
    @Mapping("link/remove/userFromGroup")
    public ViewModel removeuserFromGroup(Integer lk_objt, Integer lk_objt_id, String[] columnIds) throws Exception {

        for (String firstColumnId : columnIds) {

            DbBcfResourceApi.removeResourceLinkByOpt(lk_objt, lk_objt_id, Integer.parseInt(firstColumnId), Config.BCF_USER_LINKED);

        }

        return viewModel.set("code", 1).set("msg", "成功");
    }

    @Mapping("permission/add/user")
    public ViewModel addUser(UserLinkedModel userLinkedModel) throws Exception {

        long psid = 0;

        if (Utils.isNotEmpty(userLinkedModel.pass_wd)) {
            userLinkedModel.pass_wd = BcfUtil.buildBcfPassWd(userLinkedModel.user_id, userLinkedModel.pass_wd.trim());
        }

        if (userLinkedModel.puid == 0) {
            // build a new resource to the table of the database
            try {
                psid = DbBcfUserApi.addBcfUser(userLinkedModel);
            } catch (Exception e) {
                return viewModel.set("code", 0).set("msg", "未知错误");
            }

            if (psid == 0) {
                return viewModel.set("code", 0).set("msg", "传入参数有误");
            }
        } else {

            boolean flag = true;

            try {
                flag = DbBcfUserApi.updateBcfUser(userLinkedModel);
            } catch (Exception e) {
                return viewModel.set("code", 0).set("msg", "未知错误");
            }
            if (!flag) {
                return viewModel.set("code", 0).set("msg", "传入参数有误");
            }
        }
        return viewModel.set("code", 1).set("msg", "成功").set("psid", psid);
    }
}
