package webapp.controller.core;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import webapp.controller.BaseController;
import webapp.controller.ViewModel;
import webapp.dso.db.DbBcfUserApi;
import webapp.models.bcf.UserLinkedModel;
import webapp.models.bcf.UserModel;
import webapp.models.bcf.vo.UserRel;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Date: 2019/1/2
 * Time: 17:27
 * Author: Yukai
 * Description : 对用户进行操作
 **/

@Mapping("/bcf")
@Controller
public class UserController extends BaseController {

    @Mapping("user/delete/{puid}")
    public ViewModel deleteUser(Integer puid) throws SQLException {

        if (puid == 0) {
            return viewModel.set("code", 0).set("msg", "无效puid");
        }

        DbBcfUserApi.deleteUserById(puid);

        return viewModel.set("code", 1).set("msg", "success");

    }

    @Mapping("user/detail/{puid}")
    public ViewModel detailUser(Integer puid) throws SQLException {

        if (puid == 0) {
            return viewModel.set("code", 0).set("msg", "无效puid");
        }

        UserModel bcfUserModel = DbBcfUserApi.getUserModelById(puid);

        return viewModel.set("code", 1).set("msg", "success").set("data", bcfUserModel);

    }


    @Mapping("group/getUserConn")
    public ModelAndView getUserConn(int puid) throws IllegalAccessException, SQLException, InstantiationException {

        List<UserLinkedModel> linkedModels = DbBcfUserApi.getAllUserConn();

        List<UserRel> matchedList_left = linkedModels
                .stream()
                .filter(lk -> lk.lk_objt_id == puid)
                .map(lk -> {
                    UserRel rel = new UserRel();

                    UserModel user;

                    try {
                        user = DbBcfUserApi.getUserModelById(lk.puid);
                    } catch (SQLException e) {
                        user = new UserModel();
                    }
                    rel.puid = user.puid;
                    rel.cn_name = user.cn_name;
                    rel.rl = "<";
                    return rel;
                })
                .collect(Collectors.toList());

        List<UserRel> matchedList_right = linkedModels
                .stream()
                .filter(lk -> lk.puid == puid)
                .map(lk -> {
                    UserRel rel = new UserRel();
                    UserModel user;
                    try {
                        user = DbBcfUserApi.getUserModelById(lk.lk_objt_id);
                    } catch (SQLException e) {
                        user = new UserModel();
                    }
                    rel.puid = user.puid;
                    rel.cn_name = user.cn_name;
                    rel.rl = ">";
                    return rel;
                })
                .collect(Collectors.toList());

        matchedList_left.addAll(matchedList_right);

        viewModel.set("list", matchedList_left);

        return view("/bcf/permission_person_relation");
    }

}
