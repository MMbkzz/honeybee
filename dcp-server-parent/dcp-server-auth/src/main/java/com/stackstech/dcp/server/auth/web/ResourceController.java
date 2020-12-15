package com.stackstech.dcp.server.auth.web;

import com.google.common.collect.Maps;
import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.server.auth.api.ApiUrls;
import com.stackstech.dcp.server.auth.api.AuthCommon;
import com.stackstech.dcp.server.auth.model.AuthResource;
import com.stackstech.dcp.server.auth.model.vo.ResourceVo;
import com.stackstech.dcp.server.auth.service.ResourceService;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 资源树接口层
 */
@RestController
@RequestMapping(ApiUrls.API_AUTH_RESOURCE_URI)
public class ResourceController {
    final static Logger LOG = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private LoginUserManager loginUserManager;

    /**
     * 添加资源
     *
     * @param resourceVo
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_RESOURCE_ADD_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> addResource(@RequestBody AuthResource resourceVo, HttpServletRequest req) {
        try {
            if (StringUtils.isEmpty(resourceVo.getCode())) {
                return ResponseError.create(400, "code不能为空!");
            } else {
                if (resourceService.getResourceByCode(resourceVo.getCode(), 0L).size() > 0) {
                    return ResponseError.create(400, "code已经存在!");
                } else {
                    String token = TokenUtil.getToken(req);
                    if (token != null) {
                        LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
                        if (loginUser != null) {
                            int checkNum = resourceService.addResource(resourceVo, loginUser.getUserId());
                            if (checkNum > 0) {
                                return ResponseOk.create("result", "OK");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("NewResourceController.addResource error!", e);
        }
        return ResponseError.create(400, "添加失败");
    }

    /**
     * 删除资源
     *
     * @param resourceVo
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_RESOURCE_DEL_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteResource(@RequestBody AuthResource resourceVo) {
        try {
            // 1. 更新资源pid为-1L，状态为不可用
//            resourceVo.setId(resourceVo.getId());
//            resourceVo.setParent_id(-1L);
//            resourceVo.setStatus(AuthCommon.AUTH_RESOURCE_STATUS_DISABLE);
//            resourceService.updateResource(resourceVo);

            String result = resourceService.delResourcesById(resourceVo.getId());
            if (!"success".equals(result)) {
                return ResponseError.create(result);
            }
        } catch (Exception e) {
            LOG.error("NewResourceController.DeleteResource error!", e);
        }
        return ResponseOk.create("result", "OK");
    }

    /**
     * 更新资源
     *
     * @param resourceVo
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_RESOURCE_UPDATE_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> UpdateResource(@RequestBody AuthResource resourceVo, HttpServletRequest req) {
        try {
            if (StringUtils.isEmpty(resourceVo.getCode())) {
                return ResponseError.create(400, "code不能为空!");
            } else {
                List<AuthResource> authResources = resourceService.getResourceByCode(resourceVo.getCode(), 0L);
                if (authResources.size() > 0 && !authResources.get(0).getId().equals(resourceVo.getId())) {
                    return ResponseError.create(400, "code已经存在!");
                } else {
                    String token = TokenUtil.getToken(req);
                    if (token != null) {
                        LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
                        if (loginUser != null) {
                            int checkNum = resourceService.updateResource(resourceVo, loginUser.getUserId());
                            if (checkNum > 0) {
                                return ResponseOk.create("result", "OK");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("NewResourceController.updateResource error!", e);
        }
        return ResponseError.create(400, "修改失败!");
    }

    /**
     * 启用资源
     *
     * @param resourceVo
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_RESOURCE_ENABLE_URI)
    public ResponseEntity<?> ResourceEnable(@RequestBody AuthResource resourceVo, HttpServletRequest req) {
        try {
            String token = TokenUtil.getToken(req);
            if (token != null) {
                LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
                if (loginUser != null) {
                    resourceService.updateResourceStatus(resourceVo.getId(), AuthCommon.AUTH_RESOURCE_STATUS_ENABLE, loginUser.getUserId());
                    return ResponseOk.create("result", "OK");
                }
            }
        } catch (Exception e) {
            LOG.error("NewResourceController.ResourceEnable error!", e);
        }
        return ResponseError.create(400, "resource enable error");
    }

    /**
     * 禁用资源
     *
     * @param resourceVo
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_RESOURCE_DISABLE_URI)
    public ResponseEntity<?> ResourceDisable(@RequestBody AuthResource resourceVo, HttpServletRequest req) {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                resourceService.updateResourceStatus(resourceVo.getId(), AuthCommon.AUTH_RESOURCE_STATUS_DISABLE, loginUser.getUserId());
                return ResponseOk.create("result", "OK");
            }
        }
        return ResponseError.create(400, "禁用资源失败!");
    }

    /**
     * 检验资源code唯一
     *
     * @param resourceVo
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_RESOURCE_CODE_CHECK_URI)
    public ResponseEntity<?> checkResourceCode(AuthResource resourceVo) {
        Map<String, Boolean> result = Maps.newHashMap();
        if (StringUtils.isNotEmpty(resourceVo.getCode())) {
            List<AuthResource> authResources = resourceService.getResourceByCode(resourceVo.getCode(), resourceVo.getId() != null ? resourceVo.getId() : 0);
            result.put("result", authResources.size() > 0);
            return ResponseOk.create(result);
        } else {
            return ResponseError.create(400, "参数错误!");
        }
    }

    /**
     * 获取资源树
     *
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_RESOURCE_GET_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getResourceTree() {
        List<ResourceVo> resourceVoList = resourceService.getResourceTree();
        return ResponseOk.create(resourceVoList);
    }

}
