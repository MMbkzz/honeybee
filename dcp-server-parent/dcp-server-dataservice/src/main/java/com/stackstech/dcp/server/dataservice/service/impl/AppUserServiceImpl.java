package com.stackstech.dcp.server.dataservice.service.impl;

import com.stackstech.dcp.core.enums.ModelTypesEnum;
import com.stackstech.dcp.core.enums.ServiceStatusEnum;
import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.core.util.CommonUtils;
import com.stackstech.dcp.core.util.DateUtils;
import com.stackstech.dcp.core.util.JacksonUtil;
import com.stackstech.dcp.core.util.MD5Util;
import com.stackstech.dcp.server.auth.dao.AuthUserMapper;
import com.stackstech.dcp.server.auth.model.AuthUser;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import com.stackstech.dcp.server.dataasset.dao.ServiceModelFieldMapper;
import com.stackstech.dcp.server.dataasset.dao.ServiceModelMapper;
import com.stackstech.dcp.server.dataasset.model.ServiceModel;
import com.stackstech.dcp.server.dataasset.model.ServiceModelField;
import com.stackstech.dcp.server.dataservice.dao.AppDsFieldMapper;
import com.stackstech.dcp.server.dataservice.dao.AppDsMapper;
import com.stackstech.dcp.server.dataservice.dao.AppUserMapper;
import com.stackstech.dcp.server.dataservice.dao.DataServiceMapper;
import com.stackstech.dcp.server.dataservice.model.AppDs;
import com.stackstech.dcp.server.dataservice.model.AppDsField;
import com.stackstech.dcp.server.dataservice.model.AppUser;
import com.stackstech.dcp.server.dataservice.model.DataService;
import com.stackstech.dcp.server.dataservice.service.AppUserService;
import com.stackstech.dcp.server.dataservice.vo.AppServiceVO;
import com.stackstech.dcp.server.dataservice.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数据服务APP用户业务逻辑类
 */
