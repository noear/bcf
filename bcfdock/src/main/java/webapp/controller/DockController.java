package webapp.controller;

import org.noear.bcf.BcfClientEx;
import org.noear.bcf.models.BcfGroupModelEx;
import org.noear.bcf.models.BcfResourceModelEx;
import org.noear.snack.ONode;
import org.noear.water.utils.TextUtils;
import org.noear.weed.cache.CacheUsing;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Context;
import webapp.dso.CacheUtil;
import webapp.dso.Session;
import webapp.dso.bcf.BcfTool;
import webapp.models.MenuViewModel;

import java.net.URLDecoder;
import java.sql.SQLException;

/**
 * Created by noear on 18-10-10.
 */
@Controller
public class DockController extends BaseController {

    /** 显示所有权限 */
    @Mapping("/dock/home") //视图 返回
    public ModelAndView dock_home() throws SQLException {

        int puid = Session.current().getPUID();

        //获取所有模块菜单
        CacheUsing cu  =new CacheUsing(CacheUtil.dataCache);
        MenuViewModel vm = cu.getEx("user_menus_x_"+puid,()-> BcfTool.buildSystemMenus());

        int section_margin = 20;
        int header_margin = 5;

        if(vm.count>5) {
            section_margin = 20 - vm.count * 2;

            if (section_margin < 5) {
                section_margin = 5;
            }

            header_margin = 5-(vm.count/2);
            if(header_margin<0){
                header_margin = 0;
            }
        }

        viewModel.put("section_margin",section_margin);
        viewModel.put("header_margin",header_margin);
        viewModel.put("code", vm.code);

        return view("dock_home");
    }


    //支持外部url
    @Mapping("/**/$*") //视图 返回
    public ModelAndView dock1(Context ctx) {
        String uri = ctx.path();
        String query =ctx.queryString();

        uri = BcfTool.clearSystemCodeByPath(uri);

        try {
            BcfResourceModelEx res = BcfClientEx.getResourceByPath(uri);
            viewModel.set("fun_name", res.cn_name);
            viewModel.set("fun_url", optimizeUrl(res.note));

            if (query != null && query.indexOf("@=") >= 0) {
                viewModel.set("fun_type", 1);
            } else {
                viewModel.set("fun_type", 0);
            }
        } catch (Exception ex) {

        }

        return view("dock");
    }

    /** 支持内部虚拟地址（之前的$*已不需要了） */
    @Mapping("/**/@*") //视图 返回
    public ModelAndView dock2(Context ctx) throws Exception {
        String uri = ctx.path();
        String query = ctx.queryString();

        String fun_name = uri.split("/@")[1]; // /x/x/@x   =>  /x/x   +  服务监控
        String fun_url = uri.split("/@")[0];

        fun_url = BcfTool.clearSystemCodeByPath(fun_url);

        String newUrl = fun_url;
        String p = BcfTool.systemCodeByPath(uri);
        String r = ctx.param("__r");

        //如果有r参数传入，则用r.note获取域 (r = res_id)
        if(TextUtils.isEmpty(r) == false){
            BcfResourceModelEx res = BcfClientEx.getResourceByID(Integer.parseInt(r));
            if(res.note != null && res.note.indexOf("://")>0){
                newUrl = res.note + fun_url;
            }
        }

        if(TextUtils.isEmpty(p) == false) {

            //如果还没有域尝试从根包获取
            if (newUrl.indexOf("://") < 0) {
                BcfGroupModelEx pack = new BcfClientEx().system(p).getRoot();

                if (TextUtils.isEmpty(pack.uri_path) == false) {
                    newUrl = pack.uri_path + fun_url;
                    //记录cookie
                }
            }

            ctx.cookieSet("_lLnQIO4W", p, 60 * 60 * 24 * 365);
        }

        //传递参数
        if(TextUtils.isEmpty(query) == false) {
            if (newUrl.indexOf("?") > 0) {
                newUrl = newUrl + "&" + query;
            } else {
                newUrl = newUrl + "?" + query;
            }
        }


        try {


            viewModel.set("fun_name", URLDecoder.decode(fun_name, "utf-8"));
            viewModel.set("fun_url", optimizeUrl(newUrl));

            if (query != null && query.indexOf("@=") >= 0) {
                viewModel.set("fun_type", 1);
            } else {
                viewModel.set("fun_type", 0);
            }
        } catch (Exception ex) {

        }

        return view("dock");
    }

    public String optimizeUrl(String url) throws Exception{
        if(url == null){
            return null;
        }

        if(url.indexOf("{{") > 0){
            ONode tmp = BcfClientEx.getUserOpsx(Session.current().getPUID());
            for(String key : tmp.obj().keySet()){
                url = url.replace("{{"+key+"}}", tmp.get(key).getString());
            }

            return url;
        }else{
            return url;
        }

    }
}
