package com.stackstech.dcp.server.dataservice.service.impl;

import com.stackstech.dcp.server.dataservice.dao.AppDsMapper;
import com.stackstech.dcp.server.dataservice.model.AppDs;
import com.stackstech.dcp.server.dataservice.service.AppDsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据服务APP授权业务逻辑类
 */
@Service
//@Transactional(value = "transactionManager")
public class AppDsServiceImpl implements AppDsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppDsMapper appDsMapper;

    /**
     * 新增数据服务APP授权
     *
     * @param appDs
     * @return
     */
    @Override
    public int add(AppDs appDs) {
        List<AppDs> getAppDs = appDsMapper.queryAll(appDs);
        int i = 0;
        if (getAppDs.size() > 0) {
            log.info("数据服务APP授权名字重复" + getAppDs.get(0).getDataServiceId());
        } else {
            i = appDsMapper.insert(appDs);
        }

        return i;
    }

    /**
     * 数据服务APP授权详情查询
     *
     * @param appDs,page
     * @return
     */
    @Override
    public List<AppDs> queryAll(AppDs appDs) {
        // 获取数据服务APP授权信息
        List<AppDs> areaList = appDsMapper.queryAll(appDs);
        return areaList;
    }

    /**
     * 数据服务APP授权详情查询
     *
     * @param id
     * @return
     */
    @Override
    public AppDs query(Long id) {
        // 获取数据服务APP授权信息
        AppDs area = appDsMapper.queryByPrimaryKey(id);
        return area;
    }

    /**
     * 数据服务APP授权详情查询
     *
     * @param id
     * @return
     */
    @Override
    public int delete(Long id) {
        // 获取数据服务APP授权信息
        int i = appDsMapper.delete(id);
        return i;
    }

    /**
     * 数据服务APP授权修改
     *
     * @param appDs
     * @return
     */
    @Override
    public int update(AppDs appDs) {
        // 获取数据服务APP授权信息
        int status = appDsMapper.update(appDs);
        return status;
    }
}
