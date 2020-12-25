package com.stackstech.honeybee.server.datasource.service;

import com.stackstech.honeybee.server.datasource.model.ServiceSource;
import com.stackstech.honeybee.server.datasource.vo.ServiceSourceQueryVO;
import com.stackstech.honeybee.server.datasource.vo.ServiceSourceVO;
import com.stackstech.honeybee.server.platform.model.Instance;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface ServiceSourceService {

    Map<String, Object> queryAll(ServiceSourceQueryVO queryVO, int i, int parseInt) throws Exception;

    ServiceSourceVO query(String id) throws Exception;

    ServiceSource queryByName(String serviceSourceName) throws Exception;

    ResponseEntity<?> insert(ServiceSourceVO serviceSourceVO, HttpServletRequest req) throws Exception;

    ResponseEntity<?> update(ServiceSourceVO serviceSourceVO, HttpServletRequest req) throws Exception;

    ResponseEntity<?> changeStatus(ServiceSource serviceSource, HttpServletRequest req) throws Exception;

    ResponseEntity<?> delete(String id) throws Exception;

    ResponseEntity<?> getConnectionStatus(ServiceSourceVO serviceSourceVO) throws Exception;

    int countAll(ServiceSourceQueryVO queryVO) throws Exception;

    List<String> getTables(String id, String name) throws Exception;

    List<Map<String, Object>> getTableFields(String id, String tableName) throws Exception;

    ResponseEntity<?> parseSQL(String sql) throws Exception;

    ResponseEntity<?> executeSQL(Map<String, Object> params) throws Exception;

    ResponseEntity<?> getResultFromSQL(String id, String sql);

    List<ServiceSource> querySources(ServiceSource serviceSource);

    /**
     * 获取可用数据源
     *
     * @return
     */
    List<ServiceSource> getEnable();

    /**
     * 获取禁用数据源
     *
     * @return
     */
    List<ServiceSource> getDisable();


    /**
     * 数据源是否负载均衡
     *
     * @param serviceSource
     * @param instances
     * @return
     */
    boolean isBalanced(ServiceSource serviceSource, List<Instance> instances);


    /**
     * 数据源是否已上线
     *
     * @param serviceSource
     * @return
     */
    boolean isOnline(ServiceSource serviceSource);

    /**
     * 数据源是否已下线
     *
     * @param serviceSource
     * @return
     */
    boolean isOffline(ServiceSource serviceSource);

    /**
     * 更新数据源阶段
     *
     * @param id
     * @param stageCode
     */
    void updateStage(String id, String stageCode);
}
