package com.stackstech.dcp.server.auth.service;

import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.server.auth.model.AuthUser;
import com.stackstech.dcp.server.auth.model.vo.MenuVo;
import com.stackstech.dcp.server.auth.model.vo.UserVo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户操作业务逻辑类
 */
@Service
public interface UserService {
    /**
     * 新增用户
     *
     * @param userVo
     * @return
     */
    ResponseEntity<?> addUser(UserVo userVo, Long userId);

    /**
     * 启用停用用户
     *
     * @param id
     * @param status
     * @param userId
     * @return
     */
    int startStopUser(String id, String status, long userId);

    /**
     * 用户关联角色
     *
     * @param id
     * @param rolesId
     * @return
     */
    int deployUserRole(String id, String[] rolesId);

    /**
     * 获取所有用户
     *
     * @param userVo
     * @return
     */
    Map<String, Object> getPageUsers(UserVo userVo, Page page);

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    UserVo getUserInfo(Long userId);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    int delUser(Long id);

    /**
     * 编辑用户
     *
     * @param userVo
     * @return
     */
    ResponseEntity<?> editUser(UserVo userVo, Long userId);

    /**
     * 修改密码
     *
     * @param userVo
     */
    void editPwd(UserVo userVo, Long userId);

    /**
     * 根据用户名查找
     *
     * @param name
     * @param id
     * @return
     */
    AuthUser selectByName(String name, Long id);

    /**
     * 根据登陆用户名查找
     *
     * @param name
     * @return
     */
    AuthUser selectByLoginName(String name);

    /**
     * 根据email查找
     *
     * @param name
     * @param id
     * @return
     */
    AuthUser selectByEmail(String name, Long id);

    AuthUser selectByPhone(String phone, Long id);


    /**
     * 根据用户 id 获取该用户的菜单树
     *
     * @param userId
     * @return
     */
    List<MenuVo> getMenuTreeByUserId(Long userId);

    /**
     * 获取角色下会员列表
     *
     * @param roleCode 角色编号
     * @return
     */
    List<Map<String, Object>> getRoleUserList(String roleCode);

    ResponseEntity<?> addUserOrUpdate(UserVo userVo, long userId);
}
