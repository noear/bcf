package webapp.dao;

import org.noear.solon.Solon;
import org.noear.weed.cache.ICacheServiceEx;
import webapp.Config;

public class CacheUtil {

   public static ICacheServiceEx dataCache = Config.cfg("water_cache")
           .getCh(Solon.cfg().appName() + "_DATA_CACHE", 60 * 5);
}
