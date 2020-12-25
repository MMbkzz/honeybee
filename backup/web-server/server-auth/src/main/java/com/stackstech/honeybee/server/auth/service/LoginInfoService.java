package com.stackstech.honeybee.server.auth.service;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.core.page.IProcessPage;
import com.stackstech.honeybee.core.page.Page;
import com.stackstech.honeybee.core.page.PageUtil;
import com.stackstech.honeybee.core.util.SnowFlake;
import com.stackstech.honeybee.server.auth.dao.AuthLoginInfoMapper;
import com.stackstech.honeybee.server.auth.dao.AuthRoleMapper;
import com.stackstech.honeybee.server.auth.model.AuthRole;
import com.stackstech.honeybee.server.auth.model.LoginInfo;
import com.stackstech.honeybee.server.auth.model.vo.LoginInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登陆日志服务层
 */
@Service
public class LoginInfoService {

    @Autowired
    private AuthLoginInfoMapper authLoginInfoMapper;
    @Autowired
    private AuthRoleMapper authRoleMapper;
    @Autowired
    private SnowFlake snowflake;

    @Autowired
    private IProcessPage iProcessPage;

    /**
     * 登陆日志
     *
     * @param userId     用户ID
     * @param loginHost  登陆IP
     * @param loginAgent 浏览器信息
     * @param token      票据信息
     * @param expiretime 过期时间
     *                   //     * @param roles        用户角色
     */
    public void insertLoginInfo(long userId, String loginName, String loginHost, String loginAgent, String token, int
            expiretime) {
        LoginInfo loginInfo = new LoginInfo();
        //主键
        loginInfo.setId(snowflake.next());
        loginInfo.setUserId(userId);
        loginInfo.setUserAgent(loginAgent);
        loginInfo.setLoginName(loginName);
        loginInfo.setAccessToken(token);
        loginInfo.setTokenExpire(expiretime);
        loginInfo.setLoginDate(new Date());
        loginInfo.setLoginIp(loginHost);
        //用户角色信息
        List<AuthRole> roles = authRoleMapper.getRoles(userId);
        //用","拼接用户角色信息
        if (roles.size() == 1) {
            loginInfo.setRoleNames(roles.get(0).getName());
        } else {
            StringBuffer roleName = new StringBuffer();
            for (int i = 0; i < roles.size(); i++) {
                roleName.append(roles.get(0).getName());
                if (i != roles.size() - 1) {
                    roleName.append(",");
                }
            }
            //设置角色信息
            loginInfo.setRoleNames(roleName.toString());
        }
        authLoginInfoMapper.insertLoginInfo(loginInfo);
    }

    /**
     * 获取登陆日志信息（包含分页）
     * //     * @param 查询条件
     *
     * @return
     * @parma 分页参数
     */
    public Map<String, Object> getLoginInfoList(LoginInfoVo loginInfoVo, Page page) throws Exception {
        Map<String, Object> data = Maps.newHashMap();
        //分页信息
        // 分页
        if (null == page.getPageNo() && null == page.getPageSize()) {
            page.setPageNo(1);
            page.setPageSize(10);
        }
        PageUtil.page(page);
        List<LoginInfo> loginInfos = authLoginInfoMapper.getLoginInfo(loginInfoVo);
        return iProcessPage.process(loginInfos);
    }

    public List<LoginInfo> getLoginInfoList(long userId) {
        LoginInfoVo loginInfoVo = new LoginInfoVo();
        loginInfoVo.setUserId(userId);
        List<LoginInfo> loginInfos = authLoginInfoMapper.getLoginInfo(loginInfoVo);
        return loginInfos;

    }
}
