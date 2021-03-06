package webapp;

import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BcfdockApp {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger("water_log_admin");

        Solon.start(BcfdockApp.class, args).onError((err) -> {
            Context ctx = Context.current();
            if (ctx == null) {
                logger.error("bcfdock", "", err);
            } else {
                logger.error("bcfdock", ctx.path(), err);
            }
        });
    }
}

