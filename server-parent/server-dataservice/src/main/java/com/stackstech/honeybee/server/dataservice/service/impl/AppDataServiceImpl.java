package com.stackstech.honeybee.server.dataservice.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.stackstech.honeybee.connector.core.util.SqlParser;
import com.stackstech.honeybee.core.conf.ApplicationConfig;
import com.stackstech.honeybee.core.enums.ModelTypesEnum;
import com.stackstech.honeybee.core.enums.RequestTypeEnum;
import com.stackstech.honeybee.core.http.ResponseError;
import com.stackstech.honeybee.core.http.ResponseOk;
import com.stackstech.honeybee.core.model.LoginUserProtos;
import com.stackstech.honeybee.core.pdf.PDFUtil;
import com.stackstech.honeybee.core.util.CommonUtils;
import com.stackstech.honeybee.core.util.HttpUtil;
import com.stackstech.honeybee.core.util.JacksonUtil;
import com.stackstech.honeybee.core.util.MD5Util;
import com.stackstech.honeybee.server.auth.dao.AuthUserMapper;
import com.stackstech.honeybee.server.auth.model.AuthUser;
import com.stackstech.honeybee.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.dataasset.dao.*;
import com.stackstech.dcp.server.dataasset.model.*;
import com.stackstech.honeybee.server.dataservice.dao.AppDsFieldMapper;
import com.stackstech.honeybee.server.dataservice.dao.AppDsMapper;
import com.stackstech.honeybee.server.dataservice.dao.AppUserMapper;
import com.stackstech.honeybee.server.dataservice.dao.DataServiceMapper;
import com.stackstech.honeybee.server.dataservice.model.AppDs;
import com.stackstech.honeybee.server.dataservice.model.AppDsField;
import com.stackstech.honeybee.server.dataservice.model.AppUser;
import com.stackstech.honeybee.server.dataservice.model.DataService;
import com.stackstech.honeybee.server.dataservice.service.AppDataService;
import com.stackstech.honeybee.server.dataservice.vo.AppUserVO;
import com.stackstech.honeybee.server.dataservice.vo.AppVerifyParamVO;
import com.stackstech.honeybee.server.dataservice.vo.AppVerifyVO;
import com.stackstech.honeybee.server.dataservice.vo.DataServiceVO;
import com.stackstech.honeybee.server.datasource.dao.ServiceSourceMapper;
import com.stackstech.honeybee.server.datasource.model.ServiceSource;
import com.stackstech.honeybee.server.platform.dao.ServiceDriverMapper;
import com.stackstech.honeybee.server.platform.model.ServiceDriver;
import com.stackstech.honeybee.server.dataasset.dao.*;
import com.stackstech.honeybee.server.dataasset.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据服务业务逻辑类
 */
