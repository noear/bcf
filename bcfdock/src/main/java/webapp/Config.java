package webapp;

import org.noear.bcf.BcfClientEx;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Init;
import org.noear.water.model.ConfigM;
import org.noear.weed.DbContext;
import org.noear.water.*;
import webapp.dao.CacheUtil;

@Configuration
public class Config {

    public static String alarm_sign() {
        return cfg("alarm_sign").getString("");
    }

    public static String web_title() {
        return cfg("bcfdock_title").getString(Solon.cfg().appTitle());
    }

    @Init
    public void init() {
        DbContext bcf_db = new DbContext(Solon.cfg().getProp("bcf.db"));

        BcfClientEx.tryInit(CacheUtil.dataCache, bcf_db);
    }

    //获取一个缓存配置
    public static ConfigM cfg(String key) {
        return WaterClient.Config.get(Solon.cfg().appGroup(), key);
    }
}
