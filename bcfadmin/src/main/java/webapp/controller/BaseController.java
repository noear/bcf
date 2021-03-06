package webapp.controller;


import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.utils.IPUtils;


/**
 * Created by yuety on 14-9-11.
 */
public abstract class BaseController {

    /*分页默认长度(适合内容单行的列表)*/
    protected static int pageBigSize = 16;
    /*分页默认长度(适合内容两行的列表)*/
    protected static int pageSmlSize = 6;
    /*视图数据模型*/
    protected ViewModel viewModel = new ViewModel();

    //获取 StateSelectorTag 传来的值
    public int getState(Context request) {
        return getInt(request, "_state");
    }

    public int getInt(Context request, String key) {
        return request.paramAsInt(key, -1);
    }

    public String getIP(Context request) {

        return IPUtils.getIP(request);

    }

    /*
     * @return 输出一个视图（自动放置viewModel）
     * @param viewName 视图名字(内部uri)
     * */
    public ModelAndView view(String viewName) {
        //设置必要参数
        viewModel.put("root", "");

        viewModel.put("app", Solon.cfg().appTitle());

        viewModel.put("css", "/_static/css");
        viewModel.put("js", "/_static/js");
        viewModel.put("img", "/_static/img");
        viewModel.put("title", Solon.cfg().appTitle());

        return new ModelAndView(viewName + ".ftl", viewModel);
    }

    /*
     * @return 输出一个跳转视图
     * @prarm  url 可以是任何URL地址
     * */
    public void redirect(String url) {
        try {
            Context.current().redirect(url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
