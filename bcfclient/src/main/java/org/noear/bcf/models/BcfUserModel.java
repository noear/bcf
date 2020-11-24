package org.noear.bcf.models;

import lombok.Getter;
import lombok.Setter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

@Getter
@Setter
public class BcfUserModel implements IBinder
{
    public int puid;
    public String user_id;
    public int out_objt;
    public long out_objt_id;
    public String cn_name;
    public String en_name;
    public String pw_mail;
    public String tags;
    public boolean is_disabled;
    public boolean is_visibled;
    public Date create_time;
    public Date last_update;
    public String pass_wd;
    public int state;

    public void bind(GetHandlerEx s)
    {
        //1.source:数据源
        //
        puid = s.get("puid").value(0);
        user_id = s.get("user_id").value(null);
        out_objt = s.get("out_objt").value(0);
        out_objt_id = s.get("out_objt_id").longValue(0l);
        cn_name = s.get("cn_name").value(null);
        en_name = s.get("en_name").value(null);
        pw_mail = s.get("pw_mail").value(null);
        tags = s.get("tags").value(null);
        is_disabled = s.get("is_disabled").value(false);
        is_visibled = s.get("is_visibled").value(false);
        create_time = s.get("create_time").value(null);
        last_update = s.get("last_update").value(null);
        pass_wd = s.get("pass_wd").value(null);
        state = s.get("state").value(0);
    }

    public IBinder clone()
    {
        return new BcfUserModel();
    }
}