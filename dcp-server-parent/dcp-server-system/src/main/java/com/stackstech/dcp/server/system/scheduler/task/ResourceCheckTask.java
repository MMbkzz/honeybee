package com.stackstech.dcp.server.system.scheduler.task;

import com.stackstech.dcp.core.conf.ApplicationConfig;
import com.stackstech.dcp.core.enums.ModelTypesEnum;
import com.stackstech.dcp.core.enums.RequestTypeEnum;
import com.stackstech.dcp.core.util.HttpUtil;
import com.stackstech.dcp.core.util.JacksonUtil;
import com.stackstech.dcp.server.dataasset.dao.ServiceModelParamMapper;
import com.stackstech.dcp.server.dataasset.model.ServiceModelParam;
import com.stackstech.dcp.server.dataservice.dao.AppDsFieldMapper;
import com.stackstech.dcp.server.dataservice.dao.AppDsMapper;
import com.stackstech.dcp.server.dataservice.dao.DataServiceMapper;
import com.stackstech.dcp.server.dataservice.model.AppDs;
import com.stackstech.dcp.server.dataservice.model.AppDsField;
import com.stackstech.dcp.server.dataservice.model.DataService;
import com.stackstech.dcp.server.platform.dao.MessageMapper;
import com.stackstech.dcp.server.platform.model.Message;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源检查Task
 */
@Component
public class ResourceCheckTask {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppDsMapper appDsMapper;
    @Autowired
    private AppDsFieldMapper appDsFieldMapper;
    @Autowired
    private DataServiceMapper dataServiceMapper;
    @Autowired
    private ServiceModelParamMapper paramMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private ApplicationConfig applicationConfig;


    public void run() {

        logger.info("ResourceCheckTask start!!! 数据源检查调度开始 >>> " + System.currentTimeMillis());

        Message message = new Message();
        message.setMessageLevel("warning");
        message.setTitle("数据源检查告警");

        //1. 获取授权的服务
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("statusCode", "active");
        List<DataService> dataServices = dataServiceMapper.queryAll(map);
        if (dataServices == null || dataServices.size() == 0) {
            logger.warn("ResourceCheckTask is running!!! 没有正在使用中的服务");
            message.setContext("没有正在使用中的服务");
            messageMapper.insert(message);
            return;
        }

        String url = applicationConfig.getApi();

        for (DataService dataService : dataServices) {

            String dataServiceId = dataService.getId();
            String serviceModelId = dataService.getServiceModelId();
            String requestMethod = dataService.getRequestMethod();

            try {
                List<AppDs> appDss = appDsMapper.queryByServiceId(dataServiceId);
                if (appDss == null || appDss.size() == 0) {
                    continue;
                }

                for (AppDs appDs : appDss) {

                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("dataServiceId", dataServiceId);

                    Map<String, String> headers = new HashMap<>();
                    headers.put("token", appDs.getToken());
                    headers.put("appid", appDs.getAppId());
                    if (dataService.getTypeCode() != null && ModelTypesEnum.asset.toString().equals(dataService.getTypeCode())) {
                        headers.put("data-type", "data");
                    }

                    Map<String, Object> datas = new HashMap<>();

                    //2. 获取授权字段和参数列表
                    List<Map<String, Object>> fieldsMap = new ArrayList<>();
                    List<Map<String, Object>> paramsMap = new ArrayList<>();
                    List<AppDsField> dsFields = appDsFieldMapper.queryByServiceIdAndAppId(dataServiceId, appDs.getAppId());
                    if (dsFields != null && dsFields.size() > 0) {
                        for (AppDsField field : dsFields) {
                            Map<String, Object> fieldMap = new HashMap<>();
                            fieldMap.put("fieldName", field.getFieldName());
                            fieldsMap.add(fieldMap);
                        }
                    }
                    List<ServiceModelParam> modelParams = paramMapper.queryByServiceId(serviceModelId);
                    if (modelParams != null && modelParams.size() > 0) {
                        for (ServiceModelParam param : modelParams) {
                            if (StringUtils.isNotBlank(param.getDefaultValue())) {
                                Map<String, Object> paramMap = new HashMap<>();
                                paramMap.put("name", param.getParamName());
                                paramMap.put("value", param.getDefaultValue());
                                paramsMap.add(paramMap);
                            }
                        }
                    }

                    datas.put("fields", fieldsMap);
                    datas.put("params", paramsMap);

                    //3.发送请求
                    String response = null;
                    if (requestMethod != null && RequestTypeEnum.POST.toString().equalsIgnoreCase(requestMethod)) {
                        parameters.put("data", datas);
                        response = HttpUtil.doPost(url, headers, JacksonUtil.beanToJson(parameters));
                    } else if (requestMethod != null && RequestTypeEnum.GET.toString().equalsIgnoreCase(requestMethod)) {
                        parameters.put("data", datas);
                        response = HttpUtil.doGet(url, headers, parameters);
                    }

                    if (response == null) {
                        logger.warn("ResourceCheckTask is running!!!  服务: " + dataServiceId + " >>> 返回值为空");
                    } else {
                        if (StringUtils.contains(response, "\"code\":400")) {
                            logger.warn("ResourceCheckTask is running!!!  服务: " + dataServiceId + " 服务访问异常 >>> appId : " + appDs.getAppId() + " >>> " + response);
                            message.setContext("服务: " + dataServiceId + " 服务访问异常 >>> appId : " + appDs.getAppId() + " >>> " + response);
                            messageMapper.insert(message);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("ResourceCheckTask is running!!! 数据源检查告警 >>> 服务: " + dataServiceId + " >>> e :" + e.toString());
                message.setContext("服务: " + dataServiceId + " >>> 程序运行异常 >>> e :" + e.toString());
                messageMapper.insert(message);
                e.printStackTrace();
            }
        }
        logger.info("ResourceCheckTask end!!! 数据源检查调度结束 >>> " + System.currentTimeMillis());
    }

}
