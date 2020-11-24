package webapp.models.bcf.vo;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

@Getter
public class UserOwenRsidVo implements IBinder{
    private String cn_name;
    private String en_name;
    private String pg_cn_name;
    private String pg_en_name;
    private int is_disabled;
    private int order_index;

    private int pg_is_disabled;
    private int pg_order_index;

    @Override
    public void bind(GetHandlerEx s) {

        cn_name = s.get("cn_name").value(null);
        en_name = s.get("en_name").value(null);
        pg_cn_name = s.get("pg_cn_name").value(null);
        pg_en_name = s.get("pg_en_name").value(null);
        is_disabled = s.get("is_disabled").value(0);
        pg_is_disabled = s.get("pg_is_disabled").value(0);
        order_index = s.get("order_index").value(0);
        pg_order_index = s.get("pg_order_index").value(0);
    }


    @Override
    public IBinder clone() {
        return new UserOwenRsidVo();
    }
}