@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataServiceMapper dataServiceMapper;
    @Autowired
    private ServiceModelFieldMapper serviceModelFieldMapper;
    @Autowired
    private ServiceModelMapper serviceModelMapper;

    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private AppDsMapper appDsMapper;
    @Autowired
    private AppDsFieldMapper appDsFieldMapper;

    @Autowired
    private AuthUserMapper authUserMapper;
    @Autowired
    private LoginUserManager loginUserManager;

    /**
     * 新增数据服务APP用户
     *
     * @param appUser
     * @return
     */
    @Override
    public int add(AppUser appUser, HttpServletRequest req) throws Exception {
        LoginUserProtos.LoginUser loginUser = getLoginUser(req);
        if (loginUser != null) {
            appUser.setCreateBy(loginUser.getUserId());
            appUser.setUpdateBy(loginUser.getUserId());
        }

        appUser.setId(appUserMapper.queryPrimaryKey());
        int i = appUserMapper.insert(appUser);
        return i;
    }

    /**
     * 数据服务APP用户列表
     *
     * @param appUser,page
     * @return
     */
    @Override
    public List<Map<String, Object>> queryAll(AppUser appUser) throws Exception {
        List<Map<String, Object>> userVOS = new ArrayList<>();
        // 获取数据服务APP用户信息
        List<AppUser> appUsers = null;
        if (StringUtils.isNotBlank(appUser.getQueryType())) {
            appUsers = appUserMapper.queryByCondition(appUser.getQueryString());
        } else {
            appUsers = appUserMapper.queryAll(appUser);
        }
        if (appUsers != null && appUsers.size() > 0) {
            for (AppUser app : appUsers) {
                //获取更新用户
                this.queryUpdateUser(app);

                Map<String, Object> appMap = CommonUtils.elementToMap(app);
                //查询服务列表
                StringBuffer buffer = new StringBuffer();
                List<AppDs> appDss = appDsMapper.queryByAppId(app.getId());
                if (appDss != null && appDss.size() > 0) {
                    for (AppDs appDs : appDss) {
                        DataService dataService = dataServiceMapper.queryByPrimaryKey(appDs.getDataServiceId());
                        if (dataService != null) {
                            buffer.append(dataService.getDataServiceName()).append(",");
                        }
                    }
                }
                if (buffer != null && buffer.length() > 0) {
                    appMap.put("serviceName", buffer.substring(0, buffer.length() - 1));
                }
                userVOS.add(appMap);
            }
        }
        return userVOS;
    }


    /**
     * 获取APPUser列表
     *
     * @return
     */
    @Override
    public List<AppUser> queryUsers(String dataServiceId) throws Exception {
        List<String> ids = new ArrayList<>();
        List<AppDs> appDss = appDsMapper.queryByServiceId(dataServiceId);
        if (appDss != null && appDss.size() > 0) {
            for (AppDs appDs : appDss) {
                ids.add(appDs.getAppId());
            }
        }
        return appUserMapper.query(ids);
    }

    @Override
    public AppUser queryByName(String name) throws Exception {
        return appUserMapper.queryByName(name);
    }

    /**
     * 数据服务APP用户详情查询
     *
     * @param id
     * @return
     */
    @Override
    public UserVO query(String id) throws Exception {
        // 获取数据服务APP用户信息
        AppUser app = appUserMapper.queryByPrimaryKey(id);
        if (app != null) {
            //获取更新用户
            this.queryUpdateUser(app);

            List<AppServiceVO> appServiceVOS = new ArrayList<>();
            List<AppDs> appDss = appDsMapper.queryByAppId(app.getId());
            if (appDss != null && appDss.size() > 0) {
                //query all AppDss by appId
                for (AppDs appDs : appDss) {
                    DataService dataService = dataServiceMapper.queryByPrimaryKey(appDs.getDataServiceId());
                    List<AppDsField> appDsFields = null;
                    List<ServiceModelField> modelParams = null;
                    if (dataService != null && dataService.getId() != null) {
                        appDsFields = appDsFieldMapper.queryByServiceIdAndAppId(dataService.getId(), app.getId());
                        //添加授权字段描述和类型字段
                        this.addFieldsInfo(appDsFields);
                        //query modelParams
                        ServiceModelField field = new ServiceModelField();
                        field.setServiceModelId(dataService.getServiceModelId());
                        modelParams = serviceModelFieldMapper.queryAll(field);
                    }
                    AppServiceVO appServiceVO = parseService2ServiceVO(dataService, appDs, appDsFields, modelParams);
                    if (appServiceVO != null) {
                        appServiceVOS.add(appServiceVO);
                    }
                }
            }
            UserVO userVO = parseApp2AppVO(app, appServiceVOS);
            return userVO;
        }
        return null;
    }

    /**
     * 获取记录数
     *
     * @param appUser
     * @return
     * @throws Exception
     */
    @Override
    public int countAll(AppUser appUser) throws Exception {
        int i = 0;
        if (StringUtils.isNotBlank(appUser.getQueryType())) {
            i = appUserMapper.countByCondition(appUser.getQueryString());
        } else {
            i = appUserMapper.countAll(appUser);
        }
        return i;
    }

    @Override
    public int addOrUpdate(AppUser appUser, long userId) {
        if (StringUtils.isEmpty(appUser.getId())) {
            appUser.setCreateBy(userId);
            appUser.setCreateTime(DateUtils.getDefaultCurrentTimeStamp(new Date()));
            appUser.setId(appUserMapper.queryPrimaryKey());
            return appUserMapper.insert(appUser);
        } else {
            appUser.setUpdateBy(userId);
            appUser.setUpdateTime(DateUtils.getDefaultCurrentTimeStamp(new Date()));
            return appUserMapper.update(appUser);
        }
    }

    /**
     * 数据服务APP用户详情查询
     *
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<?> delete(String id) throws Exception {
        // 获取数据服务APP用户信息
        AppUser appUser = appUserMapper.queryByPrimaryKey(id);
        if (appUser != null && !ServiceStatusEnum.disabled.toString().equals(appUser.getStatusCode())) {
            return ResponseError.create(500, "当前用户处于启用状态,不能进行删除操作");
        }
        int i = appUserMapper.delete(id);
        if (i > 0) {
            List<AppDs> appDss = appDsMapper.queryByAppId(id);
            //delete AppDs
            if (appDss != null && appDss.size() > 0) {
                for (AppDs appDs : appDss) {
                    int index = appDsMapper.delete(appDs.getId());
                    if (index > 0) {
                        List<AppDsField> appDsFields = appDsFieldMapper.queryByServiceIdAndAppId(appDs.getDataServiceId(), id);
                        if (appDsFields != null && appDsFields.size() > 0) {
                            for (AppDsField field : appDsFields) {
                                appDsFieldMapper.delete(field.getId());
                            }
                        }
                    }
                }
            }
            return ResponseOk.create("用户删除成功");
        } else {
            return ResponseError.create(500, "用户删除失败");
        }
    }

    /**
     * 数据服务APP用户修改
     *
     * @param appUser
     * @return
     */
    @Override
    public int update(AppUser appUser, HttpServletRequest req) throws Exception {
        // 获取数据服务APP用户信息
        LoginUserProtos.LoginUser loginUser = getLoginUser(req);
        if (loginUser != null) {
            appUser.setUpdateBy(loginUser.getUserId());
        }
        int status = appUserMapper.update(appUser);
        return status;
    }

    /**
     * APP授权
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public boolean auth(Map<String, Object> map) throws Exception {
        String id = (String) map.get("id");
        String json = JacksonUtil.beanToJson(map.get("appServices"));
        List<AppServiceVO> appServices = JacksonUtil.parseJsonList(json, AppServiceVO.class);
        //del AppDss
        int dsDel = 1;
        List<AppDs> appDss = appDsMapper.queryByAppId(id);
        if (appDss != null && appDss.size() > 0) {
            dsDel = appDsMapper.deleteByAppId(id);
        }
        //del AppDsFields
        int fieldDel = 1;
        List<AppDsField> appDsFields = appDsFieldMapper.queryByAppId(id);
        if (appDsFields != null && appDsFields.size() > 0) {
            fieldDel = appDsFieldMapper.deleteByAppId(id);
        }
        //insert AppDs & AppDsField
        if (dsDel > 0 && fieldDel > 0) {
            if (appServices != null && appServices.size() > 0) {
                for (int i = 0; i < appServices.size(); i++) {
                    //insert Appds
                    AppServiceVO serviceVO = appServices.get(i);
                    if (serviceVO != null) {
                        AppDs ds = new AppDs();
                        ds.setAppId(id);
                        ds.setDataServiceId(serviceVO.getId());
                        ds.setSample("y");
                        ds.setToken(MD5Util.getLowercaseMD5(id + serviceVO.getId()));
                        ds.setSeqNum(i + 1);
                        ds.setTokenExpiredTime(new Timestamp(Long.valueOf("1111")));                //token时效值
                        // insert AppDsField
                        if (appDsMapper.insert(ds) > 0) {
                            List<AppDsField> dsFields = serviceVO.getDsFields();
                            if (dsFields != null && dsFields.size() > 0) {
                                for (AppDsField field : dsFields) {
                                    field.setAppId(id);
                                    field.setDataServiceId(serviceVO.getId());
                                    field.setSeqNum(dsFields.indexOf(field) + 1);
                                    appDsFieldMapper.insert(field);
                                }
                            } else {
                                // insert AppDsFields<Ability-Model>
                                DataService service = dataServiceMapper.queryByPrimaryKey(serviceVO.getId());
                                if (service != null && ModelTypesEnum.ability.toString().equalsIgnoreCase(service.getTypeCode())) {
                                    ServiceModel model = serviceModelMapper.queryByPrimaryKey(service.getServiceModelId());
                                    if (model != null) {
                                        List<ServiceModelField> fields = serviceModelFieldMapper.queryByServiceId(model.getId());
                                        if (fields != null && fields.size() > 0) {
                                            for (ServiceModelField modelField : fields) {
                                                AppDsField dsField = new AppDsField();
                                                dsField.setAppId(id);
                                                dsField.setDataServiceId(serviceVO.getId());
                                                dsField.setFieldId(String.valueOf(modelField.getId()));
                                                dsField.setFieldName(modelField.getFieldName());
                                                dsField.setSeqNum(fields.indexOf(modelField) + 1);
                                                appDsFieldMapper.insert(dsField);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }


    /**
     * 查询更新用户
     *
     * @param app
     */
    private void queryUpdateUser(AppUser app) {
        AuthUser authUser = null;
        if (app.getCreateBy() != null) {
            authUser = authUserMapper.selectByloginId(app.getCreateBy());
            app.setCreateUser(authUser != null ? authUser.getLoginName() : null);
        }
        if (app.getUpdateBy() != null) {
            authUser = authUserMapper.selectByloginId(app.getUpdateBy());
            app.setUpdateUser(authUser != null ? authUser.getLoginName() : null);
        }
    }

    /**
     * 返回LoginUser
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
     * 新增字段描述&类型
     *
     * @param appDsFields
     */
    private void addFieldsInfo(List<AppDsField> appDsFields) {
        if (appDsFields != null && appDsFields.size() > 0) {
            for (AppDsField appDsField : appDsFields) {
                String fieldId = appDsField.getFieldId();
                if (StringUtils.isNotBlank(fieldId)) {
                    ServiceModelField serviceModelField = serviceModelFieldMapper.queryByPrimaryKey(Long.valueOf(fieldId));
                    if (serviceModelField != null) {
                        //appDsField.setDataType(serviceModelField.getDataType());
                        appDsField.setFieldDesc(serviceModelField.getFieldDesc());
                    }
                }
            }
        }
    }

    /**
     * 将AppUser转换成AppVO
     *
     * @param app
     * @param appServices
     * @return
     * @throws Exception
     */
    private UserVO parseApp2AppVO(AppUser app, List<AppServiceVO> appServices) throws Exception {
        if (app != null) {
            UserVO userVO = new UserVO();
            userVO.setId(app.getId());
            userVO.setName(app.getName());
            userVO.setAppDesc(app.getAppDesc());
            userVO.setAppOwner(app.getAppOwner());
            userVO.setStatusCode(app.getStatusCode());
            userVO.setCreateBy(app.getCreateBy());
            userVO.setCreateTime(app.getCreateTime());
            userVO.setUpdateBy(app.getUpdateBy());
            userVO.setUpdateTime(app.getUpdateTime());
            userVO.setCreateUser(app.getCreateUser());
            userVO.setUpdateUser(app.getUpdateUser());

            if (appServices != null && appServices.size() > 0) {
                List<AppServiceVO> assetServices = new ArrayList<>();
                List<AppServiceVO> abilityServices = new ArrayList<>();
                for (AppServiceVO appServiceVO : appServices) {
                    if (ModelTypesEnum.asset.toString().equalsIgnoreCase(appServiceVO.getTypeCode())) {
                        assetServices.add(appServiceVO);
                    } else if (ModelTypesEnum.ability.toString().equalsIgnoreCase(appServiceVO.getTypeCode())) {
                        abilityServices.add(appServiceVO);
                    }
                }
                userVO.setAppServices(assetServices);
                userVO.setAbilityServices(abilityServices);
            }
            return userVO;
        }
        return null;
    }

    /**
     * 将DataService实体转换为DataServiceVO
     *
     * @param dataService
     * @param dsFields
     * @return
     */
    private AppServiceVO parseService2ServiceVO(DataService dataService, AppDs appDs, List<AppDsField> dsFields, List<ServiceModelField> modelParams) {
        if (dataService != null) {
            AppServiceVO serviceVO = new AppServiceVO();
            serviceVO.setId(dataService.getId());
            serviceVO.setDataServiceName(dataService.getDataServiceName());
            serviceVO.setDataServiceDesc(dataService.getDataServiceDesc());
            serviceVO.setServiceModelId(dataService.getServiceModelId());
            serviceVO.setExpression(dataService.getExpression());
            serviceVO.setRequestMethod(dataService.getRequestMethod());
            serviceVO.setStatusCode(dataService.getStatusCode());
            serviceVO.setTypeCode(dataService.getTypeCode());
            serviceVO.setOperateType(dataService.getOperateType());
            serviceVO.setCreateBy(dataService.getCreateBy());
            serviceVO.setCreateTime(dataService.getCreateTime());
            serviceVO.setUpdateBy(dataService.getUpdateBy());
            serviceVO.setUpdateTime(dataService.getUpdateTime());

            serviceVO.setDsFields(dsFields);
            serviceVO.setModelParams(modelParams);
            serviceVO.setAppDs(appDs);
            return serviceVO;
        }
        return null;
    }

}
