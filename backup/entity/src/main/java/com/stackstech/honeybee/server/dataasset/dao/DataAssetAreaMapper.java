package com.stackstech.honeybee.server.dataasset.dao;

import com.stackstech.honeybee.server.dataasset.model.DataAssetArea;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据资产领域表Dao
 * <p>
 * 表名 dgp_data_model_area
 */
@Mapper
@Repository
public interface DataAssetAreaMapper {

    /**
     * 新增数据资产领域
     *
     * @param dataAssetArea
     * @return
     */
    int insert(DataAssetArea dataAssetArea);


    /**
     * 根据编号删除资产领域
     *
     * @param ids
     * @return
     */
    int delete(List<String> ids);

    /**
     * 编辑资产领域
     *
     * @param dataAssetArea
     * @return
     */
    int update(DataAssetArea dataAssetArea);

    /**
     * 资产领域查询列表
     *
     * @param dataAssetArea
     * @return
     */
    List<DataAssetArea> queryAll(DataAssetArea dataAssetArea);

    /**
     * 资产领域详情查询
     *
     * @param id
     * @return
     */
    DataAssetArea queryByPrimaryKey(@Param("id") String id);

    /**
     * 根据名称获取详情
     *
     * @param areaName
     * @return
     */
    DataAssetArea queryByName(@Param("areaName") String areaName);

    /**
     * 获取主键
     *
     * @return
     */
    String queryPrimaryKey();

    /**
     * 获取记录数
     *
     * @param dataAssetArea
     * @return
     */
    int countAll(DataAssetArea dataAssetArea);

    int countByCondition(@Param("queryString") String queryString);

    List<DataAssetArea> queryByCondition(@Param("queryString") String queryString);
}
