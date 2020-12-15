package com.stackstech.honeybee.server.platform.service.impl;


import com.stackstech.honeybee.core.conf.ApplicationConfig;
import com.stackstech.honeybee.core.enums.ParameterTypesEnum;
import com.stackstech.honeybee.core.http.ResponseError;
import com.stackstech.honeybee.core.http.ResponseOk;
import com.stackstech.honeybee.core.model.LoginUserProtos;
import com.stackstech.honeybee.core.util.FileUtil;
import com.stackstech.honeybee.server.auth.dao.AuthUserMapper;
import com.stackstech.honeybee.server.auth.model.AuthUser;
import com.stackstech.honeybee.server.auth.utils.LoginUserManager;
import com.stackstech.honeybee.server.auth.utils.TokenUtil;
import com.stackstech.honeybee.server.datasource.dao.ServiceSourceMapper;
import com.stackstech.honeybee.server.datasource.model.ServiceSource;
import com.stackstech.honeybee.server.param.dao.ParameterMapper;
import com.stackstech.honeybee.server.param.model.Parameter;
import com.stackstech.honeybee.server.platform.dao.ServiceDriverMapper;
import com.stackstech.honeybee.server.platform.model.ServiceDriver;
import com.stackstech.honeybee.server.platform.service.ServiceDriverService;
import com.stackstech.honeybee.server.platform.vo.ServiceDriverConfigVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 驱动Service类
 */
@Service
@Transactional
public class ServiceDriverServiceImpl implements ServiceDriverService {
    private final Logger logger = LoggerFactory.getLogger(ServiceDriverServiceImpl.class);


    @Autowired
    private ServiceDriverMapper serviceDriverMapper;

    @Autowired
    private ServiceSourceMapper serviceSourceMapper;

    @Autowired
    private ParameterMapper parameterMapper;

    @Autowired
    private AuthUserMapper authUserMapper;
    @Autowired
    private LoginUserManager loginUserManager;
    @Autowired
    private ApplicationConfig applicationConfig;

    /**
     * 获取数据驱动列表<数据源></>
     *
     * @param serviceDriver
     * @return
     * @throws Exception
     */
    @Override
    public List<ServiceDriver> queryAll(ServiceDriver serviceDriver) throws Exception {
        List<ServiceDriver> driverList = null;
        if (StringUtils.isNotBlank(serviceDriver.getQueryType())) {
            driverList = serviceDriverMapper.queryByCondition(serviceDriver.getQueryString());
        } else {
            driverList = serviceDriverMapper.queryAll(serviceDriver);
        }
        if (driverList != null && driverList.size() > 0) {
            for (ServiceDriver exsitDriver : driverList) {
                //获取更新用户
                this.queryUpdateUser(exsitDriver);
            }
        }
        return driverList;
    }

    /**
     * 获取数据驱动
     *
     * @param id
     * @return
     */
    @Override
    public ServiceDriverConfigVO query(String id) throws Exception {
        //query driver
        ServiceDriver serviceDriver = serviceDriverMapper.queryByPrimaryKey(id);
        if (serviceDriver != null) {
            //获取更新用户
            this.queryUpdateUser(serviceDriver);

            Parameter parameter = new Parameter();
            parameter.setObjectId(serviceDriver.getId());
            parameter.setObjectType(ParameterTypesEnum.DRIVER.toString());

            ServiceDriverConfigVO serviceDriverConfigVO = parseDriver2DriverconfigVO(serviceDriver, parameterMapper.queryAll(parameter));
            return serviceDriverConfigVO;
        }
        return null;
    }

    /**
     * 根据名称获取驱动
     *
     * @param driverName
     * @return
     */
    @Override
    public ServiceDriver queryByName(String driverName) throws Exception {
        return serviceDriverMapper.queryByName(driverName);
    }

