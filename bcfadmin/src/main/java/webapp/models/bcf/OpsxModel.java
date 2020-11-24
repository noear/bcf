package webapp.models.bcf;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

@Getter
public class OpsxModel implements IBinder {
    public int lk_objt;
    public int lk_objt_id;
    public String tags;
    public String opsx;

    @Override
    public void bind(GetHandlerEx s) {

        lk_objt = s.get("lk_objt").value(0);
        lk_objt_id = s.get("lk_objt_id").value(0);
        tags = s.get("tags").value(null);
        opsx = s.get("opsx").value(null);


    }


    @Override
    public IBinder clone() {
        return new OpsxModel();
    }

}