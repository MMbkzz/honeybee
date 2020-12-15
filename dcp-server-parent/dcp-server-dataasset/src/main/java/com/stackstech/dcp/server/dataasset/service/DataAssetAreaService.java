package com.stackstech.dcp.server.dataasset.service;

import com.stackstech.dcp.server.dataasset.model.DataAssetArea;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 数据资产领域
 */
public interface DataAssetAreaService {

    /**
     * 资产领域详情查询
     *
     * @param dataAssetArea
     * @return
     */
    List<DataAssetArea> queryAll(DataAssetArea dataAssetArea) throws Exception;

    /**
     * 资产领域详情查询
     *
     * @param id
     * @return
     */
    DataAssetArea query(String id) throws Exception;

    /**
     * 新增数据资产领域
     *
     * @param dataAssetArea
     * @return
     */
    int add(DataAssetArea dataAssetArea, HttpServletRequest req) throws Exception;

    /**
     * 资产领域删除
     *
     * @param ids
     * @return
     */
    ResponseEntity<?> delete(List<String> ids) throws Exception;

    /**
     * 资产领域修改
     *
     * @param dataAssetArea
     * @return
     */
    int update(DataAssetArea dataAssetArea, HttpServletRequest req) throws Exception;

    /**
     * 获取记录数
     *
     * @param dataAssetArea
     * @return
     * @throws Exception
     */
    int countAll(DataAssetArea dataAssetArea) throws Exception;

    /**
     * 根据名称获取实体
     *
     * @param areaName
     * @return
     * @throws Exception
     */
    DataAssetArea queryByName(String areaName) throws Exception;
}