    /**
     * 根据主键修改状态
     *
     * @return
     * @throws Exception
     */
    @Override
    public int updateDriverstatus(ServiceDriver serviceDriver) throws Exception {
        return serviceDriverMapper.update(serviceDriver);
    }

    /**
     * 新增驱动
     *
     * @param serviceDriverConfigVO
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<?> insert(ServiceDriverConfigVO serviceDriverConfigVO, MultipartFile file, HttpServletRequest req) throws Exception {
        LoginUserProtos.LoginUser loginUser = getLoginUser(req);
        if (loginUser != null) {
            serviceDriverConfigVO.setCreateBy(loginUser.getUserId());
            serviceDriverConfigVO.setUpdateBy(loginUser.getUserId());
        }

        ServiceDriver serviceDriver = parseDriverconfigVO2Driver(serviceDriverConfigVO);
        //upload file
        if (file != null) {
            long uploadTime = System.currentTimeMillis();

            String path = applicationConfig.getUpload() + serviceDriverConfigVO.getDriverName() + "/" + serviceDriverConfigVO.getVersion() + "/";
            String fileName = StringUtils.joinWith("-", serviceDriverConfigVO.getDriverName(), uploadTime) + ".jar";
            if (!FileUtil.upload(file.getInputStream(), path, fileName)) {
                return ResponseError.create(500, "新增服务驱动失败!驱动包上传失败");
            } else {
                serviceDriver.setDriverPath(serviceDriverConfigVO.getDriverName() + "/" + serviceDriverConfigVO.getVersion() + "/" +
                        serviceDriverConfigVO.getDriverName() + "-" + serviceDriverConfigVO.getVersion() + "-" + uploadTime + ".jar");
            }
        }
        serviceDriver.setId(serviceDriverMapper.queryPrimaryKey());
        int i = serviceDriverMapper.insert(serviceDriver);
        if (i > 0) {
            if (serviceDriverConfigVO.getParamConfigList() != null && serviceDriverConfigVO.getParamConfigList().size() > 0) {
                List<Parameter> paramConfigList = serviceDriverConfigVO.getParamConfigList();
                //add paramconfig
                for (Parameter parameter : paramConfigList) {
                    parameter.setSeqNum(paramConfigList.indexOf(parameter) + 1);
                    parameter.setCreateBy(loginUser != null ? loginUser.getUserId() : null);
                    parameter.setObjectId(serviceDriver.getId());
                    parameter.setObjectType(ParameterTypesEnum.DRIVER.toString());

                    parameterMapper.insert(parameter);
                }
            }
            return ResponseOk.create("新增服务驱动成功");
        } else {
            return ResponseError.create(500, "新增服务驱动失败");
        }
    }

    /**
     * 更新数据驱动
     *
     * @param serviceDriverConfigVO
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<?> update(ServiceDriverConfigVO serviceDriverConfigVO, MultipartFile file, HttpServletRequest req) throws Exception {
        LoginUserProtos.LoginUser loginUser = getLoginUser(req);
        if (loginUser != null) {
            serviceDriverConfigVO.setUpdateBy(loginUser.getUserId());
        }

        ServiceDriver serviceDriver = parseDriverconfigVO2Driver(serviceDriverConfigVO);
        //upload file
        if (file != null) {
            long uploadTime = System.currentTimeMillis();

            String path = applicationConfig.getUpload() + serviceDriverConfigVO.getDriverName() + "/" + serviceDriverConfigVO.getVersion() + "/";
            String fileName = StringUtils.joinWith("-", serviceDriverConfigVO.getDriverName(), uploadTime) + ".jar";
            if (!FileUtil.upload(file.getInputStream(), path, fileName)) {
                return ResponseError.create(500, "更新服务驱动失败!驱动包上传失败");
            } else {
                serviceDriver.setDriverPath(serviceDriverConfigVO.getDriverName() + "/" + serviceDriverConfigVO.getVersion() + "/" +
                        serviceDriverConfigVO.getDriverName() + "-" + serviceDriverConfigVO.getVersion() + "-" + uploadTime + ".jar");
            }
        }
        int i = serviceDriverMapper.update(serviceDriver);
        if (i > 0) {
            int del = 1;
            Parameter param = new Parameter();
            param.setObjectId(serviceDriverConfigVO.getId());
            List<Parameter> parameters = parameterMapper.queryAll(param);
            if (parameters != null && parameters.size() > 0) {
                del = parameterMapper.deleteByObjectId(serviceDriverConfigVO.getId());
            }
            if (del > 0) {
                // insert parameter of driver
                if (serviceDriverConfigVO.getParamConfigList() != null && serviceDriverConfigVO.getParamConfigList().size() > 0) {
                    List<Parameter> paramConfigList = serviceDriverConfigVO.getParamConfigList();
                    for (Parameter parameter : paramConfigList) {
                        parameter.setSeqNum(paramConfigList.indexOf(parameter) + 1);
                        parameter.setCreateBy(loginUser != null ? loginUser.getUserId() : null);
                        parameter.setObjectId(serviceDriverConfigVO.getId());
                        parameter.setObjectType(ParameterTypesEnum.DRIVER.toString());
                        parameterMapper.insert(parameter);
                    }
                }
            }
            return ResponseOk.create("更新服务驱动成功");
        } else {
            return ResponseError.create(500, "更新服务驱动失败");
        }
    }

    /**
     * 删除数据驱动
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<?> delete(List<String> ids) throws Exception {
        List<ServiceDriver> list = new ArrayList<ServiceDriver>();
        //query dcp_datasource by driverId
        Map<String, Object> map = new HashMap<>();
        for (String id : ids) {
            map.put("driverId", id);
            if (serviceSourceMapper.queryAll(map) != null && serviceSourceMapper.queryAll(map).size() > 0) {
                ServiceSource source = serviceSourceMapper.queryAll(map).get(0);
                if (source != null) {
                    return ResponseError.create("删除服务驱动失败!数据源" + source.getServiceSourceName() + "正在使用中");
                }
            }
            ServiceDriver serviceDriver = serviceDriverMapper.queryByPrimaryKey(id);
            if (serviceDriver != null) {
                list.add(serviceDriver);
            }
        }
        //delete the driver if it is not referenced by the datasource
        int i = serviceDriverMapper.delete(ids);
        if (i > 0) {
            for (String id : ids) {
                Parameter parameter = new Parameter();
                parameter.setObjectId(id);
                logger.info("delete paramconfig");
                List<Parameter> paramConfigList = parameterMapper.queryAll(parameter);
                if (paramConfigList != null && paramConfigList.size() > 0) {
                    for (Parameter parameter1 : paramConfigList) {
                        parameterMapper.delete(parameter1.getId());
                    }
                }
            }
            //delete file
            for (ServiceDriver serviceDriver : list) {
                if (!FileUtil.deleteFile(serviceDriver.getDriverPath(),
                        serviceDriver.getDriverName() + "-" + serviceDriver.getVersion() + ".jar")) {
                    return ResponseError.create(500, "删除服务驱动失败!驱动包删除失败");
                }
            }
            return ResponseOk.create("删除服务驱动成功");
        } else {
            return ResponseError.create(500, "删除服务驱动失败");
        }
    }

    /**
     * 获取记录数
     *
     * @param serviceDriver
     * @return
     * @throws Exception
     */
    @Override
    public int countAll(ServiceDriver serviceDriver) throws Exception {
        int i = 0;
        if (StringUtils.isNotBlank(serviceDriver.getQueryType())) {
            i = serviceDriverMapper.countByCondition(serviceDriver.getQueryString());
        } else {
            i = serviceDriverMapper.countAll(serviceDriver);
        }
        return i;
    }


