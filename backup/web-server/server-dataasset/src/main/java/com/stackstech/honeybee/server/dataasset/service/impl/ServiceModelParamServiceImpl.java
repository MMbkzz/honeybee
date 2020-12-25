package com.stackstech.honeybee.server.dataasset.service.impl;

import com.stackstech.honeybee.core.http.ResponseError;
import com.stackstech.honeybee.core.http.ResponseOk;
import com.stackstech.honeybee.server.dataasset.dao.ServiceModelParamMapper;
import com.stackstech.honeybee.server.dataasset.model.ServiceModelParam;
import com.stackstech.honeybee.server.dataasset.service.ServiceModelParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据模型参数业务逻辑类
 */
@Service
@Transactional
public class ServiceModelParamServiceImpl implements ServiceModelParamService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ServiceModelParamMapper serviceModelParamMapper;

    /**
     * 新增数据模型参数
     *
     * @param serviceModelParam
     * @return
     */
    @Override
    public int add(ServiceModelParam serviceModelParam) {
        ServiceModelParam ModelParam = serviceModelParamMapper.queryByPrimaryKey(serviceModelParam.getId());
        if (ModelParam != null) {
            log.info("模型参数名字重复" + ModelParam.getParamName());
        }
        int i = serviceModelParamMapper.insert(serviceModelParam);
        return i;
    }

    /**
     * 模型参数详情查询
     *
     * @param serviceModelParam
     * @return
     */
    @Override
    public List<ServiceModelParam> queryAll(ServiceModelParam serviceModelParam) {
        // 获取模型参数信息
        List<ServiceModelParam> areaList = serviceModelParamMapper.queryAll(serviceModelParam);
        return areaList;
    }

    /**
     * 模型参数详情查询
     *
     * @param id
     * @return
     */
    @Override
    public ServiceModelParam query(Long id) {
        // 获取模型参数信息
        ServiceModelParam area = serviceModelParamMapper.queryByPrimaryKey(id);
        return area;
    }

    /**
     * 模型参数详情查询
     *
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<?> delete(Long id) {
        // 获取模型参数信息
        int i = serviceModelParamMapper.delete(id);
        if (i > 0) {
            return ResponseOk.create("删除模型参数成功");
        } else {
            return ResponseError.create(500, "删除模型参数失败");
        }
    }

    /**
     * 模型参数修改
     *
     * @param serviceModelParam
     * @return
     */
    @Override
    public int update(ServiceModelParam serviceModelParam) {
        // 获取模型参数信息
        int status = serviceModelParamMapper.update(serviceModelParam);
        return status;
    }
}
