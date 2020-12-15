package com.stackstech.dcp.server.auth.api;

/**
 * 资源类别（模块） API URI list:
 * 命名规范： /实体名复数/动作名_分割符
 */
public class ApiUrls {
    /**
     * root level URI
     */
    public static final String API_AUTH_URI = "/api/auth";

    /**
     * sso单点
     */
    public static final String API_SSO_LOGIN_URI = "/sso";

    public static final String API_SSO_USER_GET_URI = "/get_ldap_user";

    public static final String API_AUTH_OSP_USER_GET_URI = "/get_osp_user";

    /**
     * 登入
     */
    public static final String API_AUTH_LOGIN_URI = "/login";
    /**
     * 登入
     */
    public static final String API_AUTH_LOGIN_ERROR_URI = "/error";
    /**
     * 登出
     */
    public static final String API_AUTH_LOGOUT_URI = "/logout";

    /**
     * 用户模块
     */
    public static final String API_AUTH_USER_URI = "/api/auth/user";
    /**
     * 修改密码
     */
    public static final String API_AUTH_USER_PWD_EDIT_URI = "/edit_pwd";
    /**
     * 新增用户
     */
    public static final String API_AUTH_USER_ADD_URI = "/add_user";
    /**
     * 删除用户
     */
    public static final String API_AUTH_USER_DEL_URI = "/del_user";
    /**
     * 编辑用户
     */
    public static final String API_AUTH_USER_EDIT_URI = "/edit_user";
    /**
     * 用户详情查询
     */
    public static final String API_AUTH_USER_GET_USERINFO_URI = "/get_user_info";
    /**
     * 查询操作人上级ID
     */
    public static final String API_AUTH_USER_LEADER_URI = "/get_leader";
    /**
     * 用户启用停用接口
     */
    public static final String API_AUTH_USER_SSU_URI = "/start_stop_user";
    /**
     * 配置用户角色接口
     */
    public static final String API_AUTH_USER_DEPLOY_URI = "/deploy_user_role";
    /**
     * 判断该用户下是否有对应的权限资源
     */
    public static final String API_AUTH_USER_EXIST_RESOURCE_URI = "/exists_resource";
    /**
     * 用户查询列表接口
     */
    public static final String API_AUTH_USER_GET_URI = "/get_users";

    /**
     * 角色模块
     */
    public static final String API_AUTH_ROLE_URI = "/api/auth/role";
    /**
     * 新增角色接口
     */
    public static final String API_AUTH_ROLE_ADD_URI = "/add_role";
    /**
     * 删除角色接口
     */
    public static final String API_AUTH_ROLE_DEL_URI = "/del_role";
    /**
     * 编辑角色接口
     */
    public static final String API_AUTH_ROLE_EDIT_URI = "/edit_role";
    /**
     * 角色启用停用接口
     */
    public static final String API_AUTH_ROLE_SSR_URI = "/start_stop_role";
    /**
     * 角色查询列表接口
     */
    public static final String API_AUTH_ROLE_GET_URI = "/get_page_roles";
    /**
     * 查询所有角色
     */
    public static final String API_AUTH_ROLE_ALL_URI = "/get_all_role";
    /**
     * 用户角色查询
     */
    public static final String API_AUTH_USER_ROLE_URI = "/get_user_role";
    /**
     * 角色详情查询
     */
    public static final String API_AUTH_ROLE_GET_USERINFO_URI = "/get_role_info";
    /**
     * 校验角色是否存在
     */
    public static final String API_AUTH_ROLE_VERIFY_EXISTS_URI = "/verify_role_exists";
    /**
     * 构建资源操作 tree 结构
     */
    public static final String API_AUTH_ROLE_BUILD_RESOURCE_OPERATIONS_URI = "/build_resource_operations";
    /**
     * 新增角色与权限
     */
    public static final String API_AUTH_ROLE_ADD_ROLE_PERMISSION_URI = "/add_role_permission";
    /**
     * 编辑角色与权限
     */
    public static final String API_AUTH_ROLE_EDIT_ROLE_PERMISSION_URI = "/edit_role_permission";
    /**
     * 获取角色下会员列表
     */
    public static final String API_AUTH_ROLE_GET_ROLE_USER_URI = "/get_role_user";

    /**
     * 类别模块
     */
    public static final String API_AUTH_CATEGORY_URI = "/api/auth/category";
    /**
     * 新增资源类别
     */
    public static final String API_AUTH_CATEGORY_ADD_URI = "/add_category";
    /**
     * 删除资源类别
     */
    public static final String API_AUTH_CATEGORY_DEL_URI = "/del_category";
    /**
     * 更新资源类别
     */
    public static final String API_AUTH_CATEGORY_UPDATE_URI = "/update_category";
    /**
     * 获取资源类别
     */
    public static final String API_AUTH_CATEGORY_GET_URI = "/get_categorys";
    /**
     * 获取资源类别 tree
     */
    public static final String API_AUTH_CATEGORY_TREE_URI = "/get_categorys_tree";

    /**
     * 资源模块
     */
    public static final String API_AUTH_RESOURCE_URI = "/api/auth/resource";
    /**
     * 新增资源
     */
    public static final String API_AUTH_RESOURCE_ADD_URI = "/add";
    /**
     * 更新资源
     */
    public static final String API_AUTH_RESOURCE_UPDATE_URI = "/update";
    /**
     * 启用资源
     */
    public static final String API_AUTH_RESOURCE_ENABLE_URI = "/enable";
    /**
     * 禁用资源
     */
    public static final String API_AUTH_RESOURCE_DISABLE_URI = "/disable";
    /**
     * 删除资源
     */
    public static final String API_AUTH_RESOURCE_DEL_URI = "/delete";
    /**
     * 获取资源树
     */
    public static final String API_AUTH_RESOURCE_GET_URI = "/get";
    /**
     * 校验资源编码
     */
    public static final String API_AUTH_RESOURCE_CODE_CHECK_URI = "/codecheck";

    /**
     * 操作模块
     */
    public static final String API_AUTH_OPERATION_URI = "/api/auth/operation";
    /**
     * 新增动作
     */
    public static final String API_AUTH_OPERATION_ADD_URI = "/add_operation";
    /**
     * 删除动作
     */
    public static final String API_AUTH_OPERATION_DEL_URI = "/del_operation";
    /**
     * 修改动作
     */
    public static final String API_AUTH_OPERATION_UPDATE_URI = "/update_operation";
    /**
     * 获取动作
     */
    public static final String API_AUTH_OPERATION_GET_URI = "/get_operations";
    /**
     * 检查用户名
     */
    public static final String API_AUTH_USERS_NAME_CHECK_URI = "/name-unique";

    /**
     * 检查登陆名
     */
    public static final String API_AUTH_USERS_LOGINNAME_CHECK_URI = "/loginname-unique";
    /**
     * 检查邮箱
     */
    public static final String API_AUTH_USERS_EMAIL_CHECK_URI = "/email-unique";

    /**
     * 检查手机
     */
    public static final String API_AUTH_USERS_PHONE_CHECK_URI = "/phone-unique";
}
