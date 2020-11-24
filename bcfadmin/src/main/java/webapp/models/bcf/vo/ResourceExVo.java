package webapp.models.bcf.vo;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Objects;

@Getter
public class ResourceExVo implements IBinder{
    public int rsid;
    public int lk_objt;
    public String cn_name;
    public String en_name;
    public Boolean is_disabled;
    public int order_index;
    public long p_express;

    public int pg_order_index;
    public Boolean pg_is_disabled;
    public String pg_cn_name;
    public String pg_en_name;

    @Override
    public void bind(GetHandlerEx s) {
        cn_name = s.get("cn_name").value(null);
        en_name = s.get("en_name").value(null);
        is_disabled = s.get("is_disabled").value(false);
        order_index = s.get("order_index").value(0);
        rsid = s.get("rsid").value(0);
        lk_objt = s.get("lk_objt").value(0);
        p_express = s.get("p_express").value(0L);

        pg_order_index = s.get("pg_order_index").value(0);
        pg_is_disabled = s.get("pg_is_disabled").value(false);
        pg_cn_name = s.get("pg_cn_name").value(null);
        pg_en_name = s.get("pg_en_name").value(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceExVo that = (ResourceExVo) o;
        return rsid == that.rsid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rsid);
    }

    @Override
    public IBinder clone() {
        return new ResourceExVo();
    }
}
