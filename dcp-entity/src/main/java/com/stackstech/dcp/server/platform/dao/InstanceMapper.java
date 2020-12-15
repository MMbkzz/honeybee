package com.stackstech.dcp.server.platform.dao;

import com.stackstech.dcp.server.platform.model.Instance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface InstanceMapper {

    /**
     * 获取集群实例数量
     *
     * @param instance
     * @return
     */
    int countAll(Instance instance);


    /**
     * 查询
     *
     * @param instance 实例对象
     * @return
     */
    List<Instance> queryAll(Instance instance);

    /**
     * 新增
     *
     * @param instance 实例对象
     * @return
     */
    int insert(Instance instance);

    /**
     * 更新
     *
     * @param instance 实例对象
     */
    int update(Instance instance);

    /**
     * 删除
     *
     * @param id 主键
     */
    int delete(@Param("id") String id);

    /**
     * 根据状态、阶段查询
     *
     * @param status 状态
     * @param stage  阶段
     * @return
     */
    List<Instance> queryByStatus(@Param("status") String[] status, @Param("stage") String[] stage);

    /**
     * 重名校验
     *
     * @param name
     * @return
     */
    Instance queryByName(@Param("name") String name);

    Instance queryByPrimaryKey(@Param("id") String id);

    /**
     * 按主机查询实例
     *
     * @param host ip
     * @param port 端口
     * @return
     */
    Instance queryByHost(@Param("id") String id, @Param("host") String host, @Param("port") String port);

    /**
     * 根据实例资源表的数据源ID 查询 对应的实例列表
     *
     * @param serviceSourceId
     * @return
     */
    List<Instance> queryByServiceSourceId(@Param("serviceSourceId") String serviceSourceId);

    int countByCondition(@Param("queryString") String queryString);

    List<Instance> queryByCondition(@Param("queryString") String queryString);
}
