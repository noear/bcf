package webapp.dao;

import org.noear.bcf.models.BcfUserModel;
import org.noear.bcf.BcfSessionBase;
import webapp.Config;


//Session对象
public class SessionBcf extends BcfSessionBase {
    @Override
    public String service() {
        return Config.water_service_name;
    }

    //////////////////////////////////
    //当前项目的扩展

    @Override
    public void loadModel(BcfUserModel user) throws Exception {
        setPUID(user.puid);
        setUserId(user.user_id);
        setUserName(user.cn_name);
    }


    public final String getValidation() {
        return get("Validation_String", null);
    }
    public final void setValidation(String validation) {
        set("Validation_String", validation.toLowerCase());
    }
}
