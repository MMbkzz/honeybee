package com.stackstech.honeybee.server.dataservice.service;

import com.stackstech.honeybee.server.dataservice.model.DataService;
import com.stackstech.honeybee.server.dataservice.vo.AppVerifyVO;
import com.stackstech.honeybee.server.dataservice.vo.DataServiceVO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 数据APP数据服务接口定义
 */
public interface AppDataService {

    /**
     * APP数据服务详情查询
     *
     * @param queryVO
     * @return
     */
    List<DataService> queryAll(Map<String, Object> queryVO) throws Exception;

    /**
     * APP数据服务详情查询
     *
     * @param id
     * @return
     */
    DataServiceVO query(String id, String userId) throws Exception;

    /**
     * APP数据服务删除
     *
     * @param id
     * @return
     */
    int delete(String id) throws Exception;

    /**
     * APP数据服务修改
     *
     * @param dataService
     * @return
     */
    int update(DataService dataService) throws Exception;


    /**
     * 根据名称获取DataService
     *
     * @param dataServiceName
     * @return
     * @throws Exception
     */
    DataService queryByName(String dataServiceName) throws Exception;

    /**
     * 服务授权
     *
     * @param map
     * @return
     * @throws Exception
     */
    boolean auth(Map<String, Object> map) throws Exception;

    /**
     * 获取服务列表
     *
     * @return
     * @throws Exception
     */
    List<DataService> queryDataservices(String typeCode, String appId) throws Exception;

    /**
     * 服务验证
     *
     * @param appVerify
     * @return
     * @throws Exception
     */
    ResponseEntity<?> verify(AppVerifyVO appVerify) throws Exception;

    /**
     * 创建模板
     *
     * @param params
     * @param req
     * @return
     * @throws Exception
     */
    void createTemplates(String params, HttpServletRequest req, HttpServletResponse resp) throws Exception;

    Map<String, Object> queryAllByQuery(Map<String, Object> map) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;
}
