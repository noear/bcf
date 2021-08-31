package webapp;

import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;
import org.noear.solon.logging.utils.TagsMDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BcfdockApp {

    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger("water_log_admin");

        Solon.start(BcfdockApp.class, args).onError((err) -> {
            Context ctx = Context.current();

            if (ctx == null) {
                log.error("< Error: {}", err);
            } else {
                TagsMDC.tag1(ctx.pathNew());

                log.error("> Header: {}\n> Param: {}\n\n< Error: {}",
                        ONode.stringify(ctx.headerMap()),
                        ONode.stringify(ctx.paramMap()),
                        ctx.errors);
            }
        });
    }
}

