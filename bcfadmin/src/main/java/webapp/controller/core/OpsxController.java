package webapp.controller.core;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import webapp.controller.BaseController;
import webapp.controller.ViewModel;
import webapp.dso.db.DbBcfOpsxApi;
import webapp.models.bcf.OpsxModel;

import java.sql.SQLException;

/**
 * @Created by: Yukai
 * @Date: 2019/4/25 10:35
 * @Description : Yukai is so handsome xxD
 */
@Mapping("/bcf")
@Controller
public class OpsxController extends BaseController {

    @Mapping("show/opsx")
    public ViewModel getOpsxInfo(Integer lk_obj_id, Integer lk_objt) throws SQLException {

        OpsxModel opsx = DbBcfOpsxApi.getBcfOpsxViaCriteria(lk_obj_id, lk_objt);
        return viewModel.set("opsx", opsx).set("code", 1).set("msg", "成功");
    }

    /**
     * @description 插入或更新扩展信息
     * @author wjn
     * @date 2019/5/7 4:03 PM
     * @param lk_obj_id,lk_objt,opsx
     * @return webapp.controller.ViewModel
     */
    @Mapping("show/set_opsx")
    public ViewModel setOpsxInfo(Integer lk_obj_id, Integer lk_objt, String opsx) throws SQLException {

        OpsxModel bcfOpsxModel = new OpsxModel();
        bcfOpsxModel.lk_objt = lk_objt;
        bcfOpsxModel.lk_objt_id = lk_obj_id;
        bcfOpsxModel.tags = "S";
        bcfOpsxModel.opsx = opsx;

        long opsxOld = DbBcfOpsxApi.getBcfOpsxViaCriteriaCount(lk_obj_id, lk_objt);
        if (opsxOld > 0) {
            DbBcfOpsxApi.updateBcfOpsxViaCriteria(bcfOpsxModel);
            return viewModel.set("opsx", opsx).set("code", 1).set("msg", "成功");
        } else {
            Boolean result = DbBcfOpsxApi.setBcfOpsxViaCriteria(bcfOpsxModel);
            if (result) {
                return viewModel.set("opsx", opsx).set("code", 1).set("msg", "成功");
            }
        }

        return viewModel.set("opsx", opsx).set("code", 0).set("msg", "失败");
    }

}
