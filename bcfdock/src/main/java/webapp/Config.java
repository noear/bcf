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

    public static String bcfdock_env() {
        return cfg("bcfdock_env").getString("");
    }

    public static String bcfdock_title() {
        return cfg("bcfdock_title").getString(Solon.cfg().appTitle());
    }

    private static DbContext db;
    public  static DbContext db(){
        return db;
    }

    @Init
    public void init() {
        db = new DbContext(Solon.cfg().getProp("bcf.db"));

        BcfClientEx.tryInit(CacheUtil.dataCache, db);
    }

    //获取一个缓存配置
    public static ConfigM cfg(String key) {
        return WaterClient.Config.get(Solon.cfg().appGroup(), key);
    }
}
