package webapp;

import org.noear.solon.Solon;

public class BcfadminApp {
    public static void main(String[] args) {
        Solon.start(BcfadminApp.class, args, (app) -> {
            Config.tryInit();

            app.get("/_session/domain.js", c -> c.output(""));
        });
    }
}

