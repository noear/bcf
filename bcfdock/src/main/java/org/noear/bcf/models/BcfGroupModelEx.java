package org.noear.bcf.models;


import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;


public class BcfGroupModelEx implements IBinder
{
    public int pgid;
    public int p_pgid;
    public int r_pgid;
    public String pg_code;
    public String cn_name;
    public String en_name;
    public String uri_path;
    public int in_level;
    public String tags;
    public int order_index;
    public boolean is_disabled;
    public boolean is_visibled;
    public Date create_time;
    public Date last_update;

    public void bind(GetHandlerEx s)
    {
        //1.source:数据源
        //
        pgid = s.get("pgid").value(0);
        p_pgid = s.get("p_pgid").value(0);
        r_pgid = s.get("r_pgid").value(0);
        pg_code = s.get("pg_code").value(null);
        cn_name = s.get("cn_name").value(null);
        en_name = s.get("en_name").value(null);
        uri_path = s.get("uri_path").value(null);
        in_level = s.get("in_level").value(0);
        tags = s.get("tags").value(null);
        order_index = s.get("order_index").value(0);
        is_disabled = s.get("is_disabled").value(false);
        is_visibled = s.get("is_visibled").value(false);
        create_time = s.get("create_time").value(null);
        last_update = s.get("last_update").value(null);
    }

    public IBinder clone()
    {
        return new BcfGroupModelEx();
    }
}
