package com.stackstech.honeybee.server.dataasset.service;

import com.stackstech.honeybee.server.dataasset.model.ServiceModel;
import com.stackstech.honeybee.server.dataasset.vo.ServiceModelQueryVO;
import com.stackstech.honeybee.server.dataasset.vo.ServiceModelVO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 数据资产模型
 */
public interface ServiceModelService {

    /**
     * 服务模型详情查询
     *
     * @param queryVO
     * @return
     */
    List<Map<String, Object>> queryAll(ServiceModelQueryVO queryVO) throws Exception;

    /**
     * 服务模型详情查询
     *
     * @param id
     * @return
     */
    ServiceModelVO query(String id) throws Exception;

    ResponseEntity<?> addBatch(List<ServiceModelVO> serviceModelVOS, HttpServletRequest request) throws Exception;

    /**
     * 新增数据资产模型
     *
     * @param serviceModelVO
     * @return
     */
    ResponseEntity<?> add(ServiceModelVO serviceModelVO, HttpServletRequest req) throws Exception;

    /**
     * 资产模型删除
     *
     * @param id
     * @return
     */
    ResponseEntity<?> delete(String id) throws Exception;

    /**
     * 资产模型修改
     *
     * @param serviceModelVO
     * @return
     */
    ResponseEntity<?> update(ServiceModelVO serviceModelVO, HttpServletRequest req) throws Exception;


    int countAll(ServiceModelQueryVO queryVO) throws Exception;

    /**
     * 重名校验
     *
     * @param modelName
     * @return
     */
    ServiceModel queryByName(String modelName) throws Exception;

    List<ServiceModel> queryAllParent();
}
