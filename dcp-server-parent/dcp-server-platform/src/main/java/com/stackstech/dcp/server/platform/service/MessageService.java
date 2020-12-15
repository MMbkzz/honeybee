package com.stackstech.dcp.server.platform.service;

import com.stackstech.dcp.server.platform.model.Message;
import com.stackstech.dcp.server.platform.vo.MessageQueryVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface MessageService {
    List<Message> queryAll(MessageQueryVO message, HttpServletRequest req);

    int deletes(List<Map<String, Object>> ids);

    int countAll(MessageQueryVO message, HttpServletRequest req);

    int queryWarnCount(HttpServletRequest request);

    List<Message> queryNotice(HttpServletRequest request);

    int countNotice(HttpServletRequest request);

    int update(Message message, HttpServletRequest req);

}
