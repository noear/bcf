package webapp;

import org.noear.bcf.BcfClientEx;
import org.noear.okldap.LdapClient;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Init;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.CacheUtils;
import org.noear.weed.DbContext;
import org.noear.water.*;
import org.noear.weed.cache.ICacheServiceEx;

import java.util.Properties;

@Configuration
public class Config {

    public static String bcfdock_env() {
        return cfg("bcfdock_env").getString("");
    }

    public static String bcfdock_title() {
        return cfg("bcfdock_title").getString(Solon.cfg().appTitle());
    }


    private static ICacheServiceEx cache;
    public static ICacheServiceEx cache() {
        return cache;
    }

    private static DbContext db;
    public static DbContext db() {
        return db;
    }

    @Init
    public void init() {
        Properties p_cache = Solon.cfg().getProp("bcf.cache");
        Properties p_db = Solon.cfg().getProp("bcf.db");
        Properties p_ldap = Solon.cfg().getProp("bcf.ldap");

        db = new DbContext(p_db);
        cache = CacheUtils.getCh(p_cache);

        LdapClient ldapClient = null;
        if (p_ldap.size() > 0) {
            ldapClient = new LdapClient(p_ldap);
        }

        BcfClientEx.tryInit(cache, db, ldapClient);
    }

    //获取一个缓存配置
    public static ConfigM cfg(String key) {
        return WaterClient.Config.get(Solon.cfg().appGroup(), key);
    }
}
