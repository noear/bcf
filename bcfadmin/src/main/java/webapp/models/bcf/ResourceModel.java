package webapp.models.bcf;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

@Getter
public class ResourceModel implements IBinder {
    public int rsid;
    public String rs_code;
    public String cn_name;
    public String en_name;
    public String uri_path;
    public String uri_target;
    public String ico_path;
    public int order_index;
    public String note;
    public String tags;
    public boolean is_disabled;
    public Date create_time;
    public Date last_update;
    //extra info
    public String pg_cn_name = "";

    @Override
    public void bind(GetHandlerEx s) {

        rsid = s.get("rsid").value(0);
        rs_code = s.get("rs_code").value(null);
        cn_name = s.get("cn_name").value(null);
        en_name = s.get("en_name").value(null);
        uri_path = s.get("uri_path").value(null);
        uri_target = s.get("uri_target").value(null);
        ico_path = s.get("ico_path").value(null);
        order_index = s.get("order_index").value(0);
        note = s.get("note").value(null);
        tags = s.get("tags").value(null);
        is_disabled = s.get("is_disabled").value(false);
        create_time = s.get("create_time").value(null);
        last_update = s.get("last_update").value(null);


    }


    @Override
    public IBinder clone() {
        return new ResourceModel();
    }

}