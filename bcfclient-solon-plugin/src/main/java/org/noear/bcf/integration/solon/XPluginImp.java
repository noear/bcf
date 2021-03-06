package org.noear.bcf.integration.solon;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.bcf.BcfClient;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.core.Plugin;
import org.noear.weed.DbContext;
import org.noear.weed.cache.memcached.MemCache;

import java.util.Properties;

public class XPluginImp implements Plugin {
    @Override
    public void start(SolonApp app) {
        String p_root = app.cfg().get("bcf.root");
        if(Utils.isEmpty(p_root)){
            p_root = Solon.cfg().appName();
        }

        Properties p_cache = app.cfg().getProp("bcf.cache");
        Properties p_db = app.cfg().getProp("bcf.db");

        //1.初始化
        if (p_cache.size() > 1 && p_db.size() > 1 && p_root != null) {

            MemCache cache = new MemCache(p_cache);
            DbContext db = getDbDo(p_db);

            BcfClient.tryInit(p_root, cache, db);
        }

        //2.加载domain.js
        app.get("/_session/domain.js", (ctx) -> {
            String domain = app.cfg().get("server.session.state.domain");
            if (isEmpty(domain) == false) {
                if (ctx.uri().getHost().indexOf(domain) >= 0) {
                    ctx.contentType("text/javascript");
                    ctx.output("try { document.domain = '" + domain + "'; }catch (err) { }");
                }
            }
        });
    }

    private DbContext getDbDo(Properties prop) {
        String url = prop.getProperty("url");

        if (isEmpty(url)) {
            return null;
        }


        DbContext db = new DbContext();

        HikariDataSource source = new HikariDataSource();

        String schema = prop.getProperty("schema");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String driverClassName = prop.getProperty("driverClassName");

        if (isEmpty(url) == false) {
            source.setJdbcUrl(url);
        }

        if (isEmpty(username) == false) {
            source.setUsername(username);
        }

        if (isEmpty(password) == false) {
            source.setPassword(password);
        }

        if (isEmpty(schema) == false) {
            source.setSchema(schema);
        }

        if (isEmpty(driverClassName) == false) {
            source.setDriverClassName(driverClassName);
        }

        db.dataSourceSet(source);
        db.schemaSet(schema);

        return db;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
