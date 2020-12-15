package com.stackstech.dcp.server.dataasset.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import com.stackstech.dcp.server.dataasset.api.ApiUrls;
import com.stackstech.dcp.server.dataasset.model.ModelCode;
import com.stackstech.dcp.server.dataasset.service.ModelCodeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 模型Code Controller类
 */
@RestController
@RequestMapping(ApiUrls.API_SERVICE_MODEL_URI)
public class ModelCodeController {

    private static final Logger logger = LoggerFactory.getLogger(ModelCodeController.class);

    @Autowired
    private ModelCodeService modelCodeService;

    @Autowired
    private LoginUserManager loginUserManager;

    /**
     * 获取modelCode
     *
     * @return
     */
    @GetMapping(ApiUrls.API_MODELCODE_QUERY_URI)
    public ResponseEntity<?> queryModelCodes(String parentCode, HttpServletRequest req) {

        try {
            if (StringUtils.isEmpty(parentCode)) {
                parentCode = "";
            }
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(TokenUtil.getToken(req));
            List<ModelCode> list = modelCodeService.queryAll(parentCode);
            return ResponseOk.create(list);
        } catch (Exception e) {
            logger.error("获取模型类型失败!!", e);
            return ResponseError.create(500, "获取模型类型失败");
        }
    }

    /**
     * 获取快码表状态码
     *
     * @param type
     * @return
     */
    @GetMapping(ApiUrls.API_MODELCODE_QUERY_STATUS_URI)
    public ResponseEntity<?> queryCodeStatus(String type) {
        try {
            if (StringUtils.isNotEmpty(type)) {
                List<ModelCode> list = modelCodeService.queryStatus(type);
                return ResponseOk.create(list);
            } else {
                return ResponseError.create(400, "请求参数为空");
            }
        } catch (Exception e) {
            logger.error("获取状态码失败!!", e);
            return ResponseError.create(500, "获取状态码失败");
        }
    }

}
