package com.stackstech.dcp.server.datasource.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.stackstech.dcp.connector.core.ResourceSessionFactory;
import com.stackstech.dcp.connector.core.ResourceSessionManager;
import com.stackstech.dcp.connector.core.util.SqlParser;
import com.stackstech.dcp.connector.core.util.SqlTemplateUtil;
import com.stackstech.dcp.core.cache.ResourceExpectCache;
import com.stackstech.dcp.core.enums.ParameterTypesEnum;
import com.stackstech.dcp.core.enums.ServiceSourceStatusEnum;
import com.stackstech.dcp.core.enums.ServiceStatusEnum;
import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.core.util.CommonUtils;
import com.stackstech.dcp.server.auth.dao.AuthUserMapper;
import com.stackstech.dcp.server.auth.model.AuthUser;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import com.stackstech.dcp.server.dataasset.dao.ModelCodeMapper;
import com.stackstech.dcp.server.dataasset.dao.ServiceModelMapper;
import com.stackstech.dcp.server.dataasset.model.ModelCode;
import com.stackstech.dcp.server.dataasset.model.ServiceModel;
import com.stackstech.dcp.server.datasource.dao.ServiceSourceMapper;
import com.stackstech.dcp.server.datasource.model.ServiceSource;
import com.stackstech.dcp.server.datasource.service.ServiceSourceService;
import com.stackstech.dcp.server.datasource.vo.ServiceSourceQueryVO;
import com.stackstech.dcp.server.datasource.vo.ServiceSourceVO;
import com.stackstech.dcp.server.param.dao.ParameterMapper;
import com.stackstech.dcp.server.param.model.Parameter;
import com.stackstech.dcp.server.platform.dao.InstanceMapper;
import com.stackstech.dcp.server.platform.dao.ServiceDriverMapper;
import com.stackstech.dcp.server.platform.model.Instance;
import com.stackstech.dcp.server.platform.model.ServiceDriver;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据源Service类
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ServiceSourceServiceImpl implements ServiceSourceService {

    @Autowired
    private ResourceSessionManager resourceSessionManager;

    private final Logger logger = LoggerFactory.getLogger(ServiceSourceServiceImpl.class);
    @Autowired
    private ModelCodeMapper modelCodeMapper;
    @Autowired
    private ServiceSourceMapper serviceSourceMapper;
    @Autowired
    private ServiceModelMapper serviceModelMapper;
    @Autowired
    private ParameterMapper parameterMapper;
    @Autowired
    private ServiceDriverMapper serviceDriverMapper;
    @Autowired
    private InstanceMapper instanceMapper;

    @Autowired
    private AuthUserMapper authUserMapper;
    @Autowired
    private LoginUserManager loginUserManager;

    @Autowired
    private ResourceExpectCache resourceExpectCache;


    /**
     * 根据条件返回数据源列表
     *
     * @param queryVO
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Map<String, Object> queryAll(ServiceSourceQueryVO queryVO, int pageNo, int pageSize) throws Exception {
        List<Map<String, Object>> dataSourceConfigVOS = new ArrayList<Map<String, Object>>();
        //query all datasources
        Page<ServiceSource> objects = PageHelper.startPage(pageNo, pageSize);
        if (StringUtils.isNotBlank(queryVO.getQueryType())) {
            serviceSourceMapper.queryByCondition(queryVO.getQueryString());
        } else {
            serviceSourceMapper.queryAll(CommonUtils.elementToMap(queryVO));
        }
        PageInfo<ServiceSource> pageInfo = new PageInfo<>(objects);
        for (ServiceSource dataSource : pageInfo.getList()) {
            //获取更新用户
            this.queryUpdateUser(dataSource);

            Map<String, Object> sourceMap = CommonUtils.elementToMap(dataSource);

            ServiceDriver serviceDriver = serviceDriverMapper.queryByPrimaryKey(dataSource.getDriverId());
            if (serviceDriver != null) {
                sourceMap.put("driverName", serviceDriver.getDriverName());
                sourceMap.put("driverVersion", serviceDriver.getVersion());
            }
            dataSourceConfigVOS.add(sourceMap);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", dataSourceConfigVOS);
        map.put("count", pageInfo.getTotal());
        return map;
    }

    /**
     * 根据条件获取数据源
     *
     * @param id
     * @return
     */
    @Override
    public ServiceSourceVO query(String id) throws Exception {
        //query datasource
        ServiceSource exsitDatasource = serviceSourceMapper.queryByPrimaryKey(id);
        if (exsitDatasource != null) {
            //获取更新用户
            this.queryUpdateUser(exsitDatasource);

            Parameter parameter = new Parameter();
            parameter.setObjectId(exsitDatasource.getId());
            parameter.setObjectType(ParameterTypesEnum.DATASOURCE.toString());

            ServiceSourceVO dsConfigVO = parseDatasource2DatasourceVO(exsitDatasource,
                    serviceDriverMapper.queryByPrimaryKey(exsitDatasource.getDriverId()),
                    parameterMapper.queryAll(parameter));
            return dsConfigVO;
        }
        return null;
    }


    /**
     * 根据名称获取DataSource
     *
     * @param serviceSourceName
     * @return
     */
    @Override
    public ServiceSource queryByName(String serviceSourceName) {
        return serviceSourceMapper.queryByName(serviceSourceName);
    }

    /**
     * 新增数据源
     * <新增前调用query()查看是否存在重名数据源></>
     *
     * @param serviceSourceVO
     * @return
     */
    @Override
    public ResponseEntity<?> insert(ServiceSourceVO serviceSourceVO, HttpServletRequest req) throws Exception {
        //add datasource
        LoginUserProtos.LoginUser loginUser = getLoginUser(req);
        if (loginUser != null) {
            serviceSourceVO.setCreateBy(loginUser.getUserId());
            serviceSourceVO.setUpdateBy(loginUser.getUserId());
        }
        //校验连接状态
        ResponseEntity<?> responseEntity = getConnectionStatus(serviceSourceVO);
        if (responseEntity != null && responseEntity.toString().contains("500")) {
            return responseEntity;
        }

        ServiceSource serviceSource = parseDatasourceVO2Datasource(serviceSourceVO);
        //判断数据源连接数是否大于实例数
        List<Instance> instances = instanceMapper.queryAll(new Instance());
        if (instances != null && instances.size() > 0) {
            Integer maxConnectors = serviceSource.getMaxConnections();
            if (maxConnectors < instances.size()) {
                return ResponseError.create(500, "数据源连接数小于当前实例数,新增不成功");
            }
        }
        serviceSource.setId(serviceSourceMapper.queryPrimaryKey());
        int i = serviceSourceMapper.insert(serviceSource);
        if (i > 0) {
            if (serviceSourceVO.getParamConfigList() != null && serviceSourceVO.getParamConfigList().size() > 0) {
                List<Parameter> paramConfigList = serviceSourceVO.getParamConfigList();
                //add paramconfig
                for (Parameter parameter : paramConfigList) {
                    parameter.setSeqNum(paramConfigList.indexOf(parameter) + 1);
                    parameter.setCreateBy(loginUser != null ? loginUser.getUserId() : null);
                    parameter.setObjectId(serviceSource.getId());
                    parameter.setObjectType(ParameterTypesEnum.DATASOURCE.toString());

                    parameterMapper.insert(parameter);
                }
            }
            return ResponseOk.create("新增数据源成功");
        } else {
            return ResponseError.create(500, "新增数据源失败");
        }
    }

    /**
     * 更新DataSource
     *
     * @param serviceSourceVO
     * @return
     */
    @Override
    public ResponseEntity<?> update(ServiceSourceVO serviceSourceVO, HttpServletRequest req) throws Exception {
        //update datasource
        LoginUserProtos.LoginUser loginUser = getLoginUser(req);
        if (loginUser != null) {
            serviceSourceVO.setUpdateBy(loginUser.getUserId());
        }
        ServiceSource serviceSource = parseDatasourceVO2Datasource(serviceSourceVO);
        //判断数据源连接数是否大于实例数
        ServiceSource querySource = serviceSourceMapper.queryByPrimaryKey(serviceSource.getId());
        if (querySource != null && querySource.getMaxConnections().intValue() != serviceSource.getMaxConnections().intValue()) {
            List<Instance> instances = instanceMapper.queryAll(new Instance());
            if (instances != null && instances.size() > 0) {
                Integer maxConnectors = serviceSource.getMaxConnections();
                if (maxConnectors < instances.size()) {
                    return ResponseError.create(500, "数据源连接数小于当前实例数,数据源更新失败");
                }
            }
        }
        int i = serviceSourceMapper.update(serviceSource);
        if (i > 0) {
            if (serviceSourceVO.getParamConfigList() != null && serviceSourceVO.getParamConfigList().size() > 0) {
                List<Parameter> paramConfigList = serviceSourceVO.getParamConfigList();
                //update or insert paramconfig
                for (int p = 0; p < paramConfigList.size(); p++) {
                    Parameter parameter = paramConfigList.get(p);
                    if (parameter.getId() != null) {
                        parameter.setSeqNum(p + 1);
                        parameterMapper.update(parameter);
                    } else {
                        parameter.setSeqNum(p + 1);
                        parameter.setCreateBy(loginUser != null ? loginUser.getUserId() : null);
                        parameter.setObjectType(ParameterTypesEnum.DATASOURCE.toString());
                        parameter.setObjectId(serviceSourceVO.getId());
                        parameterMapper.insert(parameter);
                    }
                }
            }
            return ResponseOk.create("更新数据源成功");
        } else {
            return ResponseError.create(500, "更新数据源失败");
        }
    }

    /**
     * 更新数据源状态
     *
     * @param serviceSource
     * @return
     */
    @Override
    public ResponseEntity<?> changeStatus(ServiceSource serviceSource, HttpServletRequest req) throws Exception {
        ServiceSource querySource = serviceSourceMapper.queryByPrimaryKey(serviceSource.getId());
        if (querySource != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", querySource.getStageCode());
            map.put("type", "source_stage_code");
            //当状态=“启用”and 阶段=“已发布”时，可进行“禁用”操作 <禁用/待下线>
            if (ServiceStatusEnum.disabled.toString().equals(serviceSource.getStatusCode()) &&
                    !(ServiceStatusEnum.enabled.toString().equals(querySource.getStatusCode()) && ServiceStatusEnum.published.toString().equals(querySource.getStageCode()))) {
                ModelCode modelCode = modelCodeMapper.queryCode(map);
                String displayName = (modelCode == null ? querySource.getStageCode() : modelCode.getDisplayName());
                return ResponseError.create(500, "数据源处于" + displayName + "阶段，不能进行禁用操作");
            }

            //当状态=“禁用”and 阶段=“已下线”时，可进行“启用”操作<启用/待发布>
            if (ServiceStatusEnum.enabled.toString().equals(serviceSource.getStatusCode()) &&
                    !(ServiceStatusEnum.disabled.toString().equals(querySource.getStatusCode()) && ServiceStatusEnum.offline.toString().equals(querySource.getStageCode()))) {
                ModelCode modelCode = modelCodeMapper.queryCode(map);
                String displayName = (modelCode == null ? querySource.getStageCode() : modelCode.getDisplayName());
                return ResponseError.create(500, "数据源处于" + displayName + "阶段，不能进行启用操作");
            }

            //更新DB状态
            LoginUserProtos.LoginUser loginUser = getLoginUser(req);
            if (loginUser != null) {
                serviceSource.setUpdateBy(loginUser.getUserId());
            }
            int i = serviceSourceMapper.update(serviceSource);
            if (i > 0) {
                return ResponseOk.create("数据源状态更新成功");
            } else {
                return ResponseError.create(500, "数据源状态更新失败");
            }
        } else {
            return ResponseError.create(500, "数据源不存在");
        }
    }

    /**
     * 删除数据源
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<?> delete(String id) throws Exception {
        //query dcp_data_model by datasourceId
        if (serviceModelMapper.queryBySourceId(id) != null && serviceModelMapper.queryBySourceId(id).size() > 0) {
            ServiceModel model = serviceModelMapper.queryBySourceId(id).get(0);
            return ResponseError.create(500, "删除数据源失败!模型" + model.getModelName() + "正在使用中");
        }
        ServiceSource serviceSource = serviceSourceMapper.queryByPrimaryKey(id);
        if (serviceSource != null) {
            //状态='禁用'、阶段='下线'时才能进行删除
            if (ServiceStatusEnum.disabled.toString().equals(serviceSource.getStatusCode()) &&
                    ServiceStatusEnum.offline.toString().equals(serviceSource.getStageCode())) {
                int i = serviceSourceMapper.delete(id);
                if (i > 0) {
                    Parameter parameter = new Parameter();
                    parameter.setObjectId(id);
                    List<Parameter> paramConfigList = parameterMapper.queryAll(parameter);
                    if (paramConfigList != null && paramConfigList.size() > 0) {
                        for (Parameter parameter1 : paramConfigList) {
                            parameterMapper.delete(parameter1.getId());
                        }
                    }
                    return ResponseOk.create("删除数据源成功!");
                } else {
                    return ResponseError.create(500, "删除数据源失败");
                }
            } else {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("code", serviceSource.getStageCode());
                map.put("type", "source_stage_code");
                ModelCode modelCode = modelCodeMapper.queryCode(map);
                String displayName = (modelCode == null ? serviceSource.getStageCode() : modelCode.getDisplayName());
                return ResponseError.create(500, "数据源处于" + displayName + "阶段,不能进行删除操作");
            }
        } else {
            return ResponseError.create(500, "数据源不存在");
        }
    }

    @Override
    public int countAll(ServiceSourceQueryVO queryVO) throws Exception {
        int i = 0;
        if (StringUtils.isNotBlank(queryVO.getQueryType())) {
            i = serviceSourceMapper.countByCondition(queryVO.getQueryString());
        } else {
            i = serviceSourceMapper.countAll(CommonUtils.elementToMap(queryVO));
        }
        return i;
    }

    /**
     * 获取数据源连接状态
     * DB url + user + pwd
     * REDIS url + user + pwd
     * Hbase zk集群 + user + pwd
     * kafka zk集群 + broker列表
     *
     * @param serviceSourceVO<driverId + params>
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<?> getConnectionStatus(ServiceSourceVO serviceSourceVO) throws Exception {
        List<Parameter> paramConfigList = serviceSourceVO.getParamConfigList();
        //parse param map
        if (paramConfigList != null && paramConfigList.size() > 0) {
            Map<String, Object> properties = new HashMap();
            for (Parameter paramConfig : paramConfigList) {
                if (paramConfig.getParamValue() == null) {
                    return ResponseError.create(500, "数据源连接参数值为空,连接失败");
                }
                properties.put(paramConfig.getParamName(), paramConfig.getParamValue());
            }
            //get connection status
            ServiceDriver serviceDriver = serviceDriverMapper.queryByPrimaryKey(serviceSourceVO.getDriverId());
            if (serviceDriver == null) {
                return ResponseError.create(500, "该数据源下的驱动不存在");
            }
            ResourceSessionFactory sessionFactory = this.getSessionFactory(serviceDriver, properties);
            if (sessionFactory == null) {
                String jarPath = serviceDriver.getDriverPath();
                jarPath = jarPath.substring(jarPath.lastIndexOf("/") + 1);
                return ResponseError.create(500, "驱动类加载异常, 请检查驱动包'" + jarPath + "'是否合法");
            }
            boolean flag = sessionFactory.getSession().valid();
            if (!flag) {
                return ResponseError.create(500, "连接超时,请检查连接配置是否正确");
            }
        }
        return ResponseOk.create("数据源连接成功");
    }

    /**
     * 获取数据表
     *
     * @param id
     * @return tables
     */
    @Override
    public List<String> getTables(String id, String name) throws Exception {
        List<String> list = new ArrayList<String>();
        ServiceSource serviceSource = serviceSourceMapper.queryByPrimaryKey(id);
        if (serviceSource != null) {
            Map<String, Object> properties = new HashMap<String, Object>();
            Parameter parameter = new Parameter();
            parameter.setObjectType(ParameterTypesEnum.DATASOURCE.toString());
            parameter.setObjectId(id);

            List<Parameter> parameters = parameterMapper.queryAll(parameter);
            for (Parameter param : parameters) {
                properties.put(param.getParamName(), param.getParamValue());
            }
            //query all tables
            ServiceDriver serviceDriver = serviceDriverMapper.queryByPrimaryKey(serviceSource.getDriverId());
            ResourceSessionFactory sessionFactory = this.getSessionFactory(serviceDriver, properties);
            String sql = SqlParser.getTableSQL(serviceDriver.getDriverClass(), name);
            if (StringUtils.isNotEmpty(sql)) {
                List<Map<String, Object>> fields = sessionFactory.getSession().get(sql);
                if (fields != null && fields.size() > 0) {
                    for (Map<String, Object> map : fields) {
                        list.add((String) Lists.newArrayList(map.values()).get(0));
                    }
                }
            }
            //kafka & Redis ======> return new ArrayList()
        }
        //模糊查询table
        List<String> parseList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            if (StringUtils.isNotBlank(name)) {
                for (String key : list) {
                    if (key.toLowerCase().contains(name.toLowerCase())) {
                        parseList.add(key);
                    }
                }
                parseList = (parseList.size() >= 100 ? parseList.subList(0, 99) : parseList);
            } else {
                parseList = (list.size() >= 100 ? list.subList(0, 99) : list);
            }
            Collections.sort(parseList);
        }
        return parseList;
    }


    /**
     * 获取数据表字段
     *
     * @param id
     * @param tableName
     * @return
     */
    @Override
    public List<Map<String, Object>> getTableFields(String id, String tableName) throws Exception {
        ServiceSource serviceSource = serviceSourceMapper.queryByPrimaryKey(id);
        if (serviceSource != null) {
            Map<String, Object> properties = new HashMap<String, Object>();
            Parameter parameter = new Parameter();
            parameter.setObjectType(ParameterTypesEnum.DATASOURCE.toString());
            parameter.setObjectId(id);

            List<Parameter> parameters = parameterMapper.queryAll(parameter);
            for (Parameter param : parameters) {
                properties.put(param.getParamName(), param.getParamValue());
            }
            //query all tables
            ServiceDriver serviceDriver = serviceDriverMapper.queryByPrimaryKey(serviceSource.getDriverId());
            ResourceSessionFactory sessionFactory = this.getSessionFactory(serviceDriver, properties);
            List<Map<String, Object>> lists = new ArrayList<>();
            String sql = SqlParser.getFiledsSQL(serviceDriver.getDriverClass(), tableName, properties);
            if (StringUtils.isNotEmpty(sql)) {
                lists = sessionFactory.getSession().get(sql);
            }
            return SqlParser.parseTableFields(serviceDriver.getDriverClass(), lists);
            //kafka & Redis ======> return new ArrayList()
        }
        return null;
    }

    /**
     * 解析SQL语句
     *
     * @param sql
     * @return
     */
    @Override
    public ResponseEntity<?> parseSQL(String sql) {
        try {
            Map<String, List<Map<String, Object>>> params = SqlParser.getParams(sql);
            return ResponseOk.create(params);
        } catch (Exception e) {
            logger.error("表达式解析错误", e);
            return ResponseError.create(500, "表达式解析错误,请检查输入表达式的语法及格式");
        }
    }

    /**
     * 测试SQL执行
     * Redis & Hbase & Kafka(API验证) 默认是通的,不需要验证
     *
     * @param params
     * @return
     */
    @Override
    public ResponseEntity<?> executeSQL(Map<String, Object> params) {
        String id = (String) params.get("id");
        String tableName = (String) params.get("tableName");
        String sqlExpre = (String) params.get("expression");
        String topicName = (String) params.get("topic");
        String url = (String) params.get("url");
        List<Map<String, Object>> modelFields = (List<Map<String, Object>>) params.get("modelFields");
        List<Map<String, Object>> modelParams = (List<Map<String, Object>>) params.get("modelParams");
        try {
            ServiceSource serviceSource = serviceSourceMapper.queryByPrimaryKey(id);
            ServiceDriver serviceDriver = (serviceSource != null ? serviceDriverMapper.queryByPrimaryKey(serviceSource.getDriverId()) : null);
            String driverClass = (serviceDriver != null ? serviceDriver.getDriverClass() : null);
            //Redis + Kafka + Hbase验证 + URL校验
            if (driverClass != null && (driverClass.contains("Redis") || driverClass.contains("Kafka") || driverClass.contains("Hbase"))) {
                return ResponseOk.create("SQL验证成功");
            }
            //生成SQL语句
            String parseSQL = null;
            if (tableName == null && StringUtils.isNotBlank(sqlExpre)) {
                parseSQL = sqlExpre;
            }
            if (sqlExpre == null && StringUtils.isNotBlank(tableName)) {
                parseSQL = SqlTemplateUtil.create(modelFields, modelParams, driverClass, tableName);
            }
            //SQL执行校验
            if (parseSQL != null) {
                List<Map<String, Object>> values = new ArrayList<>();
                if (modelParams != null && modelParams.size() > 0) {
                    for (Map<String, Object> param : modelParams) {
                        String paramName = String.valueOf(param.get("paramName"));
                        String paramType = String.valueOf(param.get("paramType"));
                        String defaultValue = (param.get("defaultValue") != null ? String.valueOf(param.get("defaultValue")) : null);
                        if (StringUtils.isNotBlank(defaultValue)) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", paramName.trim());
                            map.put("value", defaultValue.trim());
                            map.put("type", paramType.trim());
                            values.add(map);
                        }
                    }
                }
                String sql = SqlTemplateUtil.parse(parseSQL, SqlTemplateUtil.parseValues(values));
                ResponseEntity response = this.getResultFromSQL(id, sql);
                if (response != null && response.toString().contains("500")) {
                    return response;
                }
            }
        } catch (Exception e) {
            logger.error("SQL验证错误", e);
            return ResponseError.create(500, "SQL验证错误,请检查属性字段格式或SQL语句拼写是否正确");
        }
        return ResponseOk.create("SQL验证成功");
    }

    /**
     * 获取Sql执行结果<数据服务>
     *
     * @param id
     * @param sql
     * @return
     */
    @Override
    public ResponseEntity<?> getResultFromSQL(String id, String sql) {
        List<Map<String, Object>> list = null;
        ServiceSource serviceSource = serviceSourceMapper.queryByPrimaryKey(id);
        if (serviceSource != null) {
            Map<String, Object> properties = new HashMap<String, Object>();
            Parameter parameter = new Parameter();
            parameter.setObjectType(ParameterTypesEnum.DATASOURCE.toString());
            parameter.setObjectId(id);

            List<Parameter> parameters = parameterMapper.queryAll(parameter);
            for (Parameter param : parameters) {
                properties.put(param.getParamName(), param.getParamValue());
            }
            ServiceDriver serviceDriver = serviceDriverMapper.queryByPrimaryKey(serviceSource.getDriverId());
            ResourceSessionFactory sessionFactory = this.getSessionFactory(serviceDriver, properties);
            if (sessionFactory == null) {
                String jarPath = serviceDriver.getDriverPath();
                jarPath = jarPath.substring(jarPath.lastIndexOf("/") + 1);
                return ResponseError.create(500, "驱动类加载异常, 请检查驱动包'" + jarPath + "'是否合法");
            }
            //限制返回条数
            list = sessionFactory.getSession().get(SqlParser.limitSql(serviceDriver.getDriverClass(), sql, 1));
        }
        return ResponseOk.create(list);
    }

    /**
     * 模型获取数据源
     *
     * @param serviceSource
     * @return
     */
    @Override
    public List<ServiceSource> querySources(ServiceSource serviceSource) {
        return serviceSourceMapper.queryAll(CommonUtils.elementToMap(serviceSource));
    }


    /**
     * 获取更新用户
     *
     * @param dataSource
     */
    private void queryUpdateUser(ServiceSource dataSource) {
        AuthUser authUser = null;
        if (dataSource.getCreateBy() != null) {
            authUser = authUserMapper.selectByloginId(dataSource.getCreateBy());
            dataSource.setCreateUser(authUser != null ? authUser.getLoginName() : null);
        }
        if (dataSource.getUpdateBy() != null) {
            authUser = authUserMapper.selectByloginId(dataSource.getUpdateBy());
            dataSource.setUpdateUser(authUser != null ? authUser.getLoginName() : null);
        }
    }


    /**
     * 获取登录用户
     *
     * @param req
     * @return
     */
    private LoginUserProtos.LoginUser getLoginUser(HttpServletRequest req) {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            return loginUserManager.getLoginUser(token);
        }
        return null;
    }


    /**
     * 获取SessionFactory
     *
     * @param serviceDriver
     * @param params
     * @return
     */
    private ResourceSessionFactory getSessionFactory(ServiceDriver serviceDriver, Map<String, Object> params) {
        //get connection status
        if (serviceDriver != null) {
            String jarFile = serviceDriver.getDriverPath();
            String mainClass = serviceDriver.getDriverClass();
            try {
                if (resourceSessionManager != null) {
                    ResourceSessionFactory sessionFactory = resourceSessionManager.openSession(
                            serviceDriver.getId(),
                            params,
                            jarFile,
                            mainClass, false);
                    return sessionFactory;
                }
            } catch (Exception e) {
                logger.error("get resourceFactory error!!", e);
            }
        }
        return null;
    }


    /**
     * 将DataSource转化为DataSourceVO
     *
     * @param serviceSource
     * @return
     */
    private ServiceSourceVO parseDatasource2DatasourceVO(ServiceSource serviceSource, ServiceDriver serviceDriver, List<Parameter> parameters) {
        if (serviceSource != null) {
            ServiceSourceVO serviceSourceVO = new ServiceSourceVO();
            serviceSourceVO.setId(serviceSource.getId());
            serviceSourceVO.setDriverId(serviceSource.getDriverId());
            serviceSourceVO.setServiceSourceName(serviceSource.getServiceSourceName());
            serviceSourceVO.setServiceSourceDesc(serviceSource.getServiceSourceDesc());
            serviceSourceVO.setStatusCode(serviceSource.getStatusCode());
            serviceSourceVO.setStageCode(serviceSource.getStageCode());
            serviceSourceVO.setMaxConnections(serviceSource.getMaxConnections());
            serviceSourceVO.setConnTimeout(serviceSource.getConnTimeout());
            serviceSourceVO.setQueryTimeout(serviceSource.getQueryTimeout());
            serviceSourceVO.setCreateBy(serviceSource.getCreateBy());
            serviceSourceVO.setCreateTime(serviceSource.getCreateTime());
            serviceSourceVO.setUpdateTime(serviceSource.getUpdateTime());
            serviceSourceVO.setUpdateBy(serviceSource.getUpdateBy());
            serviceSourceVO.setCreateUser(serviceSource.getCreateUser());
            serviceSourceVO.setUpdateUser(serviceSource.getUpdateUser());

            serviceSourceVO.setServiceDriver(serviceDriver);
            serviceSourceVO.setParamConfigList(parameters);
            return serviceSourceVO;
        }
        return null;
    }

    /**
     * 将DataSourceVO转化为Datasource
     *
     * @param serviceSourceVO
     * @return
     */
    private ServiceSource parseDatasourceVO2Datasource(ServiceSourceVO serviceSourceVO) {
        if (serviceSourceVO != null) {
            ServiceSource serviceSource = new ServiceSource();
            serviceSource.setId(serviceSourceVO.getId());
            serviceSource.setDriverId(serviceSourceVO.getDriverId());
            serviceSource.setServiceSourceName(serviceSourceVO.getServiceSourceName());
            serviceSource.setServiceSourceDesc(serviceSourceVO.getServiceSourceDesc());
            serviceSource.setStatusCode(serviceSourceVO.getStatusCode());
            serviceSource.setStageCode(serviceSourceVO.getStageCode());
            serviceSource.setMaxConnections(serviceSourceVO.getMaxConnections());
            serviceSource.setConnTimeout(serviceSourceVO.getConnTimeout());
            serviceSource.setQueryTimeout(serviceSourceVO.getQueryTimeout());
            serviceSource.setCreateBy(serviceSourceVO.getCreateBy());
            serviceSource.setCreateTime(serviceSourceVO.getCreateTime());
            serviceSource.setUpdateTime(serviceSourceVO.getUpdateTime());
            serviceSource.setUpdateBy(serviceSourceVO.getUpdateBy());

            return serviceSource;
        }
        return null;
    }


    @Override
    public List<ServiceSource> getEnable() {
        return serviceSourceMapper.queryByStatus(new String[]{ServiceSourceStatusEnum.enabled.code});
    }

    @Override
    public List<ServiceSource> getDisable() {
        return serviceSourceMapper.queryByStatus(new String[]{ServiceSourceStatusEnum.disabled.code});
    }

    @Override
    public boolean isBalanced(ServiceSource serviceSource, List<Instance> instances) {
        /*
         是否负载均衡判断依据
         1. 期望资源数 总和 等于 最大连接数
         2. 有效实例是否都有连接数
         */
        boolean result = false;

        List<String> keys = instances.stream().map(instance -> {
            if (null == instance) {
                return "";
            }

            return instance.getHost() + ":" + instance.getPort();
        }).collect(Collectors.toList());


        if (CollectionUtils.isEmpty(keys)) {
            return result;
        }

        Integer sum = 0;
        for (String key : keys) {
            Integer pool = resourceExpectCache.get(key, serviceSource.getId());
            if (null == pool || 0 == pool) {
                sum = -1;
                break;
            } else {
                sum += pool;
            }
        }

        return sum.equals(serviceSource.getMaxConnections());
    }

    @Override
    public boolean isOnline(ServiceSource serviceSource) {
        /*
        是否上线判断依据
        期望资源数 不为空、0
         */

        boolean result = false;

        Set<String> keys = resourceExpectCache.keys();
        if (CollectionUtils.isEmpty(keys)) {
            return result;
        }


        result = true;
        for (String key : keys) {
            Integer pool = resourceExpectCache.getNaturo(key, serviceSource.getId());
            if (null == pool || 0 == pool) {
                result = false;
                break;
            }
        }

        return result;
    }

    @Override
    public boolean isOffline(ServiceSource serviceSource) {
        /*
        是否下线判断依据
        期望资源数 为空 或 0
         */
        boolean result = true;

        Set<String> keys = resourceExpectCache.keys();
        if (CollectionUtils.isEmpty(keys)) {
            return result;
        }


        for (String key : keys) {
            Integer pool = resourceExpectCache.getNaturo(key, serviceSource.getId());
            if (null != pool && 0 != pool) {
                result = false;
                break;
            }
        }

        return result;
    }


    @Override
    public void updateStage(String id, String stageCode) {
        if (org.apache.commons.lang.StringUtils.isBlank(id)
                || org.apache.commons.lang.StringUtils.isBlank(stageCode)) {
            return;
        }
        ServiceSource serviceSource = new ServiceSource();
        serviceSource.setId(id);
        serviceSource.setStageCode(stageCode);

        serviceSourceMapper.update(serviceSource);
    }


}
