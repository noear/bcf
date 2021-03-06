package webapp.models.bcf.vo;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

@Getter
public class GroupVo implements IBinder {
    public int pgid;
    public int p_pgid;
    public int r_pgid;
    public String pg_code;
    public String cn_name;
    public String en_name;
    public String uri_path;
    public int in_level;
    public boolean is_branch;
    public String tags;
    public int order_index;
    public boolean is_disabled;
    public boolean is_visibled;
    public Date create_time;
    public Date last_update;

    public int rsid;
    public int lk_objt;
    public int lk_objt_id;
    public String lk_operate;
    public long p_express;

    @Override
    public void bind(GetHandlerEx s) {

        pgid = s.get("pgid").value(0);
        p_pgid = s.get("p_pgid").value(0);
        r_pgid = s.get("r_pgid").value(0);
        pg_code = s.get("pg_code").value(null);
        cn_name = s.get("cn_name").value(null);
        en_name = s.get("en_name").value(null);
        uri_path = s.get("uri_path").value(null);
        in_level = s.get("in_level").value(0);
        is_branch = s.get("is_branch").value(false);
        tags = s.get("tags").value(null);
        order_index = s.get("order_index").value(0);
        is_disabled = s.get("is_disabled").value(false);
        is_visibled = s.get("is_visibled").value(false);
        create_time = s.get("create_time").value(null);
        last_update = s.get("last_update").value(null);

        rsid = s.get("rsid").value(0);
        lk_objt = s.get("lk_objt").value(0);
        lk_objt_id = s.get("lk_objt_id").value(0);
        lk_operate = s.get("lk_operate").value(null);
        p_express = s.get("p_express").value(0L);
    }


    @Override
    public IBinder clone() {
        return new GroupVo();
    }
}
