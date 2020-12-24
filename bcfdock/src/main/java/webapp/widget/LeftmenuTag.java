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
import webapp.dao.Session;
import webapp.dao.bcf.BcfTool;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("ftl:leftmenu")
public class LeftmenuTag implements TemplateDirectiveModel {
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

    public void build(Environment env,TemplateDirectiveBody body) throws Exception {

        Context context = Context.current();
        //当前视图path //此处改过，20180831
        String cPath = context.path();
        StringWriter sb = new StringWriter();
        BcfClientEx bcfClient = null;

        List<BcfGroupModelEx> plist = null;
        {
            String p = BcfTool.systemCodeByPath(cPath);
            if (TextUtils.isEmpty(p)) {
                plist = new ArrayList<>();
            } else {

                cPath = BcfTool.clearSystemCodeByPath(cPath);

                bcfClient = new BcfClientEx().system(p);
                plist = bcfClient.getModules();
            }
        }

        int packID = 0;
        for (BcfGroupModelEx p : plist) {
            if (cPath.indexOf(p.uri_path) == 0) { //::en_name 改为 uri_path
                packID = p.pgid;
                break;
            }
        }

        sb.append("<menu>");
        sb.append("<div onclick=\"$('main').toggleClass('smlmenu');if(window.onMenuHide){window.onMenuHide();}\"><img src='/_static/img/menu_w.png'/></div>");
        sb.append("<items>");

        forPack(bcfClient, packID, sb, cPath);

        sb.append("</items>");
        sb.append("</menu>");


        env.getOut().write(sb.toString());

    }

    private void forPack(BcfClientEx bcfClient, int packID, StringWriter sb, String cPath) throws SQLException {
        if (bcfClient == null) {
            return;
        }

        List<BcfResourceModelEx> list = bcfClient.getUserResourcesByPack(Session.current().getPUID(), packID);

        for (BcfResourceModelEx res : list) {
            buildItem(sb, res, cPath);
        }
    }

    private void buildItem(StringWriter sb, BcfResourceModelEx res, String cPath) {
        if ("$".equals(res.cn_name)) {
            sb.append("<br/><br/>");
            return;
        }

        //此处改过，201811(uadmin)
        String newUrl = BcfUtilEx.buildBcfUnipath(res);

        //此处改过，20180831
        if (cPath.indexOf(res.uri_path) >= 0) //  /x/x   => /x/x/@x
        {
            sb.append("<a class='sel' href='" + newUrl + "'>");
            sb.append(res.cn_name);
            sb.append("</a>");
        } else {
            sb.append("<a href='" + newUrl + "'>");
            sb.append(res.cn_name);
            sb.append("</a>");
        }
    }
}
