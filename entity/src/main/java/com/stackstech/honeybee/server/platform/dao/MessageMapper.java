package com.stackstech.honeybee.server.platform.dao;

import com.stackstech.honeybee.server.platform.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface MessageMapper {

    List<Message> queryAll(Map<String, Object> map);

    Message queryByPrimaryKey(@Param("id") Long id);

    int countAll(Map<String, Object> map);

    int queryWarnCount(Map<String, Object> map);

    List<Message> queryNotice(Map<String, Object> map);

    int countNotice(Map<String, Object> map);

    int insert(Message message);

    int update(Message message);

    int delete(@Param("id") Long id);

    int deletes(List<Map<String, Object>> ids);
}
