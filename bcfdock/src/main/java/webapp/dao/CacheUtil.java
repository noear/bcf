package webapp.dao;

import org.noear.weed.cache.ICacheServiceEx;
import webapp.Config;

public class CacheUtil {

   public static ICacheServiceEx dataCache = Config.cfg("water_cache")
           .getCh(Config.water_service_name + "_DATA_CACHE", 60 * 5);
}
