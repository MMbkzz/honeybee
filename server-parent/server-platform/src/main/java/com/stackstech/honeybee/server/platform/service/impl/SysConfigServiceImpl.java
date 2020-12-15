package com.stackstech.honeybee.server.platform.service.impl;

import com.stackstech.honeybee.core.conf.CacheConfig;
import com.stackstech.honeybee.core.conf.DatabaseConfig;
import com.stackstech.honeybee.server.dataasset.dao.ModelCodeMapper;
import com.stackstech.honeybee.server.dataasset.model.ModelCode;
import com.stackstech.honeybee.server.platform.dao.SysConfigMapper;
import com.stackstech.honeybee.server.platform.model.SysConfig;
import com.stackstech.honeybee.server.platform.service.SysConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {


    @Autowired
    private CacheConfig cacheConfig;
    @Autowired
    private DatabaseConfig databaseConfig;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private SysConfigMapper sysConfigMapper;
    @Autowired
    private ModelCodeMapper modelCodeMapper;

    /**
     * 获取配置列表
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> queryAll(ModelCode modelCode) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();

        List<ModelCode> parentCodes = modelCodeMapper.queryByType("config_type");
        if (parentCodes != null && parentCodes.size() > 0) {
            for (ModelCode parentCode : parentCodes) {
                List<Map<String, Object>> childList = new ArrayList<>();
                List<ModelCode> childCodes = modelCodeMapper.queryByParentCode(parentCode.getCode());
                if (childCodes != null && childCodes.size() > 0) {
                    for (ModelCode childCode : childCodes) {
                        SysConfig sysConfig = new SysConfig();
                        sysConfig.setConfigType(childCode.getParentCode());
                        sysConfig.setConfigSubtype(childCode.getCode());

                        Map<String, Object> childMap = new HashMap<>();
                        childMap.put("type", childCode.getType());
                        childMap.put("code", childCode.getCode());
                        childMap.put("displayName", childCode.getDisplayName());
                        childMap.put("codeDesc", childCode.getCodeDesc());
                        childMap.put("configList", sysConfigMapper.queryAll(sysConfig));
                        childList.add(childMap);
                    }
                }
                Map<String, Object> parentMap = new HashMap<>();
                parentMap.put("type", parentCode.getType());
                parentMap.put("code", parentCode.getCode());
                parentMap.put("displayName", parentCode.getDisplayName());
                parentMap.put("codeDesc", parentCode.getCodeDesc());
                parentMap.put("childList", childList);
                list.add(parentMap);
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> queryConfig() {
        Map<String, Object> map = new HashMap<>();
        //Redis
        Map<String, Object> redisMap = new HashMap<>();
        redisMap.put("ip", cacheConfig.getHost());
        redisMap.put("port", cacheConfig.getPort());
        redisMap.put("database", cacheConfig.getDatabase());

        //DB
        String url = databaseConfig.getJdbcUrl();
        Map<String, Object> dbMap = new HashMap<>();
        if (StringUtils.isNotEmpty(url)) {
            url = url.substring(url.indexOf("://") + 3, url.indexOf("?"));
            dbMap.put("ip", url.substring(0, url.indexOf(":")));
            dbMap.put("port", url.substring(url.indexOf(":") + 1, url.indexOf("/")));
            dbMap.put("database", url.substring(url.indexOf("/") + 1));
        }
        dbMap.put("userName", databaseConfig.getUsername());
        dbMap.put("password", databaseConfig.getPassword());

        map.put("cache", redisMap);
        map.put("db", dbMap);
        return map;
    }

    @Override
    public int delete(Long id) throws Exception {
        return sysConfigMapper.delete(id);
    }

    @Override
    public int update(SysConfig sysConfig) throws Exception {
        return sysConfigMapper.update(sysConfig);
    }

    @Override
    public int add(SysConfig sysConfig) throws Exception {
        return sysConfigMapper.insert(sysConfig);
    }

    @Override
    public List<SysConfig> queryAll(SysConfig sysConfig) {
        return sysConfigMapper.queryAll(sysConfig);
    }

}
