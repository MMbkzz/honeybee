package com.stackstech.dcp.server.auth.service.impl;

import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.page.IProcessPage;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.core.page.PageUtil;
import com.stackstech.dcp.core.util.SnowFlake;
import com.stackstech.dcp.server.auth.dao.AuthRoleMapper;
import com.stackstech.dcp.server.auth.dao.AuthUserMapper;
import com.stackstech.dcp.server.auth.dao.AuthUserRoleMapper;
import com.stackstech.dcp.server.auth.dao.UserRoleInsertListDao;
import com.stackstech.dcp.server.auth.model.AuthRole;
import com.stackstech.dcp.server.auth.model.AuthUser;
import com.stackstech.dcp.server.auth.model.AuthUserRole;
import com.stackstech.dcp.server.auth.model.vo.MenuVo;
import com.stackstech.dcp.server.auth.model.vo.UserVo;
import com.stackstech.dcp.server.auth.service.UserService;
import com.stackstech.dcp.server.auth.tree.BuildMenuTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户操作业务逻辑类
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthUserMapper authUserMapper;
    @Autowired
    private AuthUserRoleMapper authUserRoleMapper;
    @Autowired
    private AuthRoleMapper authRoleMapper;
    @Autowired
    private UserRoleInsertListDao userRoleInsertListDao;

    @Autowired
    private SnowFlake snowflake;
    @Autowired
    private IProcessPage iProcessPage;

    /**
     * 新增用户
     *
     * @param userVo
     * @return
     */
    @Override
    public ResponseEntity<?> addUser(UserVo userVo, Long userId) {
        userVo.setCreateTime(new Date());
        userVo.setCreateBy(userId);
        authUserMapper.insert(userVo);

        //这里缺少用户与角色的关系补上
        AuthUserRole userRole = new AuthUserRole();
        userRole.setUserId(userVo.getId());
        userRole.setCreateTime(new Date());
        String[] roles = userVo.getRoles().split(",");
        for (String roleId : roles) {
            userRole.setRoleId(Long.parseLong(roleId));
            userRole.setCreateTime(new Date());
            userRole.setCreateBy(userId);
            authUserRoleMapper.insert(userRole);
        }
        return ResponseOk.create("result", "OK");
    }

    /**
     * 用户启用停用
     *
     * @param id
     * @param status
     * @param userId
     * @return
     */
    @Override
    public int startStopUser(String id, String status, long userId) {
        return authUserMapper.startStopUser(id, status);
    }

    /**
     * 配置用户角色
     *
     * @param id
     * @param rolesId
     * @return
     */
    @Override
    public int deployUserRole(String id, String[] rolesId) {
        if (rolesId == null || rolesId.length == 0) {
            return 1;
        }
        // 删除用户编号下的角色关系
        authUserRoleMapper.deleteByUserId(Long.parseLong(id));
        List<AuthUserRole> userRoles = new ArrayList<AuthUserRole>();
        Date date = new Date();
        AuthUserRole userRole;
        for (String role : rolesId) {
            userRole = new AuthUserRole();
            userRole.setRoleId(Long.parseLong(role));
            userRole.setUserId(Long.parseLong(id));
            userRole.setCreateTime(date);
            userRoles.add(userRole);
        }
        // 新增用户角色关系
        userRoleInsertListDao.insertList(userRoles);
        return 1;
    }

    /**
     * 用户查询列表
     *
     * @param userVo
     * @param page
     * @return
     */
    @Override
    public Map<String, Object> getPageUsers(UserVo userVo, Page page) {
        // 分页
        if (null == page.getPageNo() && null == page.getPageSize()) {
            page.setPageNo(1);
            page.setPageSize(10);
        }
        PageUtil.page(page);
        List<UserVo> users = authUserMapper.getPageUsers(userVo);
        for (UserVo vo : users) {
            List<AuthRole> roles = authRoleMapper.getRoleByUser(vo.getId());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < roles.size(); i++) {
                sb.append(roles.get(i).getId() + ",");        //append String并不拥有该方法，所以借助StringBuffer
            }
            vo.setRoles(sb.toString().length() > 0 ? sb.toString().substring(0, sb.toString().length() - 1) : "");
            if (null != roles && roles.size() > 0) {
                vo.setRoleList(roles);
            }
        }
        return iProcessPage.process(users);
    }

    /**
     * 用户详情查询
     *
     * @param userId
     * @return
     */
    @Override
    public UserVo getUserInfo(Long userId) {
        // 获取用户信息
        UserVo users = authUserMapper.getUserInfo(userId);
        // 获取用户下角色信息
        List<AuthRole> roles = authRoleMapper.getRoles(userId);
        users.setRoleList(roles);
        return users;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delUser(Long id) {
        // 删除用户表
        authUserMapper.deleteByPrimaryKey(id);
        // 删除用户角色关系表
        authUserRoleMapper.deleteByUserId(id);
        return 1;
    }

    /**
     * 编辑用户
     *
     * @param userVo
     * @return
     */
    @Override
    public ResponseEntity<?> editUser(UserVo userVo, Long userId) {
        userVo.setUpdateTime(new Date());
        userVo.setUpdateBy(userId);
        // 修改用户
        authUserMapper.updateByUserId(userVo);

        Date date = new Date();

//		for (Organization organization : organizations) {
//			orgUser = new OrgUser();
//			orgUser.setOrgId(organization.getId());
//			orgUser.setUserId(userId);
//			orgUser.setCreateTime(date);
//			orgUsers.add(orgUser);
//		}

        // 删除用户角色关系
        authUserRoleMapper.deleteByUserId(userVo.getId());
        List<AuthUserRole> roleByUsers = authUserRoleMapper.selectByUserId(userVo.getId());
        for (AuthUserRole userRole : roleByUsers) {

        }
        //这里缺少用户与角色的关系补上
        AuthUserRole userRole = new AuthUserRole();
        userRole.setUserId(userVo.getId());
        userRole.setCreateTime(new Date());
        String[] roles = userVo.getRoles().split(",");
        for (String roleId : roles) {
            userRole.setRoleId(Long.parseLong(roleId));
            userRole.setUpdateBy(userId);
            userRole.setUpdateTime(new Date());
            userRole.setCreateTime(new Date());
            userRole.setCreateBy(userId);
            authUserRoleMapper.insert(userRole);
        }
        return ResponseOk.create("result", "OK");
    }

    /**
     * 修改密码
     *
     * @param userVo
     */
    @Override
    public void editPwd(UserVo userVo, Long userId) {
        userVo.setUpdateBy(userId);
        userVo.setUpdateTime(new Date());
        authUserMapper.updatePassword(userVo);
    }

    @Override
    public AuthUser selectByName(String name, Long id) {
        return authUserMapper.selectByName(name, id);
    }

    @Override
    public AuthUser selectByLoginName(String name) {
        UserVo userVo = new UserVo();
        userVo.setLoginName(name);
        return authUserMapper.selectByLoginName(userVo);
    }

    @Override
    public AuthUser selectByEmail(String name, Long id) {
        UserVo userVo = new UserVo();
        userVo.setId(id);
        userVo.setEmail(name);
        return authUserMapper.selectByEmail(userVo);
    }

    @Override
    public AuthUser selectByPhone(String phone, Long id) {
        UserVo userVo = new UserVo();
        userVo.setId(id);
        userVo.setMobile(phone);
        return authUserMapper.selectByPhone(userVo);
    }

    @Override
    public List<MenuVo> getMenuTreeByUserId(Long userId) {
        Map<String, Object> param = new HashMap<String, Object>(0);
        param.put("userId", userId);
        //param.put("name", AuthCommon.AUTH_USER_MENU);
        List<MenuVo> menus = authUserMapper.getMenuByUserId(param);
        return new BuildMenuTree(menus).buildTree();
    }

    @Override
    public List<Map<String, Object>> getRoleUserList(String roleCode) {
        return authUserMapper.selectByRoleCode(roleCode);
    }

    @Override
    public ResponseEntity<?> addUserOrUpdate(UserVo userVo, long userId) {
        if (userVo.getId() == null || userVo.getId() == 0) {
            userVo.setId(snowflake.next());
            userVo.setCreateTime(new Date());
            userVo.setCreateBy(userId);
            authUserMapper.insert(userVo);
        } else {
            userVo.setUpdateTime(new Date());
            userVo.setUpdateBy(userId);
            // 修改用户
            authUserMapper.updateByUserId(userVo);
            // 删除用户角色关系
            authUserRoleMapper.deleteByUserId(userVo.getId());
        }

        //这里缺少用户与角色的关系补上
        AuthUserRole userRole = new AuthUserRole();
        userRole.setUserId(userVo.getId());
        userRole.setCreateTime(new Date());
        String[] roles = userVo.getRoles().split(",");
        for (String roleId : roles) {
            userRole.setRoleId(Long.parseLong(roleId));
            userRole.setCreateTime(new Date());
            userRole.setCreateBy(userId);
            authUserRoleMapper.insert(userRole);
        }
        return ResponseOk.create("result", "OK");
    }

}
