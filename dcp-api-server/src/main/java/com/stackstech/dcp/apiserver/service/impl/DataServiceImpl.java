package com.stackstech.dcp.apiserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.stackstech.dcp.apiserver.exception.ServiceAccessException;
import com.stackstech.dcp.apiserver.model.RequestData;
import com.stackstech.dcp.apiserver.model.ServiceField;
import com.stackstech.dcp.apiserver.model.ServiceFilter;
import com.stackstech.dcp.apiserver.model.ServiceParam;
import com.stackstech.dcp.apiserver.service.ApiDataService;
import com.stackstech.dcp.apiserver.util.DriverDataUtil;
import com.stackstech.dcp.connector.core.ResourceSession;
import com.stackstech.dcp.connector.core.ResourceSessionFactory;
import com.stackstech.dcp.connector.core.ResourceSessionManager;
import com.stackstech.dcp.connector.core.entity.DriverMetaData;
import com.stackstech.dcp.connector.core.enums.MetaDataTypeEnum;
import com.stackstech.dcp.connector.core.executor.DriverExecutorAdapter;
import com.stackstech.dcp.core.cache.ApiLogDataCache;
import com.stackstech.dcp.core.cache.ServiceDataCache;
import com.stackstech.dcp.core.enums.ModelTypesEnum;
import com.stackstech.dcp.core.log.AccessLogHelper;
import com.stackstech.dcp.core.util.JacksonUtil;
import com.stackstech.dcp.core.util.MD5Util;
import com.stackstech.dcp.server.dataasset.dao.ModelCodeMapper;
import com.stackstech.dcp.server.dataasset.dao.ServiceModelFieldMapper;
import com.stackstech.dcp.server.dataasset.dao.ServiceModelMapper;
import com.stackstech.dcp.server.dataasset.dao.ServiceModelParamMapper;
import com.stackstech.dcp.server.dataasset.model.ModelCode;
import com.stackstech.dcp.server.dataasset.model.ServiceModel;
import com.stackstech.dcp.server.dataasset.model.ServiceModelField;
import com.stackstech.dcp.server.dataasset.model.ServiceModelParam;
import com.stackstech.dcp.server.dataservice.dao.AppDsFieldMapper;
import com.stackstech.dcp.server.dataservice.dao.AppDsMapper;
import com.stackstech.dcp.server.dataservice.dao.AppUserMapper;
import com.stackstech.dcp.server.dataservice.dao.DataServiceMapper;
import com.stackstech.dcp.server.dataservice.model.AppDs;
import com.stackstech.dcp.server.dataservice.model.AppDsField;
import com.stackstech.dcp.server.dataservice.model.AppUser;
import com.stackstech.dcp.server.dataservice.model.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.qos.logback.core.joran.action.ActionConst.NULL;

@Service
public class DataServiceImpl implements ApiDataService {

    private static final Logger log = LoggerFactory.getLogger(DataServiceImpl.class);

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private DataServiceMapper dataServiceMapper;

    @Autowired
    private AppDsMapper appDsMapper;

    @Autowired
    private ServiceModelParamMapper serviceModelParamMapper;

    @Autowired
    private AppDsFieldMapper appDsFieldMapper;
    @Autowired
    ServiceDataCache serviceDataCache;

    @Autowired
    ServiceModelFieldMapper serviceModelFieldMapper;

    @Autowired
    private ServiceModelMapper serviceModelMapper;

    @Autowired
    private ResourceSessionManager resourceSessionManager;
    @Autowired
    ApiLogDataCache apiDataCache;
    @Autowired
    ModelCodeMapper modelCodeMapper;
    @Autowired
    private ResourceServiceImpl resourceServiceImpl;

    @Override
    public boolean valid(RequestData requestData) throws ServiceAccessException {
        log.info("com.stackstech.dcp.apiserver.service.impl.DataServiceImpl.valid：" + "验证数据有效性开始");
        //  1. 数据有效性校验
        //  1.1. 必须参数有效性校验
        //  1.2. 用户有效性校验
        //  1.3. 服务有效性校验
        //  1.4. 用户、服务、令牌有效性校验
        //  1.5. 查询字段有效性校验
        //  1.6 验证连接资源
        // 1.7 数据源校验

        if (validData(requestData) && validParam(requestData) &&
                validUser(requestData) && validService(requestData) &&
                validAppDs(requestData) && validField(requestData)) {
            return false;
        }
        log.info("com.stackstech.dcp.apiserver.service.impl.DataServiceImpl.valid：" + "验证数据有效性结束");

        return true;
    }

