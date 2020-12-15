package com.stackstech.dcp.server.dataservice.service.impl;

import com.stackstech.dcp.server.dataservice.dao.AppDsFieldMapper;
import com.stackstech.dcp.server.dataservice.model.AppDsField;
import com.stackstech.dcp.server.dataservice.service.AppDsFieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据服务APP授权字段业务逻辑类
 */
@Service
//@Transactional(value = "transactionManager")
public class AppDsFieldServiceImpl implements AppDsFieldService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppDsFieldMapper appDsFieldMapper;

    /**
     * 新增数据服务APP授权字段
     *
     * @param appDsField
     * @return
     */
    @Override
    public int add(AppDsField appDsField) {
        List<AppDsField> getAppDsField = appDsFieldMapper.queryAll(appDsField);
        int i = 0;
        if (getAppDsField.size() > 0) {
            log.info("数据服务APP授权字段名字重复" + getAppDsField.get(0).getDataServiceId());
        } else {
            i = appDsFieldMapper.insert(appDsField);
        }

        return i;
    }

    /**
     * 数据服务APP授权字段详情查询
     *
     * @param appDsField,page
     * @return
     */
    @Override
    public List<AppDsField> queryAll(AppDsField appDsField) {
        // 获取数据服务APP授权字段信息
        List<AppDsField> areaList = appDsFieldMapper.queryAll(appDsField);
        return areaList;
    }

    /**
     * 数据服务APP授权字段详情查询
     *
     * @param id
     * @return
     */
    @Override
    public AppDsField query(Long id) {
        // 获取数据服务APP授权字段信息
        AppDsField area = appDsFieldMapper.queryByPrimaryKey(id);
        return area;
    }

    /**
     * 数据服务APP授权字段详情查询
     *
     * @param id
     * @return
     */
    @Override
    public int delete(Long id) {
        // 获取数据服务APP授权字段信息
        int i = appDsFieldMapper.delete(id);
        return i;
    }

    /**
     * 数据服务APP授权字段修改
     *
     * @param appDsField
     * @return
     */
    @Override
    public int update(AppDsField appDsField) {
        // 获取数据服务APP授权字段信息
        int status = appDsFieldMapper.update(appDsField);
        return status;
    }
}
