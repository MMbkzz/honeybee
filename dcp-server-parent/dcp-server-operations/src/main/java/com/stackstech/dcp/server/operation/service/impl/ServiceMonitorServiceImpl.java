package com.stackstech.dcp.server.operation.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stackstech.dcp.core.util.DateUtils;
import com.stackstech.dcp.server.dataservice.dao.AccessLogMapper;
import com.stackstech.dcp.server.operation.service.ServiceMonitorService;
import com.stackstech.dcp.server.operation.vo.ServiceMonitorQueryVO;
import com.stackstech.dcp.server.operations.dao.ServiceMonitorMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务监控ServiceImpl
 */
@Service
public class ServiceMonitorServiceImpl implements ServiceMonitorService {

    @Autowired
    private AccessLogMapper accessLogMapper;
    @Autowired
    private ServiceMonitorMapper serviceMonitorMapper;

    /**
     * 获取服务访问数量
     *
     * @param monitorQueryVO
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> queryAccessCount(ServiceMonitorQueryVO monitorQueryVO) throws Exception {
        Map<String, Object> tupleOfDay = DateUtils.getTupleDays(monitorQueryVO.getQueryDate(), Integer.parseInt(monitorQueryVO.getDateIndex()));
        tupleOfDay.put("dataServiceId", monitorQueryVO.getDataServiceId());

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>();
        if (StringUtils.isNotEmpty(String.valueOf(monitorQueryVO.getPageNo())) && StringUtils.isNotEmpty(String.valueOf(monitorQueryVO.getPageSize()))) {
            Page<Map<String, Object>> objects = PageHelper.startPage(Integer.parseInt(String.valueOf(monitorQueryVO.getPageNo())), Integer.parseInt(String.valueOf(monitorQueryVO.getPageSize())));
            serviceMonitorMapper.queryServiceCountRank(tupleOfDay);
            pageInfo = new PageInfo<>(objects);
        } else {
            List<Map<String, Object>> serviceCountRank = serviceMonitorMapper.queryServiceCountRank(tupleOfDay);
            pageInfo = new PageInfo<>(serviceCountRank);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("count", Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        map.put("list", pageInfo.getList());
        return map;
    }

    /**
     * 获取app访问数量
     *
     * @param monitorQueryVO
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> queryAppCount(ServiceMonitorQueryVO monitorQueryVO) throws Exception {
        Map<String, Object> tupleOfDay = DateUtils.getTupleDays(monitorQueryVO.getQueryDate(), Integer.parseInt(monitorQueryVO.getDateIndex()));
        tupleOfDay.put("dataServiceId", monitorQueryVO.getDataServiceId());

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>();
        if (StringUtils.isNotEmpty(String.valueOf(monitorQueryVO.getPageNo())) && StringUtils.isNotEmpty(String.valueOf(monitorQueryVO.getPageSize()))) {
            Page<Map<String, Object>> objects = PageHelper.startPage(Integer.parseInt(String.valueOf(monitorQueryVO.getPageNo())), Integer.parseInt(String.valueOf(monitorQueryVO.getPageSize())));
            serviceMonitorMapper.queryAppCountRank(tupleOfDay);
            pageInfo = new PageInfo<>(objects);
        } else {
            List<Map<String, Object>> appCountRank = serviceMonitorMapper.queryAppCountRank(tupleOfDay);
            pageInfo = new PageInfo<>(appCountRank);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("count", Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        map.put("list", pageInfo.getList());
        return map;
    }

    /**
     * 获取服务访问平均时间
     *
     * @param monitorQueryVO
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> queryAccessAvg(ServiceMonitorQueryVO monitorQueryVO) throws Exception {
        Map<String, Object> tupleOfDay = DateUtils.getTupleDays(monitorQueryVO.getQueryDate(), Integer.parseInt(monitorQueryVO.getDateIndex()));
        tupleOfDay.put("dataServiceId", monitorQueryVO.getDataServiceId());

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>();
        if (StringUtils.isNotEmpty(String.valueOf(monitorQueryVO.getPageNo())) && StringUtils.isNotEmpty(String.valueOf(monitorQueryVO.getPageSize()))) {
            Page<Map<String, Object>> objects = PageHelper.startPage(Integer.parseInt(String.valueOf(monitorQueryVO.getPageNo())), Integer.parseInt(String.valueOf(monitorQueryVO.getPageSize())));
            serviceMonitorMapper.queryAccessAvgRank(tupleOfDay);
            pageInfo = new PageInfo<>(objects);
        } else {
            List<Map<String, Object>> accessAvgRank = serviceMonitorMapper.queryAccessAvgRank(tupleOfDay);
            pageInfo = new PageInfo<>(accessAvgRank);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("count", Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        map.put("list", pageInfo.getList());
        return map;
    }

    /**
     * 获取服务执行平均时间排名
     *
     * @param monitorQueryVO
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> queryExecuteAvg(ServiceMonitorQueryVO monitorQueryVO) throws Exception {
        Map<String, Object> tupleOfDay = DateUtils.getTupleDays(monitorQueryVO.getQueryDate(), Integer.parseInt(monitorQueryVO.getDateIndex()));
        tupleOfDay.put("dataServiceId", monitorQueryVO.getDataServiceId());

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>();
        if (StringUtils.isNotEmpty(String.valueOf(monitorQueryVO.getPageNo())) && StringUtils.isNotEmpty(String.valueOf(monitorQueryVO.getPageSize()))) {
            Page<Map<String, Object>> objects = PageHelper.startPage(Integer.parseInt(String.valueOf(monitorQueryVO.getPageNo())), Integer.parseInt(String.valueOf(monitorQueryVO.getPageSize())));
            serviceMonitorMapper.queryExecuteAvgRank(tupleOfDay);
            pageInfo = new PageInfo<>(objects);
        } else {
            List<Map<String, Object>> executeAvgRank = serviceMonitorMapper.queryExecuteAvgRank(tupleOfDay);
            pageInfo = new PageInfo<>(executeAvgRank);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("count", Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        map.put("list", pageInfo.getList());
        return map;
    }


    /**
     * 查询时间段服务指标
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> queryAll(Map<String, Object> map) throws Exception {
        String queryDate = (String) map.get("queryDate");
        String dateIndex = (String) map.get("dateIndex");
        String dataServiceId = map.get("dataServiceId") != null ? String.valueOf(map.get("dataServiceId")) : null;

        // 今天-0  昨天-1  七天-7 30天-30
        Map<String, Object> tupleOfDay = DateUtils.getTupleDays(queryDate, Integer.parseInt(dateIndex));
        Map<String, Object> result = this.queryByDate(dateIndex, tupleOfDay, dataServiceId);
        return result;
    }


    /**
     * 获取时间指标
     *
     * @param dateIndex
     * @return
     */
    private Map<String, Object> queryByDate(String dateIndex, Map<String, Object> date, String dataServiceId) {
        Map<String, Object> map = new HashMap<>();
        int appKpis = serviceMonitorMapper.queryAppKpi(date);

        date.put("dataServiceId", dataServiceId);
        //获取服务访问指标<访问量/访问成功/访问失败/平均访问时间/平均执行时间>
        Map<String, Object> serviceKpis = serviceMonitorMapper.queryAccessKpi(date);
        //计算服务访问时长排名
        List<Map<String, Object>> accessTimeRank = serviceMonitorMapper.queryAccessTimeRank(date);
        //计算执行时长排名
        List<Map<String, Object>> executeTimeRank = serviceMonitorMapper.queryExecuteTimeRank(date);

        map.put("accessTotal", serviceKpis != null ? serviceKpis.get("accessCount") : 0);                //总条数
        map.put("accessServiceCount", serviceKpis != null ? serviceKpis.get("accessCount") : 0);         //浏览量
        map.put("accessAppCount", appKpis);                                                               //访客量
        map.put("accessAvgTime", serviceKpis != null ? serviceKpis.get("accessAvg") : 0);                 //平均执行时长
        map.put("executeAvgTime", serviceKpis != null ? serviceKpis.get("executeAvg") : 0);              //平均DB时长
        map.put("accessOkCount", serviceKpis != null ? serviceKpis.get("accessOkCount") : 0);            //访问成功次数
        map.put("accessErrorCount", serviceKpis != null ? serviceKpis.get("accessErrorCount") : 0);      //访问失败次数
        map.put("serviceCountRank", new ArrayList<Map<String, Object>>());                      //访问服务数量排名
        map.put("appCountRank", new ArrayList<Map<String, Object>>());                              //访问用户数量排名
        map.put("serviceTimeRank", accessTimeRank);                         //访问服务时长排名
        map.put("executeTimeRank", executeTimeRank);                        //访问DB时长排名
        map.put("serviceAvgRank", new ArrayList<Map<String, Object>>());                          //访问服务时长排名
        map.put("executeAvgRank", new ArrayList<Map<String, Object>>());                         //访问DB时长排名
        if ("0".equals(dateIndex) || "1".equals(dateIndex)) {
            //小时指标
            List<Map<String, Object>> accessApiByHour = serviceMonitorMapper.queryAccessApiByHour(date);
            map.put("accessHourApi", accessApiByHour);
        } else {
            //日指标
            List<Map<String, Object>> accessApiByDay = serviceMonitorMapper.queryAccessApiByDay(date);
            if (accessApiByDay != null && accessApiByDay.size() > 0) {
                String queryDate = (String) date.get("endTime");
                for (Map<String, Object> apiDay : accessApiByDay) {
                    String accessDay = String.valueOf(apiDay.get("accessTime"));
                    apiDay.put("day", DateUtils.daysBetween(accessDay, queryDate));
                }
            }
            map.put("accessDayApi", accessApiByDay);
        }
        return map;
    }
}
