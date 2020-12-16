package com.stackstech.honeybee.server.datasource.web;

import com.stackstech.honeybee.core.http.ResponseError;
import com.stackstech.honeybee.core.http.ResponseOk;
import com.stackstech.honeybee.server.datasource.api.ApiUrls;
import com.stackstech.honeybee.server.datasource.model.ServiceSource;
import com.stackstech.honeybee.server.datasource.service.ServiceSourceService;
import com.stackstech.honeybee.server.datasource.vo.ServiceSourceQueryVO;
import com.stackstech.honeybee.server.datasource.vo.ServiceSourceVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 数据源Controller
 */
@RestController
@RequestMapping(ApiUrls.API_SERVICESOURCE_URI)
public class ServiceSourceController {

    private final Logger logger = LoggerFactory.getLogger(ServiceSourceController.class);

    @Autowired
    private ServiceSourceService sourceService;

    /**
     * 模型获取数据源列表
     *
     * @param serviceSource
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICESOURCE_QUERY_ALL_URI)
    public ResponseEntity<?> queryServiceSource(ServiceSource serviceSource) {
        try {
            List<ServiceSource> list = sourceService.querySources(serviceSource);
            return ResponseOk.create(list);
        } catch (Exception e) {
            logger.error("获取数据源列表失败..", e);
            return ResponseError.create(500, "获取数据源列表失败");
        }
    }

    /**
     * 获取数据源列表
     *
     * @param queryVO
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICESOURCE_QUERY_URI)
    public ResponseEntity<?> queryServicesource(ServiceSourceQueryVO queryVO, HttpServletRequest req) {
        try {
            String pageNo = req.getParameter("pageNo");
            String pageSize = req.getParameter("pageSize");
            if (StringUtils.isNotEmpty(pageNo) && StringUtils.isNotEmpty(pageSize)) {

                Map<String, Object> map = sourceService.queryAll(queryVO, Integer.parseInt(pageNo), Integer.parseInt(pageSize));

                return ResponseOk.create(map);
            }

        } catch (Exception e) {
            logger.error("获取数据源列表失败..", e);
            return ResponseError.create(500, "获取数据源列表失败");
        }
        return ResponseError.create(500, "获取数据源列表失败");
    }

    /**
     * 获取数据源
     * <根据Id查看详情></>
     *
     * @param id
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICESOURCE_GET_URI)
    public ResponseEntity<?> getServicesource(String id) {
        ServiceSourceVO dsConfigVO = null;
        try {
            dsConfigVO = sourceService.query(id);
        } catch (Exception e) {
            logger.error("获取数据源失败..", e);
            return ResponseError.create(500, "获取数据源失败");
        }
        return ResponseOk.create(dsConfigVO);
    }

    /**
     * 校验数据源
     * <新增校验重名></>
     *
     * @param serviceSourceName
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICESOURCE_CHECK_URI)
    public ResponseEntity<?> checkServicesource(String serviceSourceName) {
        try {
            ServiceSource serviceSource = sourceService.queryByName(serviceSourceName);
            if (serviceSource != null) {
                return ResponseError.create(500, "数据源已经存在!");
            }
        } catch (Exception e) {
            logger.error("数据源校验失败..", e);
            return ResponseError.create(500, "数据源校验失败");
        }
        return ResponseOk.create("数据源不存在!");
    }


    /**
     * 新增数据源
     *
     * @param serviceSourceVO
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICESOURCE_ADD_URI)
    public ResponseEntity<?> addServicesource(@RequestBody ServiceSourceVO serviceSourceVO, HttpServletRequest req) {
        if (serviceSourceVO == null) {
            return ResponseError.create(400, "请求参数为空");
        }
        try {
            return sourceService.insert(serviceSourceVO, req);
        } catch (Exception e) {
            logger.error("新增数据源失败....", e);
            return ResponseError.create(500, "新增数据源失败");
        }
    }


    /**
     * 更新数据源
     *
     * @param serviceSourceVO
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICESOURCE_UPDATE_URI)
    public ResponseEntity<?> editServicesource(@RequestBody ServiceSourceVO serviceSourceVO, HttpServletRequest req) {
        if (serviceSourceVO == null) {
            return ResponseError.create(400, "请求参数为空");
        }
        try {
            return sourceService.update(serviceSourceVO, req);
        } catch (Exception e) {
            logger.error("更新数据源失败....", e);
            return ResponseError.create(500, "更新数据源失败");
        }
    }

    /**
     * 更改数据源状态
     *
     * @param serviceSource
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICESOURCE_STATUS_URI)
    public ResponseEntity<?> changeServicesource(@RequestBody ServiceSource serviceSource, HttpServletRequest req) {
        try {
            return sourceService.changeStatus(serviceSource, req);
        } catch (Exception e) {
            logger.error("数据源状态更改失败....", e);
            return ResponseError.create(500, "数据源状态更改失败");
        }
    }

    /**
     * 删除数据源
     *
     * @param map
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICESOURCE_DELETE_URI)
    public ResponseEntity<?> delServicesource(@RequestBody Map<String, Object> map) {
        try {
            String id = (String) map.get("id");
            if (StringUtils.isNotEmpty(id)) {
                return sourceService.delete(id);
            } else {
                return ResponseError.create(400, "请求参数为空");
            }
        } catch (Exception e) {
            logger.error("删除数据源失败....", e);
            return ResponseError.create(500, "删除数据源失败");
        }
    }

    /**
     * 获取Connection连接状态
     *
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICESOURCE_CONNECTION_URI)
    public ResponseEntity<?> getServicesourceConn(@RequestBody ServiceSourceVO serviceSourceVO) {
        if (serviceSourceVO == null) {
            return ResponseError.create(400, "请求参数为空");
        }
        try {
            return sourceService.getConnectionStatus(serviceSourceVO);
        } catch (Exception e) {
            logger.error("数据源连接失败....", e);
            return ResponseError.create(500, "数据源连接失败");
        }
    }

    /**
     * 获取数据表列表
     *
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICESOURCE_TABLE_URI)
    public ResponseEntity<?> getServicesourceTables(String id, @RequestParam(required = false) String name) {
        if (StringUtils.isEmpty(id)) {
            return ResponseError.create(400, "请求参数为空");
        }
        List<String> list = null;
        try {
            list = sourceService.getTables(id, name);
        } catch (Exception e) {
            logger.error("获取数据表失败....", e);
            return ResponseError.create(500, "获取数据表列表失败");
        }
        return ResponseOk.create(list);
    }


    /**
     * 获取数据字段列表
     *
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICESOURCE_FIELD_URI)
    public ResponseEntity<?> getServicesourceFields(String id, String tableName) {
        if (StringUtils.isEmpty(id)) {
            return ResponseError.create(400, "请求参数为空");
        }
        List<Map<String, Object>> list = null;
        try {
            list = sourceService.getTableFields(id, tableName);
        } catch (Exception e) {
            logger.error("获取数据表字段失败....", e);
            return ResponseError.create(500, "获取数据表字段失败");
        }
        return ResponseOk.create(list);
    }


    /**
     * 执行SQL
     *
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICESOURCE_SQL_EXECUTE_URI)
    public ResponseEntity<?> getServicesourceSQL(@RequestBody Map<String, Object> params) {
        if (params.isEmpty()) {
            return ResponseError.create(400, "请求参数为空");
        }
        try {
            return sourceService.executeSQL(params);
        } catch (Exception e) {
            logger.error("模型验证失败....", e);
            return ResponseError.create(500, "模型验证失败");
        }
    }


    /**
     * 解析SQL语句
     *
     * @param params
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICESOURCE_SQL_PARSE_URI)
    public ResponseEntity<?> parseServicesourceSQL(@RequestBody Map<String, Object> params) {
        if (params.isEmpty() || params.get("sql") == null) {
            return ResponseError.create(400, "请求参数为空");
        }
        try {
            String sql = (String) params.get("sql");
            return sourceService.parseSQL(sql);
        } catch (Exception e) {
            logger.error("表达式解析错误....", e);
            return ResponseError.create(500, "表达式解析错误");
        }
    }

}
