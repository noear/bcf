package org.noear.bcf;

import org.noear.solon.core.handle.Context;

public abstract class BcfInterceptorBase {

    /** 用户ID */
    public abstract int getPUID();

    /** 验证是否有权限 */
    public boolean verifyUrlpath(String uri) throws Exception{
        return BcfClient.hasUrlpathByUser(getPUID(), uri);
    }

    /** 验证代理::true:通过,false未通过（可以重写）*/
    public void verifyHandle(Context ctx) throws Exception{
        String uri = ctx.path().toLowerCase();

        if (getPUID() > 0) {
            ctx.attrSet("user_puid", "" + getPUID());
            ctx.attrSet("user_name", BcfSessionBase.global().getUserName());
        }

        if (uri.indexOf("/ajax/") < 0 && uri.equals("/login") ==false) {
            if (BcfClient.hasUrlpath(uri)) {

                if(getPUID()==0){
                    ctx.redirect("/login");
                    ctx.setHandled(true);
                    return;
                }

                if (verifyUrlpath(uri) == false) {
                    ctx.outputAsHtml("抱歉，没有权限!");
                    ctx.setHandled(true);
                    return;
                }
            }
        }
    }
}
