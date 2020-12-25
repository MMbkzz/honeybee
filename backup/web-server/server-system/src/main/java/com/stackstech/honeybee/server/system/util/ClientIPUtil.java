package com.stackstech.honeybee.server.system.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * SpringCloud IP工具类
 */
public class ClientIPUtil {

    private static final Logger logger = LoggerFactory.getLogger(ClientIPUtil.class);

    /**
     * @param ipAddress
     * @return
     */
    public static long ipToLong(String ipAddress) {
        long result = 0;
        String[] ipAddressInArray = ipAddress.split("\\.");
        for (int i = 3; i >= 0; i--) {
            long ip = Long.parseLong(ipAddressInArray[3 - i]);
            result |= ip << (i * 8);
        }
        return result;
    }

    /**
     * @param ip
     * @return
     */
    public static String longToIp(long ip) {
        StringBuilder result = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            result.insert(0, (ip & 0xff));
            if (i < 3) {
                result.insert(0, '.');
            }
            ip = ip >> 8;
        }
        return result.toString();
    }

    /**
     * @param ip
     * @return
     */
    public static String longToIp2(long ip) {
        return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
    }

    /**
     * 获取当前机器的IP数组
     *
     * @return
     */
    public static List<Long> getIpAddress() {
        List<Long> ipList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = null;
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresss = ni.getInetAddresses();
                while (addresss.hasMoreElements()) {
                    InetAddress nextElement = addresss.nextElement();
                    String hostAddress = nextElement.getHostAddress();
                    ipList.add(ipToLong(hostAddress));
                }
            }
            return ipList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取SpringCloud实例IP<最小>
     *
     * @param serviceUrl
     * @return
     */
    public static Long getInstanceIP(List<URI> serviceUrl) {
        try {
            int size = serviceUrl.size();
            if (size == 0) {
                return null;
            }

            Long[] longHost = new Long[size];
            for (int i = 0; i < serviceUrl.size(); i++) {
                String host = serviceUrl.get(i).getHost();
                longHost[i] = ClientIPUtil.ipToLong(host);
            }
            Arrays.sort(longHost);
            return longHost[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * IP地址比较
     *
     * @param uris
     * @return
     */
    public static boolean ipCompare(List<URI> uris) {

        Long serveIP = getInstanceIP(uris);
        if (serveIP == null) {
            return false;
        }

        List<Long> ipList = getIpAddress();
        if (ipList == null || ipList.size() == 0) {
            return false;
        }

        return ipList.contains(serveIP);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        long result = ipToLong("192.168.11.126");
        System.out.println("long转换为IP的结果： " + longToIp(result));
        System.err.println("result IP转换为long的结果： " + result);
        long result2 = ipToLong("192.168.11.217");
        System.err.println("result2IP转换为long的结果： " + result2);
        System.out.println("long转换为IP的结果： " + longToIp(result));
        System.out.println("long转换为IP的结果： " + longToIp2(result));
    }
}
