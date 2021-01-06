package webapp;

import org.noear.bcf.BcfClientEx;
import org.noear.solon.Solon;
import org.noear.water.model.ConfigM;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import org.noear.water.*;
import webapp.dao.CacheUtil;

public class Config {
    //public static String web_title = "跨系统通用管理平台";
    public static String water_config_tag = "water";
    public static String water_service_name = "bcfdock";

    public static String alarm_sign(){
        return cfg("alarm_sign").getString("");
    }

    public static String web_title(){
        return cfg("bcfdock_title").getString("跨系统通用管理平台");
    }

    public static void tryInit(){
        WeedConfig.isDebug = false;

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

        DbContext bcf_db = new DbContext(Solon.cfg().getProp("bcf.db"));

        BcfClientEx.tryInit(CacheUtil.dataCache, bcf_db);
    }

    //获取一个缓存配置
    public static ConfigM cfg(String key) {
        return WaterClient.Config.get(water_config_tag, key);
    }
}
