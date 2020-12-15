package com.stackstech.dcp.server.platform.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.core.page.PageUtil;
import com.stackstech.dcp.server.platform.api.ApiUrls;
import com.stackstech.dcp.server.platform.model.Message;
import com.stackstech.dcp.server.platform.service.MessageService;
import com.stackstech.dcp.server.platform.vo.MessageQueryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiUrls.API_MESSAGE_URI)
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    /**
     * 获取message参数列表
     *
     * @param message
     * @return
     */
    @GetMapping(ApiUrls.API_MESSAGE_QUERY_URI)
    public ResponseEntity<?> queryMessage(MessageQueryVO message, Page page, HttpServletRequest req) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (page != null && page.getPageNo() != null) {
                PageUtil.page(page);
            }
            List<Message> list = messageService.queryAll(message, req);
            int count = messageService.countAll(message, req);
            map.put("list", list);
            map.put("count", count);
        } catch (Exception e) {
            logger.error("获取消息列表失败..", e);
            return ResponseError.create(500, "获取消息列表失败");
        }
        return ResponseOk.create(map);
    }

    /**
     * 更新Message消息
     *
     * @param message
     * @return
     */
    @PostMapping(ApiUrls.API_MESSAGE_EDIT_URI)
    public ResponseEntity<?> updateMessage(@RequestBody Message message, HttpServletRequest req) {
        try {
            int i = messageService.update(message, req);
            if (i <= 0) {
                return ResponseError.create("更新消息状态失败");
            }
        } catch (Exception e) {
            logger.error("更新消息状态失败..", e);
            return ResponseError.create(500, "更新消息状态失败");
        }
        return ResponseOk.create("更新消息成功");
    }


    /**
     * 删除Message消息
     *
     * @param ids
     * @return
     */
    @PostMapping(ApiUrls.API_MESSAGE_DEL_URI)
    public ResponseEntity<?> delMessage(@RequestBody List<Map<String, Object>> ids) {
        try {
            int i = messageService.deletes(ids);
            if (i <= 0) {
                return ResponseError.create("删除消息失败");
            }
        } catch (Exception e) {
            logger.error("删除消息失败..", e);
            return ResponseError.create(500, "删除消息失败");
        }
        return ResponseOk.create("删除消息成功");
    }

    /**
     * 获取预警消息
     *
     * @return
     */
    @GetMapping(ApiUrls.API_MESSAGE_QUERY_WARN_URI)
    public ResponseEntity<?> getWarnCount(HttpServletRequest request) {
        int i = 0;
        try {
            i = messageService.queryWarnCount(request);
        } catch (Exception e) {
            logger.error("获取预警数量失败..", e);
            return ResponseError.create(500, "获取预警数量失败");
        }
        return ResponseOk.create(i);
    }

    /**
     * 获取公告消息
     *
     * @param request
     * @return
     */
    @GetMapping(ApiUrls.API_MESSAGE_QUERY_NOTICE_URI)
    public ResponseEntity<?> getNotice(HttpServletRequest request, Page page) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (page != null && page.getPageNo() != null) {
                PageUtil.page(page);
            }
            List<Message> list = messageService.queryNotice(request);
            int count = messageService.countNotice(request);
            map.put("list", list);
            map.put("count", count);
        } catch (Exception e) {
            logger.error("获取公告列表失败..", e);
            return ResponseError.create(500, "获取公告列表失败");
        }
        return ResponseOk.create(map);
    }


}
