package com.stackstech.dcp.server.auth.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.server.auth.api.ApiUrls;
import com.stackstech.dcp.server.auth.model.AuthCategory;
import com.stackstech.dcp.server.auth.service.CategoryService;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 资源类别
 */
@RestController
@RequestMapping(ApiUrls.API_AUTH_CATEGORY_URI)
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LoginUserManager loginUserManager;

    /**
     * 新增资源类别
     *
     * @param category
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_CATEGORY_ADD_URI)
    public ResponseEntity<?> addCategory(@RequestBody AuthCategory category, HttpServletRequest req) {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                int check = categoryService.insert(category, loginUser.getUserId());
                if (check > 0) {
                    return ResponseOk.create("result", "OK");
                }
            }
        }
        return ResponseError.create(400, "添加失败");
    }

    /**
     * 删除资源类别
     *
     * @param category
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_CATEGORY_DEL_URI)
    public ResponseEntity<?> delCategory(@RequestBody AuthCategory category) {
        int check = categoryService.deleteByPrimaryKey(category);
        if (check > 0) {
            return ResponseOk.create("result", "OK");
        }
        return ResponseError.create(400, "删除失败");
    }

    /**
     * 更新资源类别
     *
     * @param category
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_CATEGORY_UPDATE_URI)
    public ResponseEntity<?> updateCategory(@RequestBody AuthCategory category, HttpServletRequest req) {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                int check = categoryService.updateByPrimaryKeySelective(category, loginUser.getUserId());
                if (check > 0) {
                    return ResponseOk.create("result", "OK");
                }
            }
        }
        return ResponseError.create(400, "修改失败");
    }

    /**
     * 获取资源类别
     *
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_CATEGORY_GET_URI)
    public ResponseEntity<?> getCategories(AuthCategory category, Page page) {
        return ResponseOk.create(categoryService.getCategories(category, page));
    }

    /**
     * 获取资源类别树
     *
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_CATEGORY_TREE_URI)
    public ResponseEntity<?> getCategoriesTree() {
        return ResponseOk.create(categoryService.getCategoriesTree());
    }

}
