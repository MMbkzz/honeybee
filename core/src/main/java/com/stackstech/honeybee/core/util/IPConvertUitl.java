package com.stackstech.honeybee.core.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class IPConvertUitl {

    /**
     * 获取用户的IP地址
     */
    public static String getIP(HttpServletRequest httpServletRequest) {
        String unknownStr = "unknown";
        String ip = null;
        ip = httpServletRequest.getHeader("Proxy-Client-IP");

        if (StringUtils.isEmpty(ip) || unknownStr.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || unknownStr.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || unknownStr.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || unknownStr.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("X-Forwarded-For");
        }

        if (StringUtils.isEmpty(ip) || unknownStr.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("X-Forwarded-Host");
        }
        if (StringUtils.isEmpty(ip) || unknownStr.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getRemoteAddr();
        }
        if (StringUtils.isNotEmpty(ip) && (ip.length() >= 15)) {
            ip = StringUtils.substringBefore(ip, ",");
        }
        return ip;
    }

    /**
     * 将255.255.255.255 形式的IP地址转换成long型,传入的IP格式为"100.010.000.111"或者"1.10.0.111",不能包含字母等
     */
    public static long ipToLong(String strIP) {
        if (strIP == null || "".equals(strIP)) {
            return 0;
        }
        long[] ip = new long[4];
        int position1 = strIP.indexOf(".");
        int position2 = strIP.indexOf(".", position1 + 1);
        int position3 = strIP.indexOf(".", position2 + 1);
        ip[0] = Long.parseLong(strIP.substring(0, position1));
        ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIP.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }
}
