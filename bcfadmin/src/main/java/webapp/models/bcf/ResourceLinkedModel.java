package webapp.models.bcf;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

@Getter
public class ResourceLinkedModel implements IBinder {
    public int rsid;
    public int lk_objt;
    public int lk_objt_id;
    public String lk_operate;
    public long p_express;
    public String pg_cn_name;
    public String lk_objt_name;

    @Override
    public void bind(GetHandlerEx s) {

        rsid = s.get("rsid").value(0);
        lk_objt = s.get("lk_objt").value(0);
        lk_objt_id = s.get("lk_objt_id").value(0);
        lk_operate = s.get("lk_operate").value(null);
        p_express = s.get("p_express").value(0L);

        lk_objt_name = s.get("lk_objt_name").value(null);
        pg_cn_name = s.get("pg_cn_name").value(null);


    }


    @Override
    public IBinder clone() {
        return new ResourceLinkedModel();
    }

}