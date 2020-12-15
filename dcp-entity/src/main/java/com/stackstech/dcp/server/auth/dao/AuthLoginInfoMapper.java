package com.stackstech.dcp.server.auth.dao;

import com.stackstech.dcp.server.auth.model.LoginInfo;
import com.stackstech.dcp.server.auth.model.vo.LoginInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface AuthLoginInfoMapper {

    /**
     * 保存日志信息
     *
     * @param loginInfo
     */
    void insertLoginInfo(LoginInfo loginInfo);

    /**
     * 多规则查询登陆日志信息
     *
     * @param loginInfo
     * @return
     */
    List<LoginInfo> getLoginInfo(LoginInfoVo loginInfo);

    void deleteByUserId(long userId);

}
