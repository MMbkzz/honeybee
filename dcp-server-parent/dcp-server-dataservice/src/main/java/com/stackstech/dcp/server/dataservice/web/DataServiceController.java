package com.stackstech.dcp.server.dataservice.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import com.stackstech.dcp.server.dataservice.api.ApiUrls;
import com.stackstech.dcp.server.dataservice.model.DataService;
import com.stackstech.dcp.server.dataservice.service.AppDataService;
import com.stackstech.dcp.server.dataservice.vo.AppVerifyVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据服务
 */
@RestController
@RequestMapping(ApiUrls.API_SERVICE_URI)
public class DataServiceController {

    private final Logger log = LoggerFactory.getLogger(DataServiceController.class);

    @Autowired
    private AppDataService appDataService;

    @Autowired
    private LoginUserManager loginUserManager;

    /**
     * 数据服务查询列表接口
     *
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_QUERY_URI)
    public ResponseEntity<?> queryDataServices(HttpServletRequest req) {
        try {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(TokenUtil.getToken(req));
            String userId = String.valueOf((loginUser.getRoleCodes().contains("data_engineer") || loginUser.getRoleCodes().contains("data_app")) ? loginUser.getUserId() : "");
            Map<String, Object> map = new HashMap<>();
            map.put("pageNo", req.getParameter("pageNo"));
            map.put("pageSize", req.getParameter("pageSize"));
            map.put("areaName", req.getParameter("areaName"));
            map.put("topicName", req.getParameter("topicName"));
            map.put("modelName", req.getParameter("modelName"));
            map.put("dataServiceName", req.getParameter("dataServiceName"));
            map.put("dataServiceId", req.getParameter("dataServiceId"));
            map.put("typeCode", req.getParameter("typeCode"));
            map.put("statusCode", req.getParameter("statusCode"));
            map.put("queryString", req.getParameter("queryString"));
            map.put("queryType", req.getParameter("queryType"));
            map.put("userId", userId);
            map.put("roleCodes", loginUser.getRoleCodes());


            map = appDataService.queryAllByQuery(map);
            return ResponseOk.create(map);
        } catch (Exception e) {
            log.error("获取数据服务列表失败!!", e);
            return ResponseError.create("获取数据服务列表失败");
        }
    }


    /**
     * 数据服务查询列表<获取授权服务列表></>
     *
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_QUERY_AUTH_URI)
    public ResponseEntity<?> queryAuthDataServices(String typeCode, String appId) {
        List<DataService> dataServices = null;
        try {
            dataServices = appDataService.queryDataservices(typeCode, appId);
        } catch (Exception e) {
            log.error("获取数据服务列表失败!!", e);
            return ResponseError.create("获取数据服务列表失败");
        }
        return ResponseOk.create(dataServices);
    }

    /**
     * 查询数据数据服务详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_GET_URI)
    public ResponseEntity<?> getDataService(@RequestParam String id, HttpServletRequest req) {
        try {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(TokenUtil.getToken(req));
            String userId = String.valueOf(loginUser.getRoleCodes().contains("data_app") ? loginUser.getUserId() : "");
            DataService dataService = appDataService.query(id, userId);
            return ResponseOk.create(dataService);
        } catch (Exception e) {
            log.error("获取数据服务失败!!", e);
            return ResponseError.create("获取数据服务失败");
        }
    }

    /**
     * 编辑数据服务
     *
     * @param dataService
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_UPDATE_URI)
    public ResponseEntity<?> editDataService(@RequestBody DataService dataService) {
        try {
            appDataService.update(dataService);
        } catch (Exception e) {
            log.error("更新数据服务失败!!", e);
            return ResponseError.create("更新数据服务失败");
        }
        return ResponseOk.create("更新数据服务成功");
    }

    /**
     * 删除数据服务
     *
     * @param map
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_DELETE_URI)
    public ResponseEntity<?> delDataService(@RequestBody Map<String, Object> map) {
        try {
            String id = (String) map.get("id");
            if (StringUtils.isNotEmpty(id)) {
                int i = appDataService.delete(id);
                if (i <= 0) {
                    return ResponseError.create("删除数据服务失败");
                }
            }
        } catch (Exception e) {
            log.error("删除数据服务失败", e);
            return ResponseError.create("删除数据服务失败");
        }
        return ResponseOk.create("删除数据服务成功");
    }

    /**
     * 重名校验
     *
     * @param dataServiceName
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICE_CHECK_URI)
    public ResponseEntity checkDataService(String dataServiceName) {
        try {
            DataService service = appDataService.queryByName(dataServiceName);
            if (service != null) {
                return ResponseError.create(500, "服务已经存在!");
            }
        } catch (Exception e) {
            log.error("服务校验失败..", e);
            return ResponseError.create(500, "服务校验失败");
        }
        return ResponseOk.create("服务不存在!");
    }

    /**
     * 服务授权
     *
     * @param map
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICE_AUTHORIZATION_URI)
    public ResponseEntity<?> authDataService(@RequestBody Map<String, Object> map) {
        if (map == null) {
            return ResponseError.create(400, "请求参数为空");
        }
        try {
            boolean flag = appDataService.auth(map);
            if (!flag) {
                return ResponseError.create(500, "服务授权失败");
            }
        } catch (Exception e) {
            log.error("服务授权失败", e);
            return ResponseError.create(500, "服务授权失败");
        }
        return ResponseOk.create("服务授权成功!");
    }

    /**
     * 验证服务
     *
     * @param appVerify
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICE_VERIFICATION_URI)
    public ResponseEntity<?> verifyDataService(@RequestBody AppVerifyVO appVerify) {
        if (appVerify == null) {
            return ResponseError.create(400, "请求参数为空");
        }
        try {
            return appDataService.verify(appVerify);
        } catch (Exception e) {
            log.error("服务验证失败", e);
            return ResponseError.create(500, "服务验证失败");
        }
    }

    /**
     * 数据服务(启用禁用)
     *
     * @param dataService
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICE_STATUS_URI)
    public ResponseEntity<?> changeServiceStatus(DataService dataService) {
        DataService service = null;
        try {
            int i = appDataService.update(dataService);
            if (i <= 0) {
                return ResponseError.create(500, "修改服务状态失败!", dataService);
            }
        } catch (Exception e) {
            log.error("修改服务状态失败..", e);
            return ResponseError.create(500, "修改服务状态失败", dataService);
        }
        return ResponseOk.create(service);
    }

    /**
     * 下载文件
     *
     * @param req
     * @param resp
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_VERIFY_DOWNLOAD_URI)
    public void downloadDataservice(@RequestParam String version, HttpServletRequest req, HttpServletResponse resp) {
        try {
            appDataService.createTemplates(version, req, resp);
        } catch (Exception e) {
            log.error("下载模板失败", e);
        }
    }

}
