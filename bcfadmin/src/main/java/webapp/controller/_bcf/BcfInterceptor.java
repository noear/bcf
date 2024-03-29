package webapp.controller._bcf;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.solon.extend.health.HealthHandler;
import org.noear.water.WaterClient;

@Mapping(value = "**", before = true)
@Component
public class BcfInterceptor implements Handler {

    @Override
    public void handle(Context ctx) throws Throwable {
        //IP白名单校验
        String path = ctx.path();

        if (ctx.uri().getHost().indexOf("localhost") < 0) {
            String ip = ctx.realIp();

            if (Solon.cfg().isWhiteMode()) {
                if (WaterClient.Whitelist.existsOfServerIp(ip) == false) {
                    ctx.setHandled(true);
                    ctx.output(ip + ",not is whitelist!");
                    return;
                }
            }
        }

        if (path.indexOf("/ajax/") >= 0 ||
                path.startsWith("/login") ||
                path.startsWith("/run/") ||
                path.startsWith("/msg/") ||
                path.equals(HealthHandler.HANDLER_PATH)) {
            return;
        }

        if (ctx.session("user") == null) {
            ctx.setHandled(true);
            ctx.redirect("/login");
        }
    }
}