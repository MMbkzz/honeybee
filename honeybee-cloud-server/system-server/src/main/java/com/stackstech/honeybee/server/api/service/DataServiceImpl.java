package com.stackstech.honeybee.server.api.service;

import com.stackstech.honeybee.server.api.entity.DataServiceEntity;
import com.stackstech.honeybee.server.core.vo.DataServiceVo;
import com.stackstech.honeybee.server.api.dao.DataServiceMapper;
import com.stackstech.honeybee.server.core.service.DataService;
import com.stackstech.honeybee.server.core.utils.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * DataServiceImpl
 *
 * @author william
 */
@Service
public class DataServiceImpl implements DataService<DataServiceVo, DataServiceEntity> {

    @Autowired
    private DataServiceMapper mapper;

    @Override
    public boolean add(DataServiceVo vo, Long ownerId) {
        DataServiceEntity entity = new DataServiceEntity().build(ownerId);
        BeanUtils.copyProperties(vo, entity);
        //TODO
        entity.setServiceMeta(CommonUtil.toJsonString(vo.getDataServiceParameters()));
        entity.setDataServiceCode(CommonUtil.generateEntityCode());
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(DataServiceVo vo, Long ownerId) {
        DataServiceEntity entity = new DataServiceEntity().update(ownerId);
        BeanUtils.copyProperties(vo, entity);
        //TODO
        entity.setServiceMeta(CommonUtil.toJsonString(vo.getDataServiceParameters()));
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        return mapper.deleteByPrimaryKey(recordId, ownerId) > 0;
    }

    @Override
    public DataServiceEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<DataServiceEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }


}