    @Override
    public Object process(RequestData requestData) {

        Object obj = null;

        if (!(validData(requestData) && validParam(requestData) &&
                validUser(requestData) && validService(requestData) &&
                validAppDs(requestData) && validField(requestData))) {
            // AccessLogHelper.messageCode("参数验证失败", "400");
            return obj;
        }

        DataService dataService = dataServiceMapper.queryByPrimaryKey(requestData.getDataServiceId());
        ServiceModel serviceModel = serviceModelMapper.queryByPrimaryKey(dataService.getServiceModelId());


        //  先查缓存数据
        String jsonString = JSONObject.toJSONString(requestData.getData());
        JSONObject sortJsonString = JSONObject.parseObject(jsonString);

        String md5String = MD5Util.getUppercaseMD5(sortJsonString.toJSONString());
        String cachKey = requestData.getDataServiceId() + md5String;
        AccessLogHelper.dbStartTime();
        Map<String, Object> value = serviceDataCache.get(cachKey);
        AccessLogHelper.dbEndTime();
        if (value.size() == 0 || value == null) {
            DriverMetaData driverMetaData = (DriverMetaData) execute(requestData);

            if (driverMetaData.getDataType().equals(MetaDataTypeEnum.DATA) && driverMetaData.getData() != null && dataService.getTypeCode().equals(ModelTypesEnum.asset.toString())) {
                String jsonResult = JacksonUtil.beanToJson(driverMetaData.getData());
                if (serviceModel.getCacheDuration() > 0) {
                    serviceDataCache.put(cachKey, md5String, jsonResult);
                    serviceDataCache.expire(cachKey, serviceModel.getCacheDuration().longValue() * 1000);
                }
                obj = new DriverMetaData(MetaDataTypeEnum.DATA, jsonResult);
            } else if (driverMetaData.getDataType().equals(MetaDataTypeEnum.FILE) || driverMetaData.getDataType().equals(MetaDataTypeEnum.NOSQL)) {
                obj = driverMetaData;
            }
            //obj = driverMetaData;
        } else {
            obj = new DriverMetaData(MetaDataTypeEnum.DATA, value.get(md5String));
            log.info("缓存中的数据为：" + obj.toString());
            AccessLogHelper.messageCode("查询缓存数据成功", "200");
        }
        return obj;
    }

    @Override
    public Object execute(RequestData requestData) {
        return this.get(requestData);
    }


    /**
     * 1.2. 用户有效性校验
     *
     * @return
     */
    protected boolean validUser(RequestData requestData) {
        boolean flag = true;
        AppUser appUser = appUserMapper.queryByPrimaryKey(requestData.getAppId());

        if (appUser == null || !"enabled".equalsIgnoreCase(appUser.getStatusCode())) {
            AccessLogHelper.messageCode("此用户未激活", "400");
            flag = false;
        }
        log.info("用户有效性校验" + flag);
        return flag;
    }

    /**
     * 服务有效性校验
     *
     * @param requestData
     * @return
     */
    protected boolean validService(RequestData requestData) {
        boolean flag = true;
        DataService dataService = dataServiceMapper.queryByPrimaryKey(requestData.getDataServiceId());

        if (dataService == null || !"active".equals(dataService.getStatusCode())) {
            AccessLogHelper.messageCode("您请求的服务已禁用或服务不存在，请联系管理员", "400");
            flag = false;
        }
        log.info("服务有效性校验" + flag);
        return flag;
    }

    /**
     * 用户、服务、令牌有效性校验
     *
     * @param requestData
     * @return
     */
    protected boolean validAppDs(RequestData requestData) {
        boolean flag = true;
        AppDs ad = new AppDs();
        ad.setAppId(requestData.getAppId());
        ad.setToken(requestData.getToken());
        ad.setDataServiceId(requestData.getDataServiceId());

        AppDs appDss = appDsMapper.queryByToken(ad);
        if (appDss == null) {
            AccessLogHelper.messageCode("您没权限请求服务，请联系管理员授权", "400");
            flag = false;
        }
        log.info("用户、服务、令牌有效性校验:" + flag);
        return flag;
    }

    /**
     * 必须参数有效性校验
     *
     * @param requestData
     * @return
     */
    protected boolean validParam(RequestData requestData) {
        boolean flag = true;

        if (requestData.getData().getParams() == null) {
            return true;
        }
        //通过数据服务ID找数据模型ID
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", "active");
        map.put("id", requestData.getDataServiceId());
        DataService dataServices = dataServiceMapper.queryByPrimaryKey(requestData.getDataServiceId());

        if (dataServices != null) {
            String serviceModelId = dataServices.getServiceModelId();

            //通过模型Id获取该模型参数列表
            ServiceModelParam dmp = new ServiceModelParam();
            dmp.setServiceModelId(serviceModelId);
            //dmp.setIsRequired('Y');
            List<ServiceModelParam> pageModelParams = serviceModelParamMapper.queryAll(dmp);

            List<ServiceFilter> fields = requestData.getData().getParams(); //请求参数列表

            flag = compareParam(pageModelParams, fields, requestData);
        }
        log.info("必填参数有效性校验:" + flag);
        return flag;
    }

