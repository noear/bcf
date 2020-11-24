package webapp.controller;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;

/**
 * Created by yuety on 14-9-10.
 */
//非单例
@Controller
public class HomeController extends BaseController {


    @Mapping("/")
    public void index() {
        redirect("/login");
    }

    @Mapping("/bcf/")
    public ModelAndView home() {
        return view("/bcf/home");
    }

}
