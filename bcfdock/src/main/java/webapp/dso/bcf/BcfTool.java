package webapp.dso.bcf;

import org.noear.bcf.BcfClientEx;
import org.noear.bcf.BcfUtilEx;
import org.noear.bcf.models.BcfGroupModelEx;
import org.noear.bcf.models.BcfResourceModelEx;
import org.noear.water.utils.TextUtils;
import webapp.dso.Session;
import webapp.models.MenuViewModel;

import java.sql.SQLException;
import java.util.List;

public class BcfTool {

    public static String systemCodeByPath(String path){
        int start_idx = path.indexOf(".");

        if(start_idx==1) {
            start_idx += 1;
            int end_idx = path.indexOf("/", start_idx);

            return path.substring(start_idx, end_idx);
        }else{
            return "";
        }
    }

    public static String clearSystemCodeByPath(String path){
       return path.replaceAll("/\\.[^/]*","");
    }


    public static MenuViewModel buildSystemMenus() throws SQLException {
        MenuViewModel viewModel = new MenuViewModel();

        int puid = Session.current().getPUID();
        BcfClientEx bcfClient = new BcfClientEx();

        StringBuilder sball = new StringBuilder();

        List<BcfGroupModelEx> sysList = bcfClient.getSystems();

        for (BcfGroupModelEx s : sysList) {
            bcfClient.system(s.pg_code);

            List<BcfGroupModelEx> modList = bcfClient.getModules();
            int modSize = 0;

            StringBuilder sb  =new StringBuilder();
            sb.append("<section>");
            sb.append("<header>").append(s.en_name).append("</header>");
            sb.append("<ul>");
            for (BcfGroupModelEx m : modList) {
                BcfResourceModelEx res = bcfClient.getUserFirstResourceByPack(puid, m.pgid);
                if(TextUtils.isEmpty(res.uri_path) == false){
                    sb.append("<li>")
                            .append("<a href='").append(BcfUtilEx.buildBcfUnipath(res)).append("' target='_top'>")
                            .append(m.cn_name)
                            .append("</a>")
                            .append("</li>");
                    modSize++;
                }

            }

            sb.append("</ul>");
            sb.append("</section>");

            if(modSize>0){

                if("#".equals(s.tags)){ //强制独占处理
                    sball.append("<div>");
                    sball.append(sb.toString());
                    sball.append("</div>");
                }else{
                    sball.append(sb.toString());
                }

                viewModel.count++;
            }
        }

        viewModel.code = sball.toString();

        return viewModel;
    }
}
