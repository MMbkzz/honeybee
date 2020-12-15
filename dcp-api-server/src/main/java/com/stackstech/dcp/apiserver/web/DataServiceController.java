package com.stackstech.dcp.apiserver.web;

import com.stackstech.dcp.apiserver.api.ApiUrls;
import com.stackstech.dcp.apiserver.conf.ServerConfig;
import com.stackstech.dcp.apiserver.exception.ServiceAccessException;
import com.stackstech.dcp.apiserver.model.RequestData;
import com.stackstech.dcp.apiserver.model.ServiceFilter;
import com.stackstech.dcp.apiserver.model.ServiceParam;
import com.stackstech.dcp.apiserver.service.AccessLogServices;
import com.stackstech.dcp.apiserver.service.ApiDataService;
import com.stackstech.dcp.connector.core.entity.DriverMetaData;
import com.stackstech.dcp.connector.core.enums.MetaDataTypeEnum;
import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.log.AccessLogHelper;
import com.stackstech.dcp.core.log.AccessLogInfo;
import com.stackstech.dcp.core.log.LoggerHealper;
import com.stackstech.dcp.core.util.FileUtil;
import com.stackstech.dcp.core.util.IPConvertUitl;
import com.stackstech.dcp.core.util.JacksonUtil;
import com.stackstech.dcp.core.util.TokenUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.POST;

@RestController
public class DataServiceController {

    private final Logger log = LoggerFactory.getLogger(DataServiceController.class);

    @Autowired
    private ApiDataService dataService;

    @Autowired
    AccessLogServices accessLogServices;

    @Autowired
    private ServerConfig serverConfig;

    @RequestMapping(value = ApiUrls.OPEN_API)
    public DeferredResult<ResponseEntity<?>> process(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        final DeferredResult deferredResult = new DeferredResult();

        //开始访问时间记录
        RequestData requestData = parseRequest(request);

        if (requestData.getData() == null) {
            AccessLogHelper.accessStart(requestData.getAppId(), requestData.getDataServiceId(), "", serverConfig.getHost(), serverConfig.getPort(), IPConvertUitl.getIP(request) + "-" + TokenUtil.getHost(request));
        } else {
            AccessLogHelper.accessStart(requestData.getAppId(), requestData.getDataServiceId(), requestData.getData().toString(), serverConfig.getHost(), serverConfig.getPort(), IPConvertUitl.getIP(request) + "-" + TokenUtil.getHost(request));
        }

        Object process = null;
        AccessLogInfo accessLog = null;
        try {
            process = dataService.process(requestData);
            DriverMetaData processResult = null;

            AccessLogHelper.accessEndTime();
            accessLog = (AccessLogInfo) LoggerHealper.getLocalLog().getMessage();
            AccessLogHelper.messageCode(accessLog.getMessage(), accessLog.getReturnCode());
            accessLogServices.insertLogs();

            if (process == null) {
                deferredResult.setErrorResult(ResponseError.create(Integer.parseInt(accessLog.getReturnCode()), accessLog.getMessage()));
                //return ResponseError.create(Integer.valueOf(accessLog.getReturnCode()), accessLog.getMessage());
            } else {
                processResult = (DriverMetaData) process;

                String dataType = request.getHeader("data-type") != null ? String.valueOf(request.getHeader("data-type")) : null;
                //文件操作处理
                if (dataType != null && "file".equalsIgnoreCase(dataType.split("/")[0]) && processResult.getData() != null) {
                    try {
                        FileUtil.download(processResult.getData().toString(), request, resp, dataType.split("/")[1]);
                        deferredResult.setResult(ResponseOk.create("下载成功"));
                        return deferredResult;
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
                //保存请求访问日志至DB
                if (processResult.getData() != null) {
                    if (dataType != null && "data".equals(dataType)) {
                        //data类型 json格式返回
                        deferredResult.setResult(ResponseOk.create("result", processResult.getData().toString()));
                    } else {
                        if (MetaDataTypeEnum.DATA.equals(processResult.getDataType())) {
                            //data类型 非json格式返回
                            List<Map> reverseResult = JacksonUtil.parseJsonList(processResult.getData().toString(), Map.class);
                            deferredResult.setResult(ResponseOk.create("result", reverseResult.toString()));
                        } else {
                            //nosql+ file 非json格式返回
                            deferredResult.setResult(ResponseOk.create("result", processResult.getData().toString()));
                        }
                    }
                } else {
                    deferredResult.setErrorResult(ResponseError.create(200, "结果为空"));
                }
            }
        } catch (ServiceAccessException e) {
            log.error("服务验证失败：", e);
            AccessLogHelper.accessEndTime();
            accessLog = (AccessLogInfo) LoggerHealper.getLocalLog().getMessage();
            AccessLogHelper.messageCode(e.getMessage(), String.valueOf(e.getCode()));
            accessLogServices.insertLogs();
            deferredResult.setErrorResult(ResponseError.create(e.getCode(), e.getMessage()));
        }
        return deferredResult;
    }

    public static RequestData parseRequest(HttpServletRequest request) throws IOException {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = String.valueOf(request.getParameter("token"));
        }

        String appid = request.getHeader("appid");
        if (StringUtils.isEmpty(appid)) {
            appid = String.valueOf(request.getParameter("appid"));
        }

        String dataServiceId = request.getHeader("dataServiceId");
        if (StringUtils.isEmpty(dataServiceId)) {
            dataServiceId = String.valueOf(request.getParameter("dataServiceId"));
        }

        RequestData data = new RequestData();
        data.setAppId(appid);
        data.setToken(token);
        data.setDataServiceId(dataServiceId);

        switch (request.getMethod()) {
            case POST:
                byte[] bytes = IOUtils.toByteArray(request.getInputStream());
                String params = new String(bytes, request.getCharacterEncoding());

                RequestData d = JacksonUtil.jsonToBean(params, RequestData.class);
                BeanUtils.copyProperties(d, data);
                data.setAppId(appid);
                data.setToken(token);
                break;

            case GET:
                ServiceParam param = new ServiceParam();
                data.setData(param);

                List<ServiceFilter> filters = new ArrayList<>();
                param.setParams(filters);

                String[] args = {"appid", "token", "dataServiceId"};
                Enumeration<String> pNames = request.getParameterNames();
                while (null != pNames && pNames.hasMoreElements()) {
                    String key = pNames.nextElement();
                    if (ArrayUtils.contains(args, key)) {
                        continue;
                    }
                    String value = request.getParameter(key);

                    ServiceFilter filter = new ServiceFilter();
                    filter.setName(key);
                    filter.setValue(value);

                    filters.add(filter);
                }

                break;
        }
        request.getParameterNames();

        return data;
    }

}
