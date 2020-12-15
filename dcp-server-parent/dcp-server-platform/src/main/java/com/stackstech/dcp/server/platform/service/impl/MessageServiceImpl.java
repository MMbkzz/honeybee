package com.stackstech.dcp.server.platform.service.impl;

import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.core.util.CommonUtils;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import com.stackstech.dcp.server.platform.dao.MessageMapper;
import com.stackstech.dcp.server.platform.dao.MessageObjectMapper;
import com.stackstech.dcp.server.platform.model.Message;
import com.stackstech.dcp.server.platform.model.MessageObject;
import com.stackstech.dcp.server.platform.service.MessageService;
import com.stackstech.dcp.server.platform.vo.MessageQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息中心Service
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private MessageObjectMapper messageObjectMapper;
    @Autowired
    private LoginUserManager loginUserManager;

    /**
     * 模糊查询
     * queryString & messageLevel
     *
     * @param message
     * @return
     */
    @Override
    public List<Message> queryAll(MessageQueryVO message, HttpServletRequest req) {
        Map<String, Object> map = CommonUtils.elementToMap(message);
        LoginUserProtos.LoginUser loginUser = getLoginUser(req);
        if (loginUser != null) {
            MessageObject object = new MessageObject();
            object.setUserId(loginUser.getUserId());
            List<MessageObject> messageObjects = messageObjectMapper.queryAll(object);
            if (messageObjects != null && messageObjects.size() > 0) {
                map.put("userId", loginUser.getUserId());
            }
        }
        List<Message> messages = messageMapper.queryAll(map);
        return messages;
    }

    /**
     * 获取预警数量
     *
     * @param request
     * @return
     */
    @Override
    public int queryWarnCount(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        LoginUserProtos.LoginUser loginUser = getLoginUser(request);
        if (loginUser != null) {
            MessageObject object = new MessageObject();
            object.setUserId(loginUser.getUserId());
            List<MessageObject> messageObjects = messageObjectMapper.queryAll(object);
            if (messageObjects != null && messageObjects.size() > 0) {
                map.put("userId", loginUser.getUserId());
            }
        }
        return messageMapper.queryWarnCount(map);
    }

    /**
     * 获取公告列表
     *
     * @param request
     * @return
     */
    @Override
    public List<Message> queryNotice(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        LoginUserProtos.LoginUser loginUser = getLoginUser(request);
        if (loginUser != null) {
            MessageObject object = new MessageObject();
            object.setUserId(loginUser.getUserId());
            List<MessageObject> messageObjects = messageObjectMapper.queryAll(object);
            if (messageObjects != null && messageObjects.size() > 0) {
                map.put("userId", loginUser.getUserId());
            }
        }
        return messageMapper.queryNotice(map);
    }

    /**
     * 获取预警列表
     *
     * @param request
     * @return
     */
    @Override
    public int countNotice(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        LoginUserProtos.LoginUser loginUser = getLoginUser(request);
        if (loginUser != null) {
            MessageObject object = new MessageObject();
            object.setUserId(loginUser.getUserId());
            List<MessageObject> messageObjects = messageObjectMapper.queryAll(object);
            if (messageObjects != null && messageObjects.size() > 0) {
                map.put("userId", loginUser.getUserId());
            }
        }
        return messageMapper.countNotice(map);
    }

    /**
     * 更新消息 <修改object对象></>
     *
     * @param message
     * @param req
     * @return
     */
    @Override
    public int update(Message message, HttpServletRequest req) {
        LoginUserProtos.LoginUser loginUser = this.getLoginUser(req);
        if (loginUser != null) {
            message.setUpdateBy(loginUser.getUserId());
        }
        MessageObject messageObject = new MessageObject();
        messageObject.setId(message.getObjectId());
        if (messageObjectMapper.queryByPrimaryKey(message.getObjectId()) != null) {
            messageObject.setStatusCode(message.getStatusCode());
            messageObject.setUpdateBy(message.getUpdateBy());
            return messageObjectMapper.update(messageObject);
        } else {
            messageObject.setStatusCode(message.getStatusCode());
            messageObject.setCreateBy(message.getUpdateBy());
            return messageObjectMapper.insert(messageObject);
        }


    }

    /**
     * 批量删除消息
     *
     * @param ids
     * @return
     */
    @Override
    public int deletes(List<Map<String, Object>> ids) {
        int i = messageMapper.deletes(ids);
        if (i > 0) {
            i = messageObjectMapper.deletes(ids);
        }
        return i;
    }

    /**
     * 获取消息数量
     *
     * @param message
     * @return
     */
    @Override
    public int countAll(MessageQueryVO message, HttpServletRequest request) {
        Map<String, Object> map = CommonUtils.elementToMap(message);
        LoginUserProtos.LoginUser loginUser = getLoginUser(request);
        if (loginUser != null) {
            MessageObject object = new MessageObject();
            object.setUserId(loginUser.getUserId());
            List<MessageObject> messageObjects = messageObjectMapper.queryAll(object);
            if (messageObjects != null && messageObjects.size() > 0) {
                map.put("userId", loginUser.getUserId());
            }
        }
        return messageMapper.countAll(map);
    }

    /**
     * 获取登录用户
     *
     * @param req
     * @return
     */
    private LoginUserProtos.LoginUser getLoginUser(HttpServletRequest req) {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            return loginUserManager.getLoginUser(token);
        }
        return null;
    }

}
