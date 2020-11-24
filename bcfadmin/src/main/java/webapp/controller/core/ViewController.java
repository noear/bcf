package webapp.controller.core;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import webapp.controller.BaseController;

/**
 * @author wjn
 * @description: 返回前端视图
 */
@Controller
@Mapping("/bcf")
public class ViewController extends BaseController {

    @Mapping("resource")
    public ModelAndView index() {


        return view("/bcf/resource");
    }

    @Mapping("permission")
    public ModelAndView permission() {


        return view("/bcf/permission");
    }

    @Mapping("resource_base")
    public ModelAndView resourceDetail(String data, String lk_objt_id) {


        return view("/bcf/resource_base");
    }

    @Mapping("permission_user_base")
    public ModelAndView permissionBase(String data, String lk_objt_id) {


        return view("/bcf/permission_user_base");
    }

    @Mapping("permission_list")
    public ModelAndView permissionList() {
        return view("/bcf/permission_list");
    }

    @Mapping("permission_add")
    public ModelAndView permissionAdd() {
        return view("/bcf/permission_add");
    }

    @Mapping("permission_orgi_relation")
    public ModelAndView permissionOrgiRelation() {
        return view("/bcf/permission_orgi_relation");
    }

    @Mapping("permission_user_extra")
    public ModelAndView permissionExtra() {
        return view("/bcf/permission_user_extra");
    }

}
