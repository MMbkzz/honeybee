package com.stackstech.dcp.server.dataasset.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.core.page.PageUtil;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import com.stackstech.dcp.server.dataasset.api.ApiUrls;
import com.stackstech.dcp.server.dataasset.model.DataAssetTopic;
import com.stackstech.dcp.server.dataasset.service.DataAssetTopicService;
import com.stackstech.dcp.server.dataasset.vo.DataAssetTopicQueryVO;
import com.stackstech.dcp.server.dataasset.vo.DataAssetTopicVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据资产主题
 */
@RestController
@RequestMapping(ApiUrls.API_DATAASSET_TOPIC_URI)
public class AssetTopicController {

    private final Logger log = LoggerFactory.getLogger(AssetTopicController.class);

    @Autowired
    private DataAssetTopicService dataAssetTopicService;

    @Autowired
    private LoginUserManager loginUserManager;

    /**
     * 新增资产主题接口
     *
     * @param dataAssetTopic
     * @return
     */
    @PostMapping(value = ApiUrls.API_DATAASSET_ADD_TOPIC_URI)
    public ResponseEntity<?> addAssetTopic(@RequestBody DataAssetTopic dataAssetTopic, HttpServletRequest req) {
        try {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(TokenUtil.getToken(req));
            if (loginUser != null) {
                int i = dataAssetTopicService.add(dataAssetTopic, loginUser.getUserId());
                if (i > 0) {
                    return ResponseOk.create("新增数据资产主题成功");
                }
            }
        } catch (Exception e) {
            log.error("新增数据资产主题失败", e);
            return ResponseError.create(500, "新增数据资产主题失败");
        }
        return ResponseError.create(500, "新增数据资产主题失败");
    }

    /**
     * 资产主题查询列表接口
     *
     * @param queryVO
     * @param page
     * @return
     */
    @GetMapping(value = ApiUrls.API_DATAASSET_QUERY_TOPIC_URI)
    public ResponseEntity<?> queryAssetTopics(DataAssetTopicQueryVO queryVO, Page page, HttpServletRequest req) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            if (page != null && page.getPageNo() != null) {
                PageUtil.page(page);
            }
            if (TokenUtil.getToken(req) != null) {
                LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(TokenUtil.getToken(req));
                String userId = loginUser.getRoleCodes().size() == 1 && loginUser.getRoleCodes().contains("data_engineer") ? String.valueOf(loginUser.getUserId()) : "";
                queryVO.setUserId(userId);
            }
            List<Map<String, Object>> result = dataAssetTopicService.queryAll(queryVO);
            int count = dataAssetTopicService.countAll(queryVO);
            map.put("list", result);
            map.put("count", count);
            return ResponseOk.create(map);
        } catch (Exception e) {
            log.error("获取资产主题列表失败", e);
            return ResponseError.create("获取资产主题列表失败");
        }
    }

    /**
     * 查询数据资产主题详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = ApiUrls.API_DATAASSET_GET_TOPIC_URI)
    public ResponseEntity<?> getAssetTopic(@RequestParam String id) {
        try {
            DataAssetTopicVO assetTopicVO = dataAssetTopicService.query(id);
            return ResponseOk.create(assetTopicVO);
        } catch (Exception e) {
            log.error("获取资产主题失败", e);
            return ResponseError.create("获取资产主题失败");
        }
    }

    /**
     * 编辑资产主题
     *
     * @param dataAssetTopic
     * @return
     */
    @PostMapping(value = ApiUrls.API_DATAASSET_EDIT_TOPIC_URI)
    public ResponseEntity<?> editAssetTopic(@RequestBody DataAssetTopic dataAssetTopic, HttpServletRequest req) {
        try {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(TokenUtil.getToken(req));
            int i = dataAssetTopicService.update(dataAssetTopic, loginUser.getUserId());
            if (i > 0) {
                return ResponseOk.create("更新资产主题成功");
            }
        } catch (Exception e) {
            log.error("更新资产主题失败", e);
            return ResponseError.create("更新资产主题失败");
        }
        return ResponseError.create("更新资产主题失败");
    }

    /**
     * 删除资产主题
     *
     * @param ids
     * @return
     */
    @PostMapping(value = ApiUrls.API_DATAASSET_DEL_TOPIC_URI)
    public ResponseEntity<?> delAssetTopic(@RequestBody List<String> ids) {
        try {
            return dataAssetTopicService.delete(ids);
        } catch (Exception e) {
            log.error("删除资产主题失败", e);
            return ResponseError.create("删除资产主题失败");
        }
    }

    /**
     * 重名校验
     *
     * @param topicName
     * @return
     */
    @GetMapping(ApiUrls.API_DATAASSET_CHECK_TOPIC_URI)
    public ResponseEntity checkTopic(String topicName) {
        try {
            DataAssetTopic assetTopic = dataAssetTopicService.queryByName(topicName);
            if (assetTopic != null) {
                return ResponseError.create(500, "资产主题已经存在!");
            }
        } catch (Exception e) {
            log.error("资产主题校验失败..", e);
            return ResponseError.create(500, "资产主题校验失败");
        }
        return ResponseOk.create("资产主题不存在!");
    }

}
