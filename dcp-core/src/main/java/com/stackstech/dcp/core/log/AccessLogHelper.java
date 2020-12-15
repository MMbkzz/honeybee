package com.stackstech.dcp.core.log;

import com.stackstech.dcp.core.util.DateUtils;

import java.util.Date;

/**
 *
 */
public class AccessLogHelper {

    public static void accessStart(String appId, String dataServiceId, String requestParams, String instanceHost, int instancePort, String clientIp) {
        AccessLogInfo accessLogInfo = new AccessLogInfo();
        accessLogInfo.setAccessStartTime(DateUtils.getDefaultCurrentTimeStamp(new Date()));
        accessLogInfo.setAppId(appId);
        accessLogInfo.setDataServiceId(dataServiceId);
        accessLogInfo.setRequestParams(requestParams);
        accessLogInfo.setInstanceHost(instanceHost);
        accessLogInfo.setInstancePort(instancePort);
        accessLogInfo.setClientHost(clientIp);
        LoggerHealper.set(accessLogInfo);
    }

    public static void dbStartTime() {
        AccessLogInfo localLog = (AccessLogInfo) LoggerHealper.getLocalLog().getMessage();
        localLog.setDbStartTime(DateUtils.getDefaultCurrentTimeStamp(new Date()));
    }

    public static void dbEndTime() {
        AccessLogInfo localLog = (AccessLogInfo) LoggerHealper.getLocalLog().getMessage();
        localLog.setDbEndTime(DateUtils.getDefaultCurrentTimeStamp(new Date()));
    }

    public static void messageCode(String message, String returnCode) {
        AccessLogInfo localLog = (AccessLogInfo) LoggerHealper.getLocalLog().getMessage();
        localLog.setMessage(message);
        localLog.setReturnCode(returnCode);
    }

    public static void resourceNum(int resourceNum, int freeResourceNUm) {
        AccessLogInfo localLog = (AccessLogInfo) LoggerHealper.getLocalLog().getMessage();
        localLog.setResourceNum(resourceNum);
        localLog.setFreeResourceNum(freeResourceNUm);
    }

    public static void resouceId(String resouceId) {
        AccessLogInfo localLog = (AccessLogInfo) LoggerHealper.getLocalLog().getMessage();
        localLog.setServiceSourceId(resouceId);
    }

    public static void accessEnd(String message, String returnCode, int returnRow, int returnSize) {
        AccessLogInfo localLog = (AccessLogInfo) LoggerHealper.getLocalLog().getMessage();

        localLog.setMessage(message);
        localLog.setReturnCode(returnCode);
        localLog.setReturnSize(returnSize);
        localLog.setReturnRow(returnRow);
        localLog.setAccessEndTime(DateUtils.getDefaultCurrentTimeStamp(new Date()));
    }

    public static void rowSize(int returnRow, int returnSize) {
        AccessLogInfo localLog = (AccessLogInfo) LoggerHealper.getLocalLog().getMessage();
        localLog.setReturnSize(returnSize);
        localLog.setReturnRow(returnRow);
    }

    public static void message(String message) {
        AccessLogInfo localLog = (AccessLogInfo) LoggerHealper.getLocalLog().getMessage();
        localLog.setMessage(message);
    }

    public static void accessEndTime() {
        AccessLogInfo localLog = (AccessLogInfo) LoggerHealper.getLocalLog().getMessage();
        localLog.setAccessEndTime(DateUtils.getDefaultCurrentTimeStamp(new Date()));
    }
}
