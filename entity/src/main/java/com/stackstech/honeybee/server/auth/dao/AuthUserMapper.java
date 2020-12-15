package com.stackstech.honeybee.server.auth.dao;

import com.stackstech.honeybee.server.auth.model.AuthUser;
import com.stackstech.honeybee.server.auth.model.vo.MenuVo;
import com.stackstech.honeybee.server.auth.model.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 用户表Dao
 * <p>
 * 表名 AUTH_USER
 */
@Mapper
@Repository
public interface AuthUserMapper {
    /**
     * 新增用户
     *
     * @param userVo
     * @return
     */
    int insert(UserVo userVo);

    /**
     * 根据用户编号删除用户
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 编辑用户
     *
     * @param userVo
     * @return
     * @Title:updateByUserId
     */
    int updateByUserId(UserVo userVo);

    /**
     * 用户启用停用
     *
     * @param id
     * @param status
     * @return
     */
    int startStopUser(@Param("id") String id, @Param("status") String status);

    /**
     * 根据用户编号查询用户
     *
     * @param loginId
     * @return
     */
    AuthUser selectByloginId(Long loginId);

    /**
     * 根据组织编号查询用户
     *
     * @param OrgId
     * @return
     */
    List<AuthUser> selectByOrgId(Long OrgId);

    /**
     * 用户查询列表
     *
     * @param userVo
     * @return
     */
    List<UserVo> getPageUsers(UserVo userVo);

    /**
     * 用户详情查询
     *
     * @param userId
     * @return
     */
    UserVo getUserInfo(Long userId);

    /**
     * 更新登录时间
     *
     * @param userId
     * @return
     */
    int updateLoginDate(Long userId);

    /**
     * 修改密码
     *
     * @param userVo
     * @return
     */
    int updatePassword(UserVo userVo);


    AuthUser selectByLoginName(UserVo userVo);

    AuthUser selectByLdapName(UserVo userVo);

    AuthUser selectByEmail(UserVo userVo);

    AuthUser selectByName(@Param("name") String name, @Param("id") Long id);

    AuthUser selectByPhone(UserVo userVo);


    /**
     * 根据用户 id 获取该用户的菜单资源
     *
     * @param param
     * @return
     */
    List<MenuVo> getMenuByUserId(Map<String, Object> param);


    /**
     * 根据用户 id 获取该用户的菜单树
     *
     * @param userId
     * @return
     */
    List<MenuVo> getMenuTreeByUserId(Long userId);

    List<Map<String, Object>> selectByRoleCode(String roleCode);

}
