package com.stackstech.dcp.server.platform.dao;

import com.stackstech.dcp.server.platform.model.MessageObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 消息对象Mapper
 */
@Mapper
@Repository
public interface MessageObjectMapper {

    List<MessageObject> queryAll(MessageObject messageObject);

    MessageObject queryByPrimaryKey(@Param("id") Long id);

    int countAll(MessageObject messageObject);

    int insert(MessageObject messageObject);

    int update(MessageObject messageObject);

    int delete(@Param("id") Long id);

    int deletes(List<Map<String, Object>> ids);
}
