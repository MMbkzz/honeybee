package com.stackstech.honeybee.apiserver.service.impl;


import com.stackstech.honeybee.apiserver.model.RequestData;
import com.stackstech.honeybee.apiserver.service.AccessLogServices;
import com.stackstech.honeybee.core.enums.ApiLogEnum;
import com.stackstech.honeybee.core.util.DateUtils;
import com.stackstech.honeybee.server.dataservice.dao.AccessLogMapper;
import com.stackstech.honeybee.server.dataservice.model.AccessLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * API日志访问实现类
 */
@Service
public class AccessLogServicesImpl implements AccessLogServices {

    private final Logger log = LoggerFactory.getLogger(AccessLogServicesImpl.class);
    @Autowired
    AccessLogMapper accessLogMapper;

    @Override
    public void save(Map<String, String> mapInfo, RequestData requestData) {
        AccessLog al = new AccessLog();
        log.info("API日志记录信息：" + mapInfo.toString());
        al.setAccessStartTime(DateUtils.getDefaultCurrentTimeStamp(mapInfo.get(ApiLogEnum.ACCESS_START_TIME.toString())));
        al.setAccessEndTime(DateUtils.getDefaultCurrentTimeStamp(mapInfo.get(ApiLogEnum.ACCESS_END_TIME.toString())));

        String sdbStartTime = mapInfo.get(ApiLogEnum.DB_START_TIME.toString());
        if (sdbStartTime != null) {
            al.setDbStartTime(DateUtils.getDefaultCurrentTimeStamp(mapInfo.get(ApiLogEnum.DB_START_TIME.toString())));
        }
        String dbEndTime = mapInfo.get(ApiLogEnum.DB_END_TIME.toString());
        if (dbEndTime != null) {
            al.setDbEndTime(DateUtils.getDefaultCurrentTimeStamp(mapInfo.get(ApiLogEnum.DB_END_TIME.toString())));
        }
        al.setAppId(requestData.getAppId());
        al.setClientHost(mapInfo.get(ApiLogEnum.CLIENT_HOST.toString()));
        al.setDataServiceId(requestData.getDataServiceId());
        al.setMessage(mapInfo.get(ApiLogEnum.MESSAGE.toString()));

        al.setReturnCode(mapInfo.get(ApiLogEnum.RETURN_CODE.toString()));

        al.setRequestParams(mapInfo.get(ApiLogEnum.REQUEST_PARAMS.toString()));
        al.setInstanceHost(mapInfo.get(ApiLogEnum.INSTANCE_HOST.toString()));
        al.setInstancePort(Integer.valueOf(mapInfo.get(ApiLogEnum.INSTANCE_PORT.toString())));

        String returnRow = mapInfo.get(ApiLogEnum.RETURN_ROW.toString());
        if (returnRow != null) {
            al.setReturnRow(Integer.valueOf(mapInfo.get(ApiLogEnum.RETURN_ROW.toString())));
        }

        String returnSize = mapInfo.get(ApiLogEnum.RETURN_SIZE.toString());
        if (returnSize != null) {
            al.setReturnSize(Integer.valueOf(mapInfo.get(ApiLogEnum.RETURN_SIZE.toString())));
        }

        accessLogMapper.insert(al);
    }

    @Override
    public void insertLogs() {

    }
}
