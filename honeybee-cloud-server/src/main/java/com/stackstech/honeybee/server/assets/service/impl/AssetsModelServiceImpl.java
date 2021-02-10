package com.stackstech.honeybee.server.assets.service.impl;

import com.stackstech.honeybee.common.entity.JsonParameterList;
import com.stackstech.honeybee.server.assets.dao.AssetsModelMapper;
import com.stackstech.honeybee.server.assets.entity.AssetsModelEntity;
import com.stackstech.honeybee.common.entity.DataSourceMeta;
import com.stackstech.honeybee.server.core.service.BaseDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssetsModelServiceImpl implements BaseDataService<AssetsModelEntity> {

    @Autowired
    private AssetsModelMapper mapper;

    private void setDatasourceMeta(AssetsModelEntity entity) {
        String expression = entity.getAssetsModelVo().getExpression();
        if (StringUtils.isNotEmpty(expression)) {
            //TODO SQL解析
            JsonParameterList metas = new JsonParameterList();
            metas.add(new DataSourceMeta("id", "number", "$id", "desc..."));
            metas.add(new DataSourceMeta("name", "varchar", "$name", null));
            metas.add(new DataSourceMeta("age", "number", "$age", null));
            metas.add(new DataSourceMeta("gender", "number", "$gender", "a desc.."));
            entity.setDatasourceMeta(metas);
        }
    }

    @Override
    public boolean add(AssetsModelEntity entity) {
        setDatasourceMeta(entity);
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(AssetsModelEntity entity) {
        setDatasourceMeta(entity);
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        //TODO insert to data recycler
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public AssetsModelEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<AssetsModelEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }

}
