package webapp;

import org.noear.solon.Solon;
import org.noear.water.WaterClient;
import org.noear.weed.DbContext;

public class Config {

    public static final int lk_objt_group = 2;
    public static final int lk_objt_user = 7;

    public static final String BCF_RESOURCE_LINKED = "bcf_resource_linked";
    public static final String BCF_USER_LINKED = "bcf_user_linked";

    public static String web_title = "BCF";

    /*相关状态控制*/
    public static DbContext bcf_db;


    public static void tryInit(){
        WaterClient.Config.getProperties("water_session").forEach((k, v) -> {
            if (Solon.cfg().isDebugMode()) {
                String key = k.toString();
                if (key.indexOf(".session.") < 0) {
                    Solon.cfg().put(k, v);
                }
            } else {
                Solon.cfg().put(k, v);
            }
        });

        bcf_db = new DbContext(Solon.cfg().getProp("bcf.db"));
    }
}
