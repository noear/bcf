package webapp.controller.core;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.core.handle.ModelAndView;
import webapp.controller.BaseController;
import webapp.controller.ViewModel;
import webapp.dso.db.DbBcfGroupApi;
import webapp.dso.db.DbBcfResourceApi;
import webapp.models.bcf.GroupModel;
import webapp.models.bcf.vo.ResourceExVo;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Date: 2018/11/26
 * Time: 10:01
 * Author: Yukai
 * Descriptionn :  对bcf_group 表中信息的展示与操作
 **/


@Controller
@Mapping("/bcf")
public class GroupController extends BaseController {

    @Mapping("group/list/{ppgid}")
    public ViewModel listGroup(Integer ppgid) throws SQLException {

        List<GroupModel> groupList =
                DbBcfGroupApi.getBcfGroupListByPid(ppgid);

        viewModel.set("list", groupList);

        return viewModel.set("code", 1).set("msg", "成功");
    }

    // add the operation for both updating | adding
    @Mapping("group/add")
    public ViewModel updateGroup(GroupModel groupModel) throws SQLException {
        if (groupModel.pgid == 0) {
            //adding operation
            DbBcfGroupApi.insertBcfGroup(groupModel);
        } else {
            DbBcfGroupApi.updateBcfGroup(groupModel);
        }

        return viewModel.set("code", 1).set("msg", "成功");

    }

    @Mapping("group/getGroupConn")
    public ModelAndView getGroupConn(int puid) throws IllegalAccessException, SQLException, InstantiationException {

        List<GroupModel> groupList = DbBcfGroupApi.getGroupListByUserId(puid);

        groupList.sort(Comparator.comparingInt(o -> o.order_index));
        viewModel.set("list", groupList);
        return view("/bcf/permission_orgi_relation");
    }

    /**
     * @description 获取部门下的资源信息
     * @author wjn
     * @date 2019/5/8 3:07 PM
     * @param pgid
     * @return webapp.controller.ViewModel
     */
    @Mapping("group/depart_get_resource")
    public ViewModel departGetGroupConn(int pgid) throws IllegalAccessException, SQLException, InstantiationException {

        List<ResourceExVo> departList = DbBcfResourceApi.getResourceLinkedModelsByOpt2(pgid);

        List<ResourceExVo> newList = departList.stream().map((val) -> {
            try {
                ResourceExVo b = DbBcfResourceApi.getResourceLinkedModelsByOpt3(val.rsid);
                val.pg_cn_name = b.cn_name;
                return val;
            } catch (Exception e) {
                return null;
            }
        }).collect(Collectors.toList());

        return viewModel.set("code", 1).set("msg", "成功").set("data", newList);
    }


}
