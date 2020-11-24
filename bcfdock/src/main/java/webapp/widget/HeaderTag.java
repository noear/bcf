package webapp.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.bcf.BcfClientEx;
import org.noear.bcf.BcfUtilEx;
import org.noear.bcf.models.BcfGroupModelEx;
import org.noear.bcf.models.BcfResourceModelEx;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.water.utils.TextUtils;
import webapp.Config;
import webapp.dao.Session;
import webapp.dao.bcf.BcfTool;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component("ftl:header")
public class HeaderTag implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (Session.current().getPUID() == 0) {   //检查用户是已登录
            Context.current().redirect("/login");
            return;
        }

        try {
            build(env,body);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void build(Environment env,TemplateDirectiveBody body) throws Exception {
        Context context = Context.current();
        //当前视图path //此处改过，xyj，20180831
        String cPath = context.path();

        BcfClientEx bcfClient = null;

        List<BcfGroupModelEx> list = null;
        {
            String p = BcfTool.systemCodeByPath(cPath);

            if (TextUtils.isEmpty(p)) {
                list = new ArrayList<>();
            } else {

                cPath = BcfTool.clearSystemCodeByPath(cPath);

                bcfClient = new BcfClientEx().system(p);
                list = bcfClient.getUserPacks(Session.current().getPUID());
            }
        }


        StringWriter sb = new StringWriter();
        sb.append("<header>");

        sb.append("<label>"); //new
        //cls1
        if (bcfClient == null) {
            sb.append(Config.web_title());
        } else {
            BcfGroupModelEx root = bcfClient.getRoot();

            if (TextUtils.isEmpty(root.en_name)) {
                sb.append(Config.web_title());
            } else {
                sb.append(root.en_name);
            }
        }

        sb.append("</label>");


        sb.append("<nav>");


        if (bcfClient != null) {
            for (BcfGroupModelEx g : list) {
                try {
                    BcfResourceModelEx res = bcfClient.getUserFirstResourceByPack(Session.current().getPUID(), g.pgid);

                    if (TextUtils.isEmpty(res.uri_path) == false) {
                        buildItem(sb, g.cn_name, res, cPath, g.uri_path); //::en_name 改为 uri_path
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(); //以防万一；万一出错头都看不见了
                }
            }
        }

        sb.append("</nav>");


        sb.append("<aside>");

        String temp = Session.current().getUserName();
        if(temp!=null) {
            sb.append("<t onclick='modifyMm(); return false;'>");
            sb.append("<i class='fa fa-user'></i> ");
            sb.append(temp);
            sb.append("</t>");
        }
        sb.append("<a onclick='_dock_home_open();'><img src='/_static/img/app_w.png'/></a>");

        sb.append("<a class='logout' href='/'><i class='fa fa-fw fa-circle-o-notch'></i>退出</a>");
        sb.append("</aside>");

        sb.append("</header>");


        env.getOut().write(sb.toString());

    }

    private void buildItem(StringWriter sb, String title, BcfResourceModelEx res, String cPath, String pack) {

        if (TextUtils.isEmpty(pack)) {
            return;
        }

        //此处改过，xyj，201811(uadmin)
        String newUrl = BcfUtilEx.buildBcfUnipath(res);

        if (cPath.indexOf(pack) == 0) {
            sb.append("<a class='sel' href='" + newUrl + "'>");
            sb.append(title);
            sb.append("</a>");
        } else {
            sb.append("<a href='" + newUrl + "'>");
            sb.append(title);
            sb.append("</a>");
        }

    }
}