    /**
     * 获取登录用户
     *
     * @param req
     * @return
     */
    private LoginUserProtos.LoginUser getLoginUser(HttpServletRequest req) {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            return loginUserManager.getLoginUser(token);
        }
        return null;
    }

    /**
     * 获取更新用户
     *
     * @param serviceDriver
     */
    private void queryUpdateUser(ServiceDriver serviceDriver) {
        AuthUser authUser = null;
        if (serviceDriver.getCreateBy() != null) {
            authUser = authUserMapper.selectByloginId(serviceDriver.getCreateBy());
            serviceDriver.setCreateUser(authUser != null ? authUser.getLoginName() : null);
        }
        if (serviceDriver.getUpdateBy() != null) {
            authUser = authUserMapper.selectByloginId(serviceDriver.getUpdateBy());
            serviceDriver.setUpdateUser(authUser != null ? authUser.getLoginName() : null);
        }
    }

    /**
     * 将Driver实体转换为DriverConfigVO实体
     *
     * @param serviceDriver
     * @return
     */
    private ServiceDriverConfigVO parseDriver2DriverconfigVO(ServiceDriver serviceDriver, List<Parameter> parameters) {
        if (serviceDriver != null) {
            ServiceDriverConfigVO serviceDriverConfigVO = new ServiceDriverConfigVO();
            serviceDriverConfigVO.setId(serviceDriver.getId());
            serviceDriverConfigVO.setDriverName(serviceDriver.getDriverName());
            serviceDriverConfigVO.setDriverDesc(serviceDriver.getDriverDesc());
            serviceDriverConfigVO.setDriverClass(serviceDriver.getDriverClass());
            serviceDriverConfigVO.setDriverPath(serviceDriver.getDriverPath());
            serviceDriverConfigVO.setVersion(serviceDriver.getVersion());
            serviceDriverConfigVO.setStatusCode(serviceDriver.getStatusCode());
            serviceDriverConfigVO.setCreateBy(serviceDriver.getCreateBy());
            serviceDriverConfigVO.setCreateTime(serviceDriver.getCreateTime());
            serviceDriverConfigVO.setUpdateBy(serviceDriver.getUpdateBy());
            serviceDriverConfigVO.setUpdateTime(serviceDriver.getUpdateTime());
            serviceDriverConfigVO.setCreateUser(serviceDriver.getCreateUser());
            serviceDriverConfigVO.setUpdateUser(serviceDriver.getUpdateUser());

            serviceDriverConfigVO.setParamConfigList(parameters);
            return serviceDriverConfigVO;
        }
        return null;
    }


    /**
     * 将DriverVO对象转化为Driver
     *
     * @param serviceDriverConfigVO
     * @return
     */
    private ServiceDriver parseDriverconfigVO2Driver(ServiceDriverConfigVO serviceDriverConfigVO) {
        if (serviceDriverConfigVO != null) {
            ServiceDriver serviceDriver = new ServiceDriver();
            serviceDriver.setId(serviceDriverConfigVO.getId());
            serviceDriver.setDriverName(serviceDriverConfigVO.getDriverName());
            serviceDriver.setDriverClass(serviceDriverConfigVO.getDriverClass());
            serviceDriver.setDriverDesc(serviceDriverConfigVO.getDriverDesc());
            serviceDriver.setDriverPath(serviceDriverConfigVO.getDriverPath());
            serviceDriver.setVersion(serviceDriverConfigVO.getVersion());
            serviceDriver.setStatusCode(serviceDriverConfigVO.getStatusCode());
            serviceDriver.setCreateBy(serviceDriverConfigVO.getCreateBy());
            serviceDriver.setCreateTime(serviceDriverConfigVO.getCreateTime());
            serviceDriver.setUpdateBy(serviceDriverConfigVO.getUpdateBy());
            serviceDriver.setUpdateTime(serviceDriverConfigVO.getUpdateTime());

            return serviceDriver;
        }
        return null;
    }


}
