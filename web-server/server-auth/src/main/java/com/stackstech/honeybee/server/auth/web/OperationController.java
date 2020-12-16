package com.stackstech.honeybee.server.auth.web;

import com.stackstech.honeybee.core.http.ResponseError;
import com.stackstech.honeybee.core.http.ResponseOk;
import com.stackstech.honeybee.core.model.LoginUserProtos;
import com.stackstech.honeybee.core.page.Page;
import com.stackstech.honeybee.server.auth.api.ApiUrls;
import com.stackstech.honeybee.server.auth.model.AuthOperation;
import com.stackstech.honeybee.server.auth.service.OperationService;
import com.stackstech.honeybee.server.auth.utils.LoginUserManager;
import com.stackstech.honeybee.server.auth.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 动作
 */
@RestController
@RequestMapping(ApiUrls.API_AUTH_OPERATION_URI)
public class OperationController {
    private final Logger log = LoggerFactory.getLogger(OperationController.class);

    @Autowired
    private OperationService operationService;

    @Autowired
    private LoginUserManager loginUserManager;

    /**
     * 新增动作
     *
     * @param operation
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_OPERATION_ADD_URI)
    public ResponseEntity<?> addOperation(@RequestBody AuthOperation operation, HttpServletRequest req) {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                int check = operationService.insert(operation, loginUser.getUserId());
                if (check > 0) {
                    return ResponseOk.create("result", "OK");
                }
            }
        }
        return ResponseError.create(400, "添加失败");
    }

    /**
     * 删除动作
     *
     * @param operation
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_OPERATION_DEL_URI)
    public ResponseEntity<?> delOperation(@RequestBody AuthOperation operation) {
        int check = operationService.deleteByPrimaryKey(operation);
        if (check != 1) {
            return ResponseError.create(400, "删除失败");
        }
        return ResponseOk.create("result", "OK");
    }

    /**
     * 修改动作
     *
     * @param operation
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_OPERATION_UPDATE_URI)
    public ResponseEntity<?> updateOperation(@RequestBody AuthOperation operation, HttpServletRequest req) {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                int check = operationService.updateByPrimaryKeySelective(operation, loginUser.getUserId());
                if (check > 0) {
                    return ResponseOk.create("result", "OK");
                }
            }
        }
        return ResponseError.create(400, "修改失败");
    }

    /**
     * 获取动作
     *
     * @param operation
     * @param page
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_OPERATION_GET_URI)
    public ResponseEntity<?> getOperations(AuthOperation operation, Page page) {
        return ResponseOk.create(operationService.getOperations(operation, page));
    }

}