    /**
     * 数据有效性校验
     *
     * @param requestData
     * @return
     */
    protected boolean validData(RequestData requestData) {
        boolean flag = true;

        if (requestData.getData() == null) {
            requestData.setData(new ServiceParam());
            return true;
        }
        //数据不能为空
        List<ServiceFilter> filters = requestData.getData().getParams();

        if (filters != null) {
            for (ServiceFilter sf : filters) {
                if (sf.getValue() == "" || sf.getValue() == NULL) {

                    AccessLogHelper.messageCode("过滤字段" + sf.getName() + "的值不能为空", "400");
                    flag = false;
                }
            }
        }

        log.info("数据有效性校验:" + flag);
        return flag;
    }

    /**
     * 查询字段有效性校验
     *
     * @param requestData
     * @return
     */
    protected boolean validField(RequestData requestData) {
        boolean flag = true;

        //通过数据服务ID找所有授权字段
        AppDsField adf = new AppDsField();
        adf.setAppId(requestData.getAppId());
        adf.setDataServiceId(requestData.getDataServiceId());
        List<AppDsField> appDsFields = appDsFieldMapper.queryAll(adf);

        List<String> fieldNameList = new ArrayList<>();

        for (AppDsField name : appDsFields) {
            fieldNameList.add(name.getFieldName());
        }
        List<ServiceField> fields = requestData.getData().getFields();

        if (fields != null) {
            for (ServiceField sf : fields) {

                log.info("====" + fieldNameList.contains(sf.getFieldName()));

                log.info(fieldNameList.toString() + "----" + sf);

                if (!fieldNameList.contains(sf.getFieldName())) {
                    log.info("不包含属性" + sf.getFieldName());
                    return false;
                }
            }
        }

        log.info("查询字段有效性校验状态:" + flag);
        return flag;
    }

    protected static boolean compareParam(List<ServiceModelParam> dmpList, List<ServiceFilter> sfList, RequestData requestData) {
        boolean flag = true;
        List<String> filterNameList = new ArrayList<>();
        List<String> modelParamList = new ArrayList<>();

        List<ServiceFilter> newFilterList = new ArrayList<>();

        for (ServiceFilter name : sfList) {
            filterNameList.add(name.getName());
        }
        for (ServiceModelParam param : dmpList) {
            modelParamList.add(param.getParamName());
        }
        //变量参数列表是否存在、是否所有必填字段都满足
        for (ServiceModelParam dmps : dmpList) {

            //验证必填字段是否存在
            if (("y".equals(dmps.getIsRequired()))) {
                flag = filterNameList.contains(dmps.getParamName());
                if (!flag) {
                    AccessLogHelper.messageCode(dmps.getParamName() + "是必填字段", "400");
                    return false;
                }
            }

            for (ServiceFilter sf : sfList) {
                if (!modelParamList.contains(sf.getName())) { //验证参数字段是否正常
                    AccessLogHelper.messageCode(sf.getName() + " 属性字段不存在,请核对:", "400");
                    log.info(sf + "此字段不存在");
                    return false;
                } else {  //如果参数存在，获取参数的类型
                    if (sf.getName().equals(dmps.getParamName())) {
                        ServiceFilter newServiceFilter = new ServiceFilter();
                        newServiceFilter.setName(sf.getName());
                        newServiceFilter.setType(dmps.getParamType());
                        newServiceFilter.setValue(sf.getValue());
                        newServiceFilter.setField(dmps.getFieldName());

                        newFilterList.add(newServiceFilter);
                    }
                }
            }


        }
        requestData.getData().setParams(newFilterList);
        return flag;

    }


    /**
     * 获取字段的类型
     *
     * @param requestData
     * @return
     */
    protected RequestData getFieldType(RequestData requestData) {

        //通过数据服务ID找所有授权字段
        AppDsField adf = new AppDsField();
        adf.setAppId(requestData.getAppId());
        adf.setDataServiceId(requestData.getDataServiceId());
        List<AppDsField> appDsFields = appDsFieldMapper.queryAll(adf);

        List<ServiceField> fields = requestData.getData().getFields();

        List<ServiceField> newFieldsList = new ArrayList<>();

        for (ServiceField sf : fields) {

            for (AppDsField appDsField : appDsFields) {
                ServiceModelField serviceModelField = serviceModelFieldMapper.queryByPrimaryKey(Long.valueOf(appDsField.getFieldId()));

                if (serviceModelField.getFieldName().equals(sf.getFieldName())) {
                    ServiceField newServiceField = new ServiceField();
                    newServiceField.setType(serviceModelField.getDataType());
                    newServiceField.setFieldName(serviceModelField.getFieldName());

                    newFieldsList.add(newServiceField);
                }
            }
        }

        requestData.getData().setFields(newFieldsList);

        return requestData;
    }

