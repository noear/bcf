package webapp.controller.extend;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import webapp.controller.BaseController;
import webapp.controller.ViewModel;
import webapp.utils.PinYinUtil;


/**
 * Date: 2018/11/28
 * Time: 19:33
 * Author: Yukai
 * Description: 大家都爱汉语拼音，学好拼音从娃娃抓起
 **/
@Mapping("/bcf")
@Controller
public class ChineseController extends BaseController {

    @Mapping("tools/pinyin")
    public ViewModel toPinYin(String text) {

        String pinyin = PinYinUtil.getPinyinString(text);

        viewModel.set("pinyin", pinyin);

        return viewModel.set("code", 1).set("msg", "成功");
    }
}
