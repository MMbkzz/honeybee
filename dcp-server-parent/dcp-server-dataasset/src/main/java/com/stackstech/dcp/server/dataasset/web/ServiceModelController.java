package com.stackstech.dcp.server.dataasset.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.core.page.PageUtil;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import com.stackstech.dcp.server.dataasset.api.ApiUrls;
import com.stackstech.dcp.server.dataasset.model.ServiceModel;
import com.stackstech.dcp.server.dataasset.service.ServiceModelService;
import com.stackstech.dcp.server.dataasset.vo.ServiceModelQueryVO;
import com.stackstech.dcp.server.dataasset.vo.ServiceModelVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据资产模型
 */
@RestController
@RequestMapping(ApiUrls.API_SERVICE_MODEL_URI)
public class ServiceModelController {

    private final Logger logger = LoggerFactory.getLogger(ServiceModelController.class);

    @Autowired
    private ServiceModelService serviceModelService;

    @Autowired
    private LoginUserManager loginUserManager;

    /**
     * 批量新增资产模型接口
     *
     * @param serviceModelVOS
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_ADD_MODEL_BATCH_URI)
    public ResponseEntity<?> addServiceModels(@RequestBody List<ServiceModelVO> serviceModelVOS, HttpServletRequest req) {
        try {
            return serviceModelService.addBatch(serviceModelVOS, req);
        } catch (Exception e) {
            logger.error("批量新增服务模型失败", e);
            return ResponseError.create(500, "批量新增服务模型失败");
        }
    }

    /**
     * 新增资产模型接口
     *
     * @param serviceModelVO
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_ADD_MODEL_URI)
    public ResponseEntity<?> addServiceModel(@RequestBody ServiceModelVO serviceModelVO, HttpServletRequest req) {
        try {
            return serviceModelService.add(serviceModelVO, req);
        } catch (Exception e) {
            logger.error("新增服务模型失败", e);
            return ResponseError.create(500, "新增服务模型失败");
        }
    }

    /**
     * 资产模型查询列表接口
     *
     * @param queryVO
     * @param page
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_QUERY_MODEL_URI)
    public ResponseEntity<?> queryServiceModels(ServiceModelQueryVO queryVO, Page page, HttpServletRequest req) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (page != null && page.getPageNo() != null) {
                PageUtil.page(page);
            }
            if (TokenUtil.getToken(req) != null) {
                LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(TokenUtil.getToken(req));
                String userId = loginUser.getRoleCodes().size() == 1 && loginUser.getRoleCodes().contains("data_engineer") ? String.valueOf(loginUser.getUserId()) : "";
                queryVO.setUserId(userId);
                queryVO.setRoles(loginUser.getRoleCodes());
            }
            List<Map<String, Object>> result = serviceModelService.queryAll(queryVO);
            int count = serviceModelService.countAll(queryVO);
            map.put("list", result);
            map.put("count", count);
        } catch (Exception e) {
            logger.error("服务模型查询列表失败", e);
            return ResponseError.create(500, "服务模型查询列表失败");
        }
        return ResponseOk.create(map);
    }

    /**
     * 获取父模型
     *
     * @param req
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICE_GET_PARENT_MODEL_URI)
    public ResponseEntity<?> queryParentModels(HttpServletRequest req) {
        List<ServiceModel> serviceModels = null;
        try {
            serviceModels = serviceModelService.queryAllParent();
        } catch (Exception e) {
            logger.error("父模型查询失败", e);
            return ResponseError.create(500, "父模型查询失败");
        }
        return ResponseOk.create(serviceModels);
    }

    /**
     * 查询数据资产模型详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_GET_MODEL_URI)
    public ResponseEntity<?> getServiceModel(@RequestParam String id) {
        ServiceModelVO serviceModelVO = null;
        try {
            serviceModelVO = serviceModelService.query(id);
        } catch (Exception e) {
            logger.error("获取服务模型失败", e);
            return ResponseError.create(500, "获取服务模型失败");
        }
        return ResponseOk.create(serviceModelVO);
    }

    /**
     * 编辑资产模型
     *
     * @param serviceModelVO
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_EDIT_MODEL_URI)
    public ResponseEntity<?> editServiceModel(@RequestBody ServiceModelVO serviceModelVO, HttpServletRequest req) {
        try {
            return serviceModelService.update(serviceModelVO, req);
        } catch (Exception e) {
            logger.error("更新服务模型失败", e);
            return ResponseError.create(500, "更新服务模型失败");
        }
    }

    /**
     * 删除资产模型
     *
     * @param map
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_DEL_MODEL_URI)
    public ResponseEntity<?> delServiceModel(@RequestBody Map<String, Object> map) {
        try {
            String id = (String) map.get("id");
            if (null != id && !"".equals(id)) {
                return serviceModelService.delete(id);
            } else {
                return ResponseError.create("删除服务模型失败");
            }
        } catch (Exception e) {
            logger.error("删除服务模型失败", e);
            return ResponseError.create("删除服务模型失败");
        }
    }

    /**
     * 重名校验
     *
     * @param modelName
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICE_CHECK_MODEL_URI)
    public ResponseEntity<?> checkServiceModel(String modelName) {
        try {
            ServiceModel serviceModel = serviceModelService.queryByName(modelName);
            if (serviceModel != null) {
                return ResponseError.create(500, "服务模型已经存在!");
            }
        } catch (Exception e) {
            logger.error("服务模型校验失败..", e);
            return ResponseError.create(500, "服务模型校验失败");
        }
        return ResponseOk.create("服务模型不存在!");
    }

}
