package com.stackstech.dcp.server.platform.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.core.page.PageUtil;
import com.stackstech.dcp.server.platform.api.ApiUrls;
import com.stackstech.dcp.server.platform.model.ServiceDriver;
import com.stackstech.dcp.server.platform.service.ServiceDriverService;
import com.stackstech.dcp.server.platform.vo.ServiceDriverConfigVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 驱动管理Controller
 */
@RestController
@RequestMapping(ApiUrls.API_SERVICEDRIVER_URI)
public class ServiceDriverController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDriverController.class);

    @Autowired
    private ServiceDriverService driverService;


    /**
     * 获取driver参数列表
     *
     * @param serviceDriver
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICEDRIVER_QUERY_URI)
    public ResponseEntity<?> queryServiceDriver(ServiceDriver serviceDriver, Page page) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (page != null && page.getPageNo() != null) {
                PageUtil.page(page);
            }
            List<ServiceDriver> list = driverService.queryAll(serviceDriver);
            int count = driverService.countAll(serviceDriver);
            map.put("list", list);
            map.put("count", count);
        } catch (Exception e) {
            logger.error("获取服务驱动列表失败..", e);
            return ResponseError.create(500, "获取服务驱动列表失败");
        }
        return ResponseOk.create(map);
    }

    /**
     * 获取数据驱动
     *
     * @param id
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICEDRIVER_GET_URI)
    public ResponseEntity<?> getServiceDriver(String id) {
        if (StringUtils.isEmpty(id)) {
            return ResponseError.create(400, "请求参数为空");
        }
        ServiceDriverConfigVO serviceDriverConfigVO = null;
        try {
            serviceDriverConfigVO = driverService.query(id);
        } catch (Exception e) {
            logger.error("获取服务驱动失败..", e);
            return ResponseError.create(500, "获取服务驱动失败");
        }
        return ResponseOk.create(serviceDriverConfigVO);
    }

    /**
     * 新增数据驱动
     *
     * @param serviceDriverConfigVO
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICEDRIVER_ADD_URL)
    public ResponseEntity<?> addServiceDriver(HttpServletRequest req, ServiceDriverConfigVO serviceDriverConfigVO, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (serviceDriverConfigVO == null) {
            return ResponseError.create(400, "请求参数为空");
        }
        try {
            return driverService.insert(serviceDriverConfigVO, file, req);
        } catch (Exception e) {
            logger.error("新增数据驱动失败....", e);
            return ResponseError.create(500, "新增服务驱动失败");
        }
    }

    /**
     * 更新数据驱动
     *
     * @param serviceDriverConfigVO
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICEDRIVER_UPDATE_URI)
    public ResponseEntity<?> editServiceDriver(HttpServletRequest req, ServiceDriverConfigVO serviceDriverConfigVO, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (serviceDriverConfigVO == null) {
            return ResponseError.create(400, "请求参数为空");
        }
        try {
            return driverService.update(serviceDriverConfigVO, file, req);
        } catch (Exception e) {
            logger.error("更新服务驱动失败....", e);
            return ResponseError.create(500, "更新服务驱动失败");
        }
    }

    /**
     * 删除数据驱动
     *
     * @param ids
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICEDRIVER_DELETE_URI)
    public ResponseEntity<?> delServiceDriver(@RequestBody List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return ResponseError.create(400, "请求参数为空");
        }
        try {
            return driverService.delete(ids);
        } catch (Exception e) {
            logger.error("删除服务驱动失败....", e);
            return ResponseError.create(500, "删除服务驱动失败");
        }
    }

    /**
     * 校验数据驱动
     *
     * @param driverName
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICEDRIVER_CHECK_URI)
    public ResponseEntity<?> checkServiceDriver(String driverName) {
        try {
            ServiceDriver serviceDriver = driverService.queryByName(driverName);
            if (serviceDriver != null) {
                return ResponseError.create(500, "服务驱动已经存在!");
            }
        } catch (Exception e) {
            logger.error("服务驱动校验失败..", e);
            return ResponseError.create(500, "服务驱动校验失败");
        }
        return ResponseOk.create("服务驱动不存在!");
    }


    /**
     * 数据驱动(启用禁用)
     *
     * @param serviceDriver
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICEDRIVER_STATUS_URI)
    public ResponseEntity<?> changeDriverStatus(ServiceDriver serviceDriver) {
        try {
            int i = driverService.updateDriverstatus(serviceDriver);
            if (i <= 0) {
                return ResponseError.create(500, "修改驱动状态失败!");
            }
        } catch (Exception e) {
            logger.error("修改驱动状态失败..", e);
            return ResponseError.create(500, "修改驱动状态失败");
        }
        return ResponseOk.create("修改驱动状态成功!");
    }


}
