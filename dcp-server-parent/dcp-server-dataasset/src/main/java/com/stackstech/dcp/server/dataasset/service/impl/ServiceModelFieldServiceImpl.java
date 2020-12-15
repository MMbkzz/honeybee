package com.stackstech.dcp.server.dataasset.service.impl;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.server.dataasset.dao.ServiceModelFieldMapper;
import com.stackstech.dcp.server.dataasset.model.ServiceModelField;
import com.stackstech.dcp.server.dataasset.service.ServiceModelFieldService;
import com.stackstech.dcp.server.dataservice.dao.AppDsFieldMapper;
import com.stackstech.dcp.server.dataservice.model.AppDsField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据模型字段业务逻辑类
 */
@Service
@Transactional
public class ServiceModelFieldServiceImpl implements ServiceModelFieldService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ServiceModelFieldMapper serviceModelFieldMapper;

    @Autowired
    private AppDsFieldMapper appDsFieldMapper;

    /**
     * 新增数据模型字段
     *
     * @param serviceModelField
     * @return
     */
    @Override
    public int add(ServiceModelField serviceModelField) {
        ServiceModelField modelField = serviceModelFieldMapper.queryByPrimaryKey(serviceModelField.getId());
        if (modelField != null) {
            log.info("模型字段名字重复" + modelField.getFieldName());
        }
        int i = serviceModelFieldMapper.insert(serviceModelField);
        return i;
    }

    /**
     * 模型字段详情查询
     *
     * @param serviceModelField
     * @return
     */
    @Override
    public List<ServiceModelField> queryAll(ServiceModelField serviceModelField) {
        // 获取模型字段信息
        List<ServiceModelField> areaList = serviceModelFieldMapper.queryAll(serviceModelField);
        return areaList;
    }

    /**
     * 模型字段详情查询
     *
     * @param id
     * @return
     */
    @Override
    public ServiceModelField query(Long id) {
        // 获取模型字段信息
        ServiceModelField area = serviceModelFieldMapper.queryByPrimaryKey(id);
        return area;
    }

    /**
     * 模型字段详情查询
     *
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<?> delete(Long id) {
        // 校验服务授权字段
        AppDsField appDsField = new AppDsField();
        appDsField.setFieldId(String.valueOf(id));
        log.info("==删除授权字段id==" + id);
        List<AppDsField> appDsFields = appDsFieldMapper.queryAll(appDsField);
        log.info("==删除授权字段list==" + appDsFields);
        if (appDsFields != null && appDsFields.size() > 0) {
            return ResponseError.create(500, "该字段已被服务使用,不能进行删除操作");
        }

        int i = serviceModelFieldMapper.delete(id);
        if (i > 0) {
            return ResponseOk.create("删除模型字段成功");
        } else {
            return ResponseError.create(500, "删除模型字段失败");
        }
    }

    /**
     * 模型字段修改
     *
     * @param serviceModelField
     * @return
     */
    @Override
    public int update(ServiceModelField serviceModelField) {
        // 获取模型字段信息
        int status = serviceModelFieldMapper.update(serviceModelField);
        return status;
    }
}
