package webapp;

import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;
import org.noear.water.log.WaterLogger;

public class BcfdockApp {

    public static void main(String[] args) {
        WaterLogger logger = new WaterLogger("water_log_admin");

        Solon.start(BcfdockApp.class, args, (app) -> {
            Config.tryInit();
        }).onError((err) -> {
            Context ctx = Context.current();
            if (ctx == null) {
                logger.error("bcfdock", "", err);
            } else {
                logger.error("bcfdock", ctx.path(), err);
            }
        });
    }
}