@Service
@Transactional
public class AppDataServiceImpl implements AppDataService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataServiceMapper dataServiceMapper;

    @Autowired
    private ModelCodeMapper modelCodeMapper;
    @Autowired
    private ServiceModelMapper serviceModelMapper;
    @Autowired
    private DataAssetAreaMapper assetAreaMapper;
    @Autowired
    private DataAssetTopicMapper assetTopicMapper;
    @Autowired
    private ServiceModelParamMapper serviceModelParamMapper;
    @Autowired
    private ServiceModelFieldMapper serviceModelFieldMapper;

    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private AppDsMapper appDsMapper;
    @Autowired
    private AppDsFieldMapper appDsFieldMapper;

    @Autowired
    private AuthUserMapper authUserMapper;
    @Autowired
    private ServiceSourceMapper serviceSourceMapper;
    @Autowired
    private ServiceDriverMapper driverMapper;
    @Autowired
    private ApplicationConfig applicationConfig;


    /**
     * 数据服务详情查询
     *
     * @param queryVO
     * @return
     */
    @Override
    public List<DataService> queryAll(Map<String, Object> queryVO) throws Exception {
        return dataServiceMapper.queryAll(queryVO);
    }


    /**
     * 数据服务详情查询
     *
     * @param queryVO
     * @return
     */
    @Override
    public Map<String, Object> queryAllByQuery(Map<String, Object> queryVO) {
        List<Map<String, Object>> dataServiceVOS = new ArrayList<>();
        // 获取数据服务信息
        PageInfo<DataService> pageInfo = null;
        if (!StringUtils.isAllEmpty(String.valueOf(queryVO.get("pageNo")), String.valueOf(queryVO.get("pageSize")))) {
            int pageNo = Integer.parseInt(queryVO.get("pageNo").toString());
            int pageSize = Integer.parseInt(queryVO.get("pageSize").toString());
            Page<DataService> result = PageHelper.startPage(pageNo, pageSize);
            dataServiceMapper.queryAllByQuery(queryVO);
            pageInfo = new PageInfo<>(result);
        } else {
            List<DataService> dataServices = dataServiceMapper.queryAllByQuery(queryVO);
            pageInfo = new PageInfo<>(dataServices);
        }

        for (DataService dataService : pageInfo.getList()) {
            //获取更新用户
            this.queryUpdateUser(dataService);

            Map<String, Object> serviceMap = CommonUtils.elementToMap(dataService);

            //查询用户/模型/领域/主题
            StringBuilder buffer = new StringBuilder();
            List<AppDs> appDss = appDsMapper.queryByServiceId(dataService.getId());
            if (appDss != null && appDss.size() > 0) {
                for (AppDs appDs : appDss) {
                    AppUser appUser = appUserMapper.queryByPrimaryKey(appDs.getAppId());
                    if (appUser != null) {
                        buffer.append(appUser.getName()).append(",");
                    }
                }
            }
            ServiceModel queryModel = serviceModelMapper.queryByPrimaryKey(dataService.getServiceModelId());
            Map<String, Object> queryTopic = Maps.newHashMap();
            Map<String, Object> queryArea = Maps.newHashMap();
            if (queryModel != null) {
                if (ModelTypesEnum.ability.toString().equals(queryModel.getTypeCode())) {
                    this.queryAbilityTopic(queryArea, queryTopic, queryModel);
                }
                if (ModelTypesEnum.asset.toString().equals(queryModel.getTypeCode())) {
                    this.queryAssetTopic(queryArea, queryTopic, queryModel);
                }
            }

            if (buffer.length() > 0) {
                serviceMap.put("appName", buffer.substring(0, buffer.length() - 1));
            }
            if (queryModel != null) {
                serviceMap.put("modelName", queryModel.getModelName());
            }
            if (queryArea.size() > 0) {
                serviceMap.put("areaName", queryArea.get("areaName"));
            }
            if (queryTopic.size() > 0) {
                serviceMap.put("topicName", queryTopic.get("topicName"));
            }
            dataServiceVOS.add(serviceMap);
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("count", pageInfo.getTotal());
        map.put("list", dataServiceVOS);
        return map;
    }


    /**
     * 数据服务详情查询
     *
     * @param id
     * @return
     */
    @Override
    public DataServiceVO query(String id, String userId) {
        // 获取数据服务信息
        DataService dataService = dataServiceMapper.queryByPrimaryKey(id);
        if (dataService != null) {
            //获取更新用户
            this.queryUpdateUser(dataService);
            //query model
            ServiceModel queryModel = serviceModelMapper.queryByPrimaryKey(dataService.getServiceModelId());
            //query datasource
            ServiceSource source = serviceSourceMapper.queryByPrimaryKey(queryModel.getServiceSourceId());
            ServiceDriver serviceDriver = driverMapper.queryByPrimaryKey(source.getDriverId());

            Map<String, Object> queryTopic = new HashMap<>();
            Map<String, Object> queryArea = new HashMap<>();
            if (queryModel != null) {
                if (ModelTypesEnum.asset.toString().equals(queryModel.getTypeCode())) {
                    this.queryAssetTopic(queryArea, queryTopic, queryModel);
                } else if (ModelTypesEnum.ability.toString().equals(queryModel.getTypeCode())) {
                    this.queryAbilityTopic(queryArea, queryTopic, queryModel);
                }
            }
            //query fields
            ServiceModelField field = new ServiceModelField();
            field.setServiceModelId(queryModel.getId());
            List<ServiceModelField> modelParams = serviceModelFieldMapper.queryAll(field);

            //query param
            ServiceModelParam param = new ServiceModelParam();
            param.setServiceModelId(queryModel.getId());
            List<ServiceModelParam> modelFilters = serviceModelParamMapper.queryAll(param);

            //query appUser & appDsField
            List<AppUserVO> appUserVOS = new ArrayList<>();
            List<AppDs> appDss = appDsMapper.queryByServiceId(dataService.getId());
            if (appDss != null && appDss.size() > 0) {
                for (AppDs appDs : appDss) {
                    AppUser appUser = appUserMapper.queryByPrimaryKey(appDs.getAppId());

                    if (userId == "" || userId == null || appUser.getAppOwner().equals(userId)) {

                        List<AppDsField> appDsFields = appDsFieldMapper.queryByServiceIdAndAppId(dataService.getId(), appUser.getId());

                        //新增字段描述信息
                        this.addFieldsInfo(appDsFields);

                        AppUserVO appUserVO = parseUser2UserVO(appUser, appDs, appDsFields);
                        appUserVOS.add(appUserVO);
                    }
                }
            }
            DataServiceVO serviceVO = parseService2ServiceVO(dataService, appUserVOS, modelParams, modelFilters, queryArea, queryTopic, queryModel, serviceDriver);
            return serviceVO;
        }
        return null;
    }


    /**
     * 删除数据服务<先暂停才能删除>
     *
     * @param id
     * @return
     */
    @Override
    public int delete(String id) throws Exception {
        int i = dataServiceMapper.delete(id);
        if (i > 0) {
            List<AppDs> appDss = appDsMapper.queryByServiceId(id);
            //delete AppDs
            if (appDss != null && appDss.size() > 0) {
                for (AppDs appDs : appDss) {
                    int index = appDsMapper.delete(appDs.getId());
                    if (index > 0) {
                        List<AppDsField> appDsFields = appDsFieldMapper.queryByServiceIdAndAppId(id, appDs.getAppId());
                        if (appDsFields != null && appDsFields.size() > 0) {
                            for (AppDsField field : appDsFields) {
                                appDsFieldMapper.delete(field.getId());
                            }
                        }
                    }
                }
            }
        }
        return i;
    }

    /**
     * 数据服务修改<不需要更新></>
     *
     * @param dataService
     * @return
     */
    @Override
    public int update(DataService dataService) throws Exception {
        // 获取数据服务信息
        LoginUserProtos.LoginUser loginUser = LoginUserManager.getLoginUser();
        if (loginUser != null) {
            dataService.setUpdateBy(loginUser.getUserId());
        }
        return dataServiceMapper.update(dataService);
    }

    /**
     * 校验重名
     *
     * @param dataServiceName
     * @return
     * @throws Exception
     */
    @Override
    public DataService queryByName(String dataServiceName) throws Exception {
        return dataServiceMapper.queryByName(dataServiceName);
    }

    /**
     * 获取DataService列表
     *
     * @return
     */
    @Override
    public List<DataService> queryDataservices(String typeCode, String appId) throws Exception {
        List<String> ids = new ArrayList<>();
        List<AppDs> appDss = appDsMapper.queryByAppId(appId);
        if (appDss != null && appDss.size() > 0) {
            for (AppDs appDs : appDss) {
                ids.add(appDs.getDataServiceId());
            }
        }
        //query AuthDataServices
        List<DataService> authDataservices = new ArrayList<DataService>();
        List<DataService> dataServices = dataServiceMapper.query(ids);
        if (dataServices != null && dataServices.size() > 0) {
            for (DataService dataService : dataServices) {
                if (typeCode != null && typeCode.equalsIgnoreCase(dataService.getTypeCode())) {
                    authDataservices.add(dataService);
                }
            }
        }
        return authDataservices;
    }

    /**
     * 服务授权
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public boolean auth(Map<String, Object> map) throws Exception {
        String id = (String) map.get("id");
        String json = JacksonUtil.beanToJson(map.get("appUsers"));
        List<AppUserVO> appUsers = JacksonUtil.parseJsonList(json, AppUserVO.class);
        //del AppDss
        int dsDel = 1;
        List<AppDs> appDss = appDsMapper.queryByServiceId(id);
        if (appDss != null && appDss.size() > 0) {
            dsDel = appDsMapper.deleteByServiceId(id);
        }
        //del AppDsFields
        int fieldDel = 1;
        List<AppDsField> appDsFields = appDsFieldMapper.queryByServiceId(id);
        if (appDsFields != null && appDsFields.size() > 0) {
            fieldDel = appDsFieldMapper.deleteByServiceId(id);
        }
        //insert AppDs & AppDsField
        if (dsDel > 0 && fieldDel > 0) {
            if (appUsers != null && appUsers.size() > 0) {
                for (int i = 0; i < appUsers.size(); i++) {
                    // insert AppDs
                    AppUserVO appUserVO = appUsers.get(i);
                    if (appUserVO != null) {
                        AppDs ds = new AppDs();
                        ds.setAppId(appUserVO.getId());
                        ds.setDataServiceId(id);
                        ds.setSample("y");
                        ds.setToken(MD5Util.getLowercaseMD5(appUserVO.getId() + id));
                        ds.setSeqNum(i + 1);
                        ds.setTokenExpiredTime(new Timestamp(Long.valueOf("1111")));                //token时效值
                        // insert AppDsField
                        if (appDsMapper.insert(ds) > 0) {
                            List<AppDsField> dsFields = appUserVO.getDsFields();
                            if (dsFields != null && dsFields.size() > 0) {
                                for (AppDsField field : dsFields) {
                                    field.setAppId(appUserVO.getId());
                                    field.setDataServiceId(id);
                                    field.setSeqNum(dsFields.indexOf(field) + 1);
                                    appDsFieldMapper.insert(field);
                                }
                            } else {
                                // insert AppDsFields<Ability-Model>
                                DataService service = dataServiceMapper.queryByPrimaryKey(id);
                                if (service != null && ModelTypesEnum.ability.toString().equalsIgnoreCase(service.getTypeCode())) {
                                    ServiceModel model = serviceModelMapper.queryByPrimaryKey(service.getServiceModelId());
                                    if (model != null) {
                                        List<ServiceModelField> fields = serviceModelFieldMapper.queryByServiceId(model.getId());
                                        if (fields != null && fields.size() > 0) {
                                            for (ServiceModelField modelField : fields) {
                                                AppDsField dsField = new AppDsField();
                                                dsField.setAppId(appUserVO.getId());
                                                dsField.setDataServiceId(id);
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
     * 服务验证
     *
     * @param appVerify
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<?> verify(AppVerifyVO appVerify) throws Exception {
        String response = null;
        if (appVerify != null) {
            //设置请求参数
            Map<String, String> headers = new HashMap<>();
            headers.put("token", appVerify.getToken());
            headers.put("appid", appVerify.getAppId());
            //是否返回json格式
            if (appVerify.getDataFormat() != null && "data".equalsIgnoreCase(appVerify.getDataFormat())) {
                headers.put("data-type", appVerify.getDataFormat());
            }

            Map<String, Object> params = new HashMap<>();
            params.put("dataServiceId", appVerify.getDataServiceId());

            String url = applicationConfig.getApi();

            if (appVerify.getDataServiceId() != null) {
                DataService dataService = dataServiceMapper.queryByPrimaryKey(appVerify.getDataServiceId());
                if (dataService != null) {
                    //判断是否授权
                    List<AppDs> appDss = appDsMapper.queryByServiceId(dataService.getId());
                    if (StringUtils.isBlank(appVerify.getAppId()) || appDss == null || appDss.size() <= 0) {
                        return ResponseError.create(500, "当前服务未授权,验证失败");
                    }
                    String requestMethod = dataService.getRequestMethod();
                    if (requestMethod != null && RequestTypeEnum.POST.toString().equalsIgnoreCase(requestMethod)) {
                        params.put("data", appVerify.getData());
                        response = HttpUtil.doPost(url, headers, JacksonUtil.beanToJson(params));
                    } else if (requestMethod != null && RequestTypeEnum.GET.toString().equalsIgnoreCase(requestMethod)) {
                        params.put("data", CommonUtils.elementToMap(appVerify.getData()));
                        response = HttpUtil.doGet(url, headers, params);
                    }
                }
            }
            if (response != null) {
                response = (response.length() > 1000 ? response.substring(0, 1000) : response);
            }
        }
        return ResponseOk.create(response);
    }


    /**
     * 下载模板
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public void createTemplates(String downloadParams, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        AppVerifyVO appVerify = JacksonUtil.jsonToBean(downloadParams, AppVerifyVO.class);
        if (appVerify == null) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        AppVerifyParamVO paramVO = appVerify.getData();

        //基本信息
        DataService dataService = dataServiceMapper.queryByPrimaryKey(appVerify.getDataServiceId());
        AppUser appUser = appUserMapper.queryByPrimaryKey(appVerify.getAppId());
        params.put("serviceName", dataService.getDataServiceName());
        params.put("serviceDesc", dataService.getDataServiceDesc());
        params.put("requestMethod", dataService.getRequestMethod());
        params.put("token", appVerify.getToken());
        params.put("appId", appVerify.getAppId());
        params.put("dataServiceId", appVerify.getDataServiceId());
        params.put("userName", appUser != null ? appUser.getName() : null);
        //返回json格式数据
        String dataFormat = appVerify.getDataFormat() != null ? String.valueOf(appVerify.getDataFormat()) : null;
        params.put("dataFormat", dataFormat);

        //参数列表
        List<Map<String, Object>> serviceModelParams = null;
        if (paramVO != null) {
            serviceModelParams = paramVO.getParams();
            if (serviceModelParams != null) {
                params.put("appParams", serviceModelParams);
            }
        }

        //属性列表
        List<AppDsField> appDsFields = appDsFieldMapper.queryByServiceIdAndAppId(appVerify.getDataServiceId(), appVerify.getAppId());
        if (appDsFields != null) {
            //新增字段描述信息
            this.addFieldsInfo(appDsFields);
            params.put("appFields", appDsFields);
        }

        //解析URL
        this.parseUrl(params, serviceModelParams, appVerify.getAppId(),
                appVerify.getDataServiceId(), appVerify.getToken(), dataFormat, dataService.getRequestMethod());

        //获取响应数据
        String response = "{\"code\":200,\"message\":\"success\",\"data\":{\"result\":{\"返回数据\"}}}";
        params.put("responseMessage", response);

        //下载模板样例
        if (params != null) {
            PDFUtil.createWord(params, applicationConfig.getUpload(), req, resp);
        }
    }

    /**
     * 封装URL
     *
     * @return
     */
    private void parseUrl(Map<String, Object> params, List<Map<String, Object>> serviceModelParams,
                          String appId, String dataServiceId,
                          String token, String dataFormat, String requestMethod) {

        String url = applicationConfig.getApi();

        StringBuffer buffer = new StringBuffer(url);

        if (null != requestMethod && RequestTypeEnum.POST.toString().equalsIgnoreCase(requestMethod)) {
            params.put("url", url);
            params.put("requestType", "application/json");

        } else if (null != requestMethod && RequestTypeEnum.GET.toString().equalsIgnoreCase(requestMethod)) {
            buffer.append("?token=").append(token)
                    .append("&appid=").append(appId)
                    .append("&dataServiceId=").append(dataServiceId);
            if (dataFormat != null) {
                buffer.append("&data-type=").append(dataFormat);
            }
            if (serviceModelParams != null) {
                for (Map<String, Object> param : serviceModelParams) {
                    if (param.get("name") != null && param.get("value") != null && StringUtils.isNotBlank((String) param.get("value"))) {
                        buffer.append("&").append(param.get("name")).append("=").append(param.get("value"));
                    }
                }
            }
            params.put("url", buffer.toString());
        }
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
                        appDsField.setDataType(serviceModelField.getDataType());
                        appDsField.setFieldDesc(serviceModelField.getFieldDesc());
                    }
                }
            }
        }
    }

    /**
     * 查询资产模型主题
     *
     * @param exsitArea
     * @param exsitTopic
     * @param serviceModel
     */
    private void queryAssetTopic(Map<String, Object> exsitArea, Map<String, Object> exsitTopic, ServiceModel serviceModel) {
        DataAssetTopic queryTopic = assetTopicMapper.queryByPrimaryKey(serviceModel.getTopicId());
        if (queryTopic != null) {
            exsitTopic.put("topicName", queryTopic.getTopicName());
            DataAssetArea queryArea = assetAreaMapper.queryByPrimaryKey(queryTopic.getAreaId());
            if (queryArea != null) {
                exsitArea.put("areaName", queryArea.getAreaName());
            }
        }
    }

    /**
     * 查询能力模型主题
     *
     * @param exsitArea
     * @param exsitTopic
     * @param serviceModel
     */
    private void queryAbilityTopic(Map<String, Object> exsitArea, Map<String, Object> exsitTopic, ServiceModel serviceModel) {
        ModelCode queryTopic = modelCodeMapper.queryByPrimaryKey(Long.parseLong(serviceModel.getTopicId()));
        if (queryTopic != null) {
            exsitTopic.put("topicName", queryTopic.getDisplayName());
            ModelCode queryArea = modelCodeMapper.queryByCode(queryTopic.getParentCode());
            if (queryArea != null) {
                exsitArea.put("areaName", queryArea.getDisplayName());
            }
        }
    }

    /**
     * 查询更新用户
     *
     * @param dataService
     */
    private void queryUpdateUser(DataService dataService) {
        AuthUser authUser = null;
        if (dataService.getCreateBy() != null) {
            authUser = authUserMapper.selectByloginId(dataService.getCreateBy());
            dataService.setCreateUser(authUser != null ? authUser.getLoginName() : null);
        }
        if (dataService.getUpdateBy() != null) {
            authUser = authUserMapper.selectByloginId(dataService.getUpdateBy());
            dataService.setUpdateUser(authUser != null ? authUser.getLoginName() : null);
        }
    }


    /**
     * 将DataServiceVO转化为DataService
     *
     * @param serviceVO
     * @return
     */
    private DataService parseServiceVO2Service(DataServiceVO serviceVO) {
        if (serviceVO != null) {
            DataService service = new DataService();
            service.setId(serviceVO.getId());
            service.setServiceModelId(serviceVO.getServiceModelId());
            service.setTypeCode(serviceVO.getTypeCode());
            service.setStatusCode(serviceVO.getStatusCode());
            service.setDataServiceName(serviceVO.getDataServiceName());
            service.setDataServiceDesc(serviceVO.getDataServiceDesc());
            service.setExpression(serviceVO.getExpression());
            service.setOperateType(serviceVO.getOperateType());
            service.setExpression(serviceVO.getExpression());
            service.setRequestMethod(serviceVO.getRequestMethod());
            service.setCreateBy(serviceVO.getCreateBy());
            service.setCreateTime(serviceVO.getCreateTime());
            service.setUpdateBy(serviceVO.getUpdateBy());
            service.setUpdateTime(serviceVO.getUpdateTime());

            return service;
        }
        return null;
    }


    /**
     * 将DataService转化为DataServiceVO
     *
     * @param
     * @return
     */
    private DataServiceVO parseService2ServiceVO(DataService service, List<AppUserVO> appUserVOS, List<ServiceModelField> modelParams, List<ServiceModelParam> modelFilters,
                                                 Map<String, Object> dataAssetArea, Map<String, Object> dataAssetTopic, ServiceModel serviceModel, ServiceDriver serviceDriver) {
        if (service != null) {
            DataServiceVO serviceVO = new DataServiceVO();
            serviceVO.setId(service.getId());
            serviceVO.setId(service.getId());
            serviceVO.setServiceModelId(service.getServiceModelId());
            serviceVO.setTypeCode(service.getTypeCode());
            serviceVO.setStatusCode(service.getStatusCode());
            serviceVO.setDataServiceName(service.getDataServiceName());
            serviceVO.setDataServiceDesc(service.getDataServiceDesc());
            serviceVO.setExpression(service.getExpression());
            serviceVO.setOperateType(service.getOperateType());
            serviceVO.setExpression(service.getExpression());
            serviceVO.setRequestMethod(service.getRequestMethod());
            serviceVO.setCreateBy(service.getCreateBy());
            serviceVO.setCreateTime(service.getCreateTime());
            serviceVO.setUpdateBy(service.getUpdateBy());
            serviceVO.setUpdateTime(service.getUpdateTime());
            serviceVO.setCreateUser(service.getCreateUser());
            serviceVO.setUpdateUser(service.getUpdateUser());
            serviceVO.setUrl(applicationConfig.getApi());
            serviceVO.setAppUsers(appUserVOS);
            serviceVO.setDataAssetArea(dataAssetArea);
            serviceVO.setDataAssetTopic(dataAssetTopic);
            serviceVO.setServiceModel(serviceModel);
            if (serviceDriver != null && serviceDriver.getDriverClass() != null) {
                //关系型数据库-->返回json格式
                if ("data".equals(SqlParser.parseModelType(serviceDriver.getDriverClass()))) {
                    serviceVO.setDataFormat("data");
                }
            }
            serviceVO.setModelParams(modelParams);
            serviceVO.setModelFilters(modelFilters);
            return serviceVO;
        }
        return null;
    }

    /**
     * 将APP用户转化为AppUserVO工具类
     *
     * @param appUser
     * @param appDs
     * @param appDsFields
     * @return
     */
    private AppUserVO parseUser2UserVO(AppUser appUser, AppDs appDs, List<AppDsField> appDsFields) {
        if (appUser != null) {
            AppUserVO appUserVO = new AppUserVO();
            appUserVO.setId(appUser.getId());
            appUserVO.setName(appUser.getName());
            appUserVO.setAppOwner(appUser.getAppOwner());
            appUserVO.setAppDesc(appUser.getAppDesc());
            appUserVO.setStatusCode(appUser.getStatusCode());
            appUserVO.setCreateBy(appUser.getCreateBy());
            appUserVO.setCreateTime(appUser.getCreateTime());
            appUserVO.setUpdateBy(appUser.getUpdateBy());
            appUserVO.setUpdateTime(appUser.getUpdateTime());

            appUserVO.setAppDs(appDs);
            appUserVO.setDsFields(appDsFields);
            return appUserVO;
        }
        return null;
    }

    /**
     * 将APP用户转化为AppUserVO工具类
     *
     * @param appUserVO
     * @return
     */
    private AppUser parseUserVO2User(AppUserVO appUserVO) {
        if (appUserVO != null) {
            AppUser appUser = new AppUser();
            appUser.setId(appUserVO.getId());
            appUser.setName(appUserVO.getName());
            appUser.setAppOwner(appUserVO.getAppOwner());
            appUser.setAppDesc(appUserVO.getAppDesc());
            appUser.setStatusCode(appUserVO.getStatusCode());
            appUser.setCreateBy(appUserVO.getCreateBy());
            appUser.setCreateTime(appUserVO.getCreateTime());
            appUser.setUpdateBy(appUserVO.getUpdateBy());
            appUser.setUpdateTime(appUserVO.getUpdateTime());

            return appUser;
        }
        return null;
    }


}
