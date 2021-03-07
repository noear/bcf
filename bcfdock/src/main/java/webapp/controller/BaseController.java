package webapp.controller;


import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Context;
import org.noear.water.utils.IPUtils;
import webapp.Config;
import webapp.dao.Session;


public abstract class BaseController {

    //获取 StateSelectorTag 传来的值
    public int getState(Context context)
    {
        return getInt(context,"_state");
    }

    public int getInt(Context context, String key) {
        return context.paramAsInt(key);
    }

    public String getIP(Context context) {

        return IPUtils.getIP(context);

    }

    /*视图数据模型*/
    protected ViewModel viewModel = new ViewModel();

    /*分页默认长度(适合内容单行的列表)*/
    protected static int pageBigSize = 16;
    /*分页默认长度(适合内容两行的列表)*/
    protected static int pageSmlSize = 6;

    /*
    * @return 输出一个视图（自动放置viewModel）
    * @param viewName 视图名字(内部uri)
    * */
    public ModelAndView view(String viewName)
    {
        //设置必要参数
        viewModel.put("root", "");

        viewModel.put("app", Config.bcfdock_title());
        viewModel.put("env",Config.bcfdock_env());

        viewModel.put("css", "/_static/css");
        viewModel.put("js", "/_static/js");
        viewModel.put("img", "/_static/img");
        viewModel.put("title", Config.bcfdock_title());


        //当前用户信息(示例)
        viewModel.put("puid", Session.current().getPUID());
        viewModel.put("cn_name", Session.current().getUserName());


        return new ModelAndView("/"+viewName +".ftl", viewModel);
    }
}
