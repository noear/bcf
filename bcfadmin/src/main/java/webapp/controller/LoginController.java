package webapp.controller;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.cloud.annotation.CloudConfig;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.MethodType;
import org.noear.water.utils.ImageUtils;
import org.noear.water.utils.RandomUtils;
import webapp.utils.TextUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by noear on 14-9-10.
 */
@Controller
public class LoginController extends BaseController {

    @Mapping("/login") //视图 返回
    public ModelAndView login() {
        //Config.regWater(request);

        return view("login");
    }

    @Mapping("/")
    public void index() throws Exception{
        Context.current().redirect("/login");
    }
    //-----------------

    //ajax.path like "{view}/ajax/{cmd}"

    @Mapping("/login/ajax/check")  // Map<,> 返回[json]  (ViewModel 是 Map<String,Object> 的子类)
    public ViewModel login_ajax_check(String userName, String passWord, String validationCode, Context ctx) throws Exception {

        //验证码检查
        if (!validationCode.toLowerCase().equals(getValidation())) {
            return viewModel.set("code", 0).set("msg", "提示：验证码错误！");
        }

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
            return viewModel.set("code", 0).set("msg", "提示：请输入账号和密码！");
        }

        String user0 = Solon.cfg().get("bcfadmin.user","");
        String password0 = Solon.cfg().get("bcfadmin.password","");

        boolean isLogin = (user0.equals(userName) && password0.equals(passWord));


        if (isLogin == false)
            return viewModel.set("code", 0).set("msg", "提示：账号或密码不对！"); //set 直接返回；有利于设置后直接返回，不用另起一行
        else {
            ctx.sessionSet("user",user0);

            return viewModel.set("code", 1)
                    .set("msg", "ok")
                    .set("url", "/bcf/");

        }
    }


    /*
     * 获取验证码图片
     */
    @Mapping(value = "/login/validation/img",method = MethodType.GET, produces = "image/jpeg")
    public void getValidationImg(Context ctx) throws IOException {
        // 生成验证码存入session
        String code = RandomUtils.code(4);
        setValidation(code);

        // 获取图片
        BufferedImage bufferedImage = ImageUtils.getValidationImage(code);

        // 禁止图像缓存
        ctx.headerSet("Pragma", "no-cache");
        ctx.headerSet("Cache-Control", "no-cache");
        ctx.headerSet("Expires", "0");

        // 图像输出
        ImageIO.setUseCache(false);
        ImageIO.write(bufferedImage, "jpeg", ctx.outputStream());
    }

    public final String getValidation() {
        return (String) Context.current().session("Validation_String");
    }

    public final void setValidation(String validation) {
        Context.current().sessionSet("Validation_String", validation.toLowerCase());
    }
}
