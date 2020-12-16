package com.stackstech.honeybee.server.dataservice.service;

import com.stackstech.honeybee.server.dataservice.model.AppUser;
import com.stackstech.honeybee.server.dataservice.vo.UserVO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 数据服务APP用户接口定义
 */
public interface AppUserService {

    /**
     * 数据服务APP用户查询
     *
     * @param appUser
     * @return
     */
    List<Map<String, Object>> queryAll(AppUser appUser) throws Exception;

    List<AppUser> queryUsers(String dataServiceId) throws Exception;

    AppUser queryByName(String name) throws Exception;

    /**
     * 数据服务APP用户详情查询
     *
     * @param id
     * @return
     */
    UserVO query(String id) throws Exception;

    /**
     * 新增数据服务APP用户
     *
     * @param appUser
     * @return
     */
    int add(AppUser appUser, HttpServletRequest req) throws Exception;

    /**
     * 数据服务APP用户删除
     *
     * @param id
     * @return
     */
    ResponseEntity<?> delete(String id) throws Exception;

    /**
     * 数据服务APP用户修改
     *
     * @param appUser
     * @return
     */
    int update(AppUser appUser, HttpServletRequest req) throws Exception;

    boolean auth(Map<String, Object> map) throws Exception;

    int countAll(AppUser appUser) throws Exception;

    int addOrUpdate(AppUser appUser, long userId);
}