    @Override
    public Object get(RequestData requestData) {

        DataService dataService = dataServiceMapper.queryByPrimaryKey(requestData.getDataServiceId());

        Object obj = null;

        if (dataService == null) {
            return "";
        }
        //获取服务模型
        ServiceModel serviceModel = serviceModelMapper.queryByPrimaryKey(dataService.getServiceModelId());

        AccessLogHelper.resouceId(serviceModel.getServiceSourceId());
        if (serviceModel.getTypeCode().equals(ModelTypesEnum.ability.toString())) {
            //获取能力模型编码
            ModelCode modelCode = modelCodeMapper.queryByPrimaryKey(Long.valueOf(serviceModel.getTopicId()));
            serviceModel.setTopicId(modelCode.getCode());
        }

        ResourceSessionFactory resourceSessionFactory = resourceSessionManager.getResource(serviceModel.getServiceSourceId());

        if (resourceSessionFactory == null) {
            throw ServiceAccessException.create(400, "数据源未初始化，请稍后在试");
        }

        ResourceSession resourceSession = resourceSessionFactory.getSession();
        validDriverResource(resourceSession, requestData);

        //DB执行开始时间
        AccessLogHelper.dbStartTime();
        DriverMetaData executeResult = null;
        try {
            executeResult = new DriverExecutorAdapter(dataService.getOperateType())
                    .execute(resourceSession, DriverDataUtil.parse(dataService, serviceModel, requestData));
        } catch (UncategorizedSQLException e) {
            log.error(e.getMessage(), e);
            AccessLogHelper.messageCode("数据源查询超时", "400");
            throw ServiceAccessException.create(400, "数据源查询超时");
        } catch (DataAccessException e) {
            log.error(e.getMessage(), e);
            AccessLogHelper.messageCode("数据源访问异常，请稍后重试", "400");
            throw ServiceAccessException.create(400, "数据源访问异常，请稍后重试");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            AccessLogHelper.messageCode("请求参数无法解析，请确认请求参数", "400");
            throw ServiceAccessException.create(400, "请求参数无法解析，请确认请求参数");
        }

        //DB执行结束时间
        AccessLogHelper.dbEndTime();
        resourceServiceImpl.saveActiveResource(serviceModel.getServiceSourceId(), resourceSession.getPoolInfo().getNumActive());

        if (executeResult == null) {
            AccessLogHelper.messageCode("解析出问题了，请确认请求参数", "400");
            throw ServiceAccessException.create(400, "解析出问题了，请确认请求参数");
        } else {
            try {
                DriverMetaData driverMetaData = executeResult;
                if (driverMetaData.getDataType().equals(MetaDataTypeEnum.DATA)) {
                    List<Map<String, Object>> listMap = (List<Map<String, Object>>) driverMetaData.getData();
                    AccessLogHelper.rowSize(listMap.size(), listMap.toString().getBytes().length);
                }
                AccessLogHelper.messageCode("请求数据源成功", "200");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                AccessLogHelper.messageCode("结果无法解析,请确认请求参数", "400");
                throw ServiceAccessException.create(400, "结果无法解析,请确认请求参数");
            }

        }

        return executeResult;
    }

    @Override
    public void push(RequestData requestData) {

    }


    /**
     * 验证驱动资源
     *
     * @param resourceSession
     * @param requestData
     */
    protected void validDriverResource(ResourceSession resourceSession, RequestData requestData) {
        if (resourceSession == null) {
            AccessLogHelper.messageCode("数据源未初始化，请稍后在试", "400");
            throw ServiceAccessException.create(400, "数据源未初始化，请稍后在试");
        }

        int maxTotal = resourceSession.getPoolInfo().getMaxTotal();
        int numActive = resourceSession.getPoolInfo().getNumActive();
        AccessLogHelper.resourceNum(maxTotal, maxTotal - numActive);
        if ((maxTotal - numActive) <= 0) {
            AccessLogHelper.messageCode("连接数量不够，请稍后在试", "400");
            throw ServiceAccessException.create(400, "连接数量不够，请稍后在试");
        }
    }

}
