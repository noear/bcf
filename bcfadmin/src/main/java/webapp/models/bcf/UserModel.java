package webapp.models.bcf;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

@Getter
public class UserModel implements IBinder {
    public int puid;
    public String user_id;
    public int out_objt;
    public long out_objt_id;
    public String cn_name;
    public String en_name;
    public String pw_mail;
    public String tags;
    public String note;
    public boolean is_disabled;
    public boolean is_visibled;
    public Date create_time;
    public Date last_update;
    public String pass_wd;
    public int state;

    @Override
    public void bind(GetHandlerEx s) {

        puid = s.get("puid").value(0);
        user_id = s.get("user_id").value(null);
        out_objt = s.get("out_objt").value(0);
        out_objt_id = s.get("out_objt_id").value(0L);
        cn_name = s.get("cn_name").value(null);
        en_name = s.get("en_name").value(null);
        pw_mail = s.get("pw_mail").value(null);
        tags = s.get("tags").value(null);
        note = s.get("note").value(null);
        is_disabled = s.get("is_disabled").value(false);
        is_visibled = s.get("is_visibled").value(false);
        create_time = s.get("create_time").value(null);
        last_update = s.get("last_update").value(null);

        state = s.get("state").value(0);

        //不能显示出来
        //pass_wd = s.get("pass_wd").value(null);
    }


    @Override
    public IBinder clone() {
        return new UserModel();
    }

}