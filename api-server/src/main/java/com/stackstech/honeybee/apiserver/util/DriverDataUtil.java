package com.stackstech.honeybee.apiserver.util;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stackstech.honeybee.apiserver.model.RequestData;
import com.stackstech.honeybee.apiserver.model.ServiceField;
import com.stackstech.honeybee.apiserver.model.ServiceFilter;
import com.stackstech.honeybee.connector.core.entity.DriverApiModel;
import com.stackstech.honeybee.connector.core.entity.DriverDataModel;
import com.stackstech.honeybee.connector.core.entity.DriverMessageModel;
import com.stackstech.honeybee.connector.core.entity.DriverModel;
import com.stackstech.honeybee.connector.core.util.SqlTemplateUtil;
import com.stackstech.honeybee.core.enums.ModelTypesEnum;
import com.stackstech.honeybee.core.util.JacksonUtil;
import com.stackstech.honeybee.server.dataasset.model.ServiceModel;
import com.stackstech.honeybee.server.dataservice.model.DataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DriverDataUtil {

    public static DriverModel parse(DataService dataService, ServiceModel serviceModel, RequestData requestData) {
        DriverModel driverModel = null;
        if (ModelTypesEnum.asset.toString().equals(dataService.getTypeCode())) {
            driverModel = parseData(dataService, serviceModel, requestData);
        } else if (ModelTypesEnum.ability.toString().equals(dataService.getTypeCode())) {
            if ("MQ".equals(serviceModel.getTopicId())) {
                driverModel = parseMessage(dataService, serviceModel, requestData);
            } else if ("image recognition".equals(serviceModel.getTopicId())) {
                driverModel = parseData(dataService, serviceModel, requestData);
            } else {
                driverModel = parseApi(dataService, serviceModel, requestData);
            }
        }

        return driverModel;
    }

    public static DriverDataModel parseData(DataService dataService, ServiceModel serviceModel, RequestData requestData) {

        List<ServiceFilter> filters = requestData.getData().getParams();
        List<ServiceField> fields = requestData.getData().getFields();

        Map<String, Object> data = Maps.newHashMap();

        List<Map<String, Object>> listValues = Lists.newArrayList();

        if (filters != null) {

            filters.forEach(sf -> {
                Map<String, Object> paramsList = Maps.newHashMap();
                paramsList.put("name", sf.getName().trim());
                paramsList.put("value", sf.getValue().toString().trim());
                paramsList.put("type", sf.getType().trim());
                paramsList.put("field", sf.getField().trim());
                listValues.add(paramsList);
            });

            data.put("filters", SqlTemplateUtil.parseValues(listValues)); //根据类型解析生成sql的值.
        }

        if (fields != null) {
            Map<String, Object> fieldList = Maps.newHashMap();
            for (ServiceField sf : fields) {
                fieldList.put(sf.getFieldName(), sf.getType());
            }
            data.put("fields", fieldList);
        }

        data.put("special", listValues); //目前只给redis特殊使用

        return new DriverDataModel(dataService.getOperateType(), dataService.getExpression(), data);
    }

    public static DriverMessageModel parseMessage(DataService dataService, ServiceModel serviceModel, RequestData requestData) {

        //String message = requestData.getMessage();
        Map<String, Object> params = Maps.newHashMap();

        List<ServiceFilter> filters = requestData.getData().getParams();

        if (filters != null) {
            for (ServiceFilter sf : filters) {
                params.put(serviceModel.getExpression(), sf.getValue()); //topic  ,message
            }
        }

        return new DriverMessageModel(dataService.getOperateType(), serviceModel.getExpression(), params, dataService.getServiceModelId());
    }

    public static DriverApiModel parseApi(DataService dataService, ServiceModel serviceModel, RequestData requestData) {

        List<ServiceFilter> filters = requestData.getData().getParams();
        List<Map<String, Object>> listValues = new ArrayList<>();

        if (filters != null && filters.size() != 0 && "get".equals(serviceModel.getRequestMethod().toLowerCase())) {

            StringBuffer getParamBuffer = new StringBuffer();
            for (ServiceFilter sf : filters) {
                getParamBuffer.append(sf.getName() + "=" + sf.getValue() + "&");
            }
            String URL = null;// serviceModel.getExpression() + getParamBuffer.substring(0,getParamBuffer.length()-1);
            if (serviceModel.getExpression().contains("?")) {
                URL = serviceModel.getExpression() + "&" + getParamBuffer.substring(0, getParamBuffer.length() - 1);
            } else {
                URL = serviceModel.getExpression() + "?" + getParamBuffer.substring(0, getParamBuffer.length() - 1);
            }
            return new DriverApiModel(dataService.getOperateType(), URL, serviceModel.getRequestMethod(), null);

        } else if ("post".equals(serviceModel.getRequestMethod().toLowerCase())) {
            if (filters != null && filters.size() != 0) {
                filters.forEach(sf -> {
                    Map<String, Object> paramsList = Maps.newHashMap();
                    paramsList.put("name", sf.getName().trim());
                    if ("json".equals(sf.getType().trim().toLowerCase())) {
                        paramsList.put("type", sf.getType().trim());
                        paramsList.put("value", JacksonUtil.beanToJson(sf.getValue()));
                    } else {
                        paramsList.put("value", sf.getValue());
                    }
                    paramsList.put("type", sf.getType().trim());
                    paramsList.put("field", sf.getField().trim());
                    listValues.add(paramsList);
                });
            }

            return new DriverApiModel(dataService.getOperateType(), serviceModel.getExpression(), serviceModel.getRequestMethod(), listValues);
        } else {
            if (filters != null && filters.size() != 0) {
                filters.forEach(sf -> {
                    Map<String, Object> paramsList = Maps.newHashMap();
                    paramsList.put("name", sf.getName().trim());
                    paramsList.put("type", sf.getType().trim());
                    paramsList.put("value", sf.getValue().toString().trim());
                    paramsList.put("field", sf.getField().trim());
                    listValues.add(paramsList);
                });
            }

        }

        return new DriverApiModel(dataService.getOperateType(), serviceModel.getExpression(), serviceModel.getRequestMethod(), listValues);
    }

}
