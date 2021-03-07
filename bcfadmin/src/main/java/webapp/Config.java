package webapp;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Init;
import org.noear.solon.cloud.CloudClient;
import org.noear.weed.DbContext;

@Configuration
public class Config {

    public static final int lk_objt_group = 2;
    public static final int lk_objt_user = 7;

    public static final String BCF_RESOURCE_LINKED = "bcf_resource_linked";
    public static final String BCF_USER_LINKED = "bcf_user_linked";

    /*相关状态控制*/
    public static DbContext bcf_db;

    @Init
    public void init() {
        bcf_db = new DbContext(Solon.cfg().getProp("bcf.db"));
        CloudClient.configLoad(Solon.cfg().appGroup(), "bcfadmin.yml");
    }
}
