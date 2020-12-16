package com.stackstech.honeybee.server.dataasset.service.impl;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.connector.core.util.SqlTemplateUtil;
import com.stackstech.honeybee.core.enums.ExpressionTypeEnum;
import com.stackstech.honeybee.core.enums.ModelTypesEnum;
import com.stackstech.honeybee.core.enums.ServiceStatusEnum;
import com.stackstech.honeybee.core.http.ResponseError;
import com.stackstech.honeybee.core.http.ResponseOk;
import com.stackstech.honeybee.core.model.LoginUserProtos;
import com.stackstech.honeybee.core.util.CommonUtils;
import com.stackstech.honeybee.server.auth.dao.AuthUserMapper;
import com.stackstech.honeybee.server.auth.model.AuthUser;
import com.stackstech.honeybee.server.auth.utils.LoginUserManager;
import com.stackstech.honeybee.server.auth.utils.TokenUtil;
import com.stackstech.honeybee.server.dataasset.dao.*;
import com.stackstech.honeybee.server.dataasset.model.*;
import com.stackstech.honeybee.server.dataasset.service.ServiceModelService;
import com.stackstech.honeybee.server.dataasset.vo.ServiceModelQueryVO;
import com.stackstech.honeybee.server.dataasset.vo.ServiceModelVO;
import com.stackstech.honeybee.server.dataservice.dao.DataServiceMapper;
import com.stackstech.honeybee.server.dataservice.model.DataService;
import com.stackstech.honeybee.server.dataservice.service.AppDataService;
import com.stackstech.honeybee.server.datasource.dao.ServiceSourceMapper;
import com.stackstech.honeybee.server.datasource.model.ServiceSource;
import com.stackstech.honeybee.server.datasource.service.ServiceSourceService;
import com.stackstech.honeybee.server.platform.dao.ServiceDriverMapper;
import com.stackstech.honeybee.server.platform.model.ServiceDriver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据资产模型操作业务逻辑类
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ServiceModelServiceImpl implements ServiceModelService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ServiceModelMapper serviceModelMapper;
    @Autowired
    private ServiceModelFieldMapper serviceModelFieldMapper;
    @Autowired
    private ServiceModelParamMapper serviceModelParamMapper;

    @Autowired
    private DataAssetAreaMapper dataAssetAreaMapper;
    @Autowired
    private DataAssetTopicMapper dataAssetTopicMapper;
    @Autowired
    private ModelCodeMapper modelCodeMapper;

    @Autowired
    private ServiceSourceService serviceSourceService;
    @Autowired
    private ServiceSourceMapper serviceSourceMapper;
    @Autowired
    private ServiceDriverMapper serviceDriverMapper;

    @Autowired
    private DataServiceMapper dataServiceMapper;
    @Autowired
    private AppDataService appDataService;

    @Autowired
    private AuthUserMapper authUserMapper;
    @Autowired
    private LoginUserManager loginUserManager;

    //批量新增
    @Override
    public ResponseEntity<?> addBatch(List<ServiceModelVO> serviceModelVOS, HttpServletRequest request) throws Exception {
        if (serviceModelVOS != null && serviceModelVOS.size() > 0) {
            for (ServiceModelVO serviceModelVO : serviceModelVOS) {
                ResponseEntity<?> responseEntity = this.add(serviceModelVO, request);
                if (responseEntity != null && responseEntity.toString().contains("500")) {
                    return ResponseError.create(500, "批量新增服务模型失败");
                }
            }
        }
        return ResponseOk.create("新增服务模型成功");
    }

    /**
     * 新增数据资产模型
     *
     * @param serviceModelVO
     * @return
     */
    @Override
    public ResponseEntity<?> add(ServiceModelVO serviceModelVO, HttpServletRequest req) throws Exception {
        LoginUserProtos.LoginUser loginUser = getLoginUser(req);
        if (loginUser != null) {
            serviceModelVO.setCreateBy(loginUser.getUserId());
            serviceModelVO.setUpdateBy(loginUser.getUserId());
        }
        ServiceModel serviceModel = parseModelVO2Assetmodel(serviceModelVO);
        serviceModel.setId(serviceModelMapper.queryPrimaryKey());
        if (serviceModel.getCacheDuration() == null) {
            serviceModel.setCacheDuration(0);
        }
        int i = serviceModelMapper.insert(serviceModel);
        if (i > 0) {
            //新增属性列表
            this.insertModelFields(serviceModel, serviceModelVO.getServiceModelFields());

            //新增参数列表
            insertModelParams(serviceModel, serviceModelVO.getServiceModelParams());

            //当模型发布时, 新增服务
            if (serviceModel.getStatusCode() != null && serviceModel.getStatusCode().equalsIgnoreCase(ServiceStatusEnum.published.toString())) {
                if (serviceModel.getTypeCode() != null && ModelTypesEnum.asset.toString().equals(serviceModel.getTypeCode())) {
                    this.addDataService(serviceModel, serviceModelVO, loginUser, "read");
                }
                if (serviceModel.getTypeCode() != null && ModelTypesEnum.ability.toString().equals(serviceModel.getTypeCode())) {
                    String topicId = serviceModel.getTopicId();
                    if (StringUtils.isNotEmpty(topicId)) {
                        ModelCode modelCode = modelCodeMapper.queryByPrimaryKey(Long.parseLong(topicId));
                        if (modelCode != null) {
                            if (modelCode.getCode() != null && "MQ".equalsIgnoreCase(modelCode.getCode())) {
                                this.addDataService(serviceModel, serviceModelVO, loginUser, "read");
                                this.addDataService(serviceModel, serviceModelVO, loginUser, "write");
                            } else {
                                this.addDataService(serviceModel, serviceModelVO, loginUser, "read");
                            }
                        }
                    }
                }
            }
            return ResponseOk.create("新增服务模型成功");
        } else {
            return ResponseError.create(500, "新增服务模型失败");
        }
    }


    /**
     * 资产模型列表
     *
     * @param queryVO
     * @return
     */
    @Override
    public List<Map<String, Object>> queryAll(ServiceModelQueryVO queryVO) throws Exception {
        List<Map<String, Object>> modelVOS = new ArrayList<Map<String, Object>>();
        // 获取模型列表
        List<ServiceModel> list = null;
        if (ModelTypesEnum.asset.toString().equals(queryVO.getTypeCode())) {
            if (StringUtils.isNotBlank(queryVO.getQueryType())) {
                list = serviceModelMapper.queryAssetByCondition(queryVO.getQueryString(), queryVO.getUserId());
            } else {
                list = serviceModelMapper.queryAssetsModel(CommonUtils.elementToMap(queryVO));
            }
        } else if (ModelTypesEnum.ability.toString().equals(queryVO.getTypeCode())) {
            if (StringUtils.isNotBlank(queryVO.getQueryType())) {
                list = serviceModelMapper.queryAbilityByCondition(queryVO.getQueryString(), queryVO.getUserId());
            } else {
                list = serviceModelMapper.queryAbilityModel(CommonUtils.elementToMap(queryVO));
            }
        }
        if (list != null && list.size() > 0) {
            for (ServiceModel serviceModel : list) {
                //获取更新用户
                this.queryUpdateUser(serviceModel);

                Map<String, Object> modelMap = CommonUtils.elementToMap(serviceModel);

                //查询领域/主题/数据源
                ServiceSource querySource = serviceSourceMapper.queryByPrimaryKey(serviceModel.getServiceSourceId());
                Map<String, Object> queryTopic = Maps.newHashMap();
                Map<String, Object> queryArea = Maps.newHashMap();
                if (ModelTypesEnum.asset.toString().equals(serviceModel.getTypeCode())) {
                    this.queryAssetTopic(queryArea, queryTopic, serviceModel);
                } else if (ModelTypesEnum.ability.toString().equals(serviceModel.getTypeCode())) {
                    this.queryAbilityTopic(queryArea, queryTopic, serviceModel);
                }

                if (querySource != null) {
                    modelMap.put("serviceSourceName", querySource.getServiceSourceName());
                }
                if (queryArea.size() > 0) {
                    modelMap.put("areaName", queryArea.get("areaName"));
                }
                if (queryTopic.size() > 0) {
                    modelMap.put("topicName", queryTopic.get("topicName"));
                }
                modelVOS.add(modelMap);
            }
        }
        return modelVOS;
    }

    /**
     * 资产模型详情查询
     *
     * @param id
     * @return
     */
    @Override
    public ServiceModelVO query(String id) throws Exception {
        ServiceModelVO assetModelVO = null;
        // 获取资产模型信息
        ServiceModel serviceModel = serviceModelMapper.queryByPrimaryKey(id);
        if (serviceModel != null) {
            //更新用户
            this.queryUpdateUser(serviceModel);
            //查询数据源/主题/领域
            Map<String, Object> queryTopic = new HashMap<>();
            Map<String, Object> queryArea = new HashMap<>();
            Map<String, Object> querySource = new HashMap<>();

            this.querySource(querySource, serviceModel);
            if (ModelTypesEnum.asset.toString().equals(serviceModel.getTypeCode())) {
                this.queryAssetTopic(queryArea, queryTopic, serviceModel);
            } else if (ModelTypesEnum.ability.toString().equals(serviceModel.getTypeCode())) {
                this.queryAbilityTopic(queryArea, queryTopic, serviceModel);
            }

            //查询模型字段
            ServiceModelField field = new ServiceModelField();
            field.setServiceModelId(serviceModel.getId());
            List<ServiceModelField> fields = serviceModelFieldMapper.queryAll(field);

            //查询模型参数
            ServiceModelParam param = new ServiceModelParam();
            param.setServiceModelId(serviceModel.getId());
            List<ServiceModelParam> params = serviceModelParamMapper.queryAll(param);
            return parseModel2AssetmodelVO(serviceModel, querySource, queryTopic, queryArea, fields, null, null, params);
        }
        return null;
    }


    /**
     * 资产模型详情查询
     *
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<?> delete(String id) throws Exception {
        //query dataservice
        ServiceModel serviceModel = serviceModelMapper.queryByPrimaryKey(id);
        List<DataService> services = dataServiceMapper.queryByModelId(serviceModel.getId());
        if (services != null && services.size() > 0) {
            for (DataService service : services) {
                if (!ServiceStatusEnum.inactive.toString().equals(service.getStatusCode())) {
                    return ResponseError.create(500, "删除模型失败!服务" + service.getDataServiceName() + "正在使用中");
                }
            }
        }
        //del assetModel
        int i = serviceModelMapper.delete(id);
        if (i > 0) {
            //del fields & models
            ServiceModelField serviceModelField = new ServiceModelField();
            serviceModelField.setServiceModelId(serviceModel.getId());
            List<ServiceModelField> fields = serviceModelFieldMapper.queryAll(serviceModelField);
            if (fields != null && fields.size() > 0) {
                for (ServiceModelField field : fields) {
                    serviceModelFieldMapper.delete(field.getId());
                }
            }
            ServiceModelParam dataModelParam = new ServiceModelParam();
            dataModelParam.setServiceModelId(serviceModel.getId());
            List<ServiceModelParam> params = serviceModelParamMapper.queryAll(dataModelParam);
            if (params != null && params.size() > 0) {
                for (ServiceModelParam param : params) {
                    serviceModelParamMapper.delete(param.getId());
                }
            }
            //del dataService
            if (services != null && services.size() > 0) {
                for (DataService service : services) {
                    appDataService.delete(service.getId());
                }
            }
            return ResponseOk.create("删除模型成功");
        } else {
            return ResponseError.create(500, "删除模型失败");
        }
    }

    /**
     * 资产模型修改
     *
     * @param serviceModelVO
     * @return
     */
    @Override
    public ResponseEntity<?> update(ServiceModelVO serviceModelVO, HttpServletRequest req) throws Exception {
        //修改模型操作
        LoginUserProtos.LoginUser loginUser = getLoginUser(req);
        if (loginUser != null) {
            serviceModelVO.setUpdateBy(loginUser.getUserId());
        }
        ServiceModel serviceModel = parseModelVO2Assetmodel(serviceModelVO);
        int i = serviceModelMapper.update(serviceModel);
        if (i > 0) {
            //同步字段<防止换数据表和数据表结构更改，字段全部load,需要把之前的字段全部删除>
            if (serviceModelVO.isFieldLoad()) {
                serviceModelFieldMapper.deleteByServiceId(serviceModel.getId());
                serviceModelParamMapper.deleteByServiceId(serviceModel.getId());
            }
            //修改模型字段和参数列表
            if (serviceModelVO.getServiceModelFields() != null && serviceModelVO.getServiceModelFields().size() > 0) {
                List<ServiceModelField> fields = serviceModelVO.getServiceModelFields();
                if (serviceModel.getExpressionType() != null && ExpressionTypeEnum.sql.toString().equalsIgnoreCase(serviceModel.getExpressionType())) {
                    this.modifyModelFields(fields, serviceModelVO);
                } else {
                    for (int f = 0; f < fields.size(); f++) {
                        ServiceModelField field = fields.get(f);
                        if (field.getId() != null) {
                            field.setSeqNum(f + 1);
                            serviceModelFieldMapper.update(field);
                        } else {
                            field.setSeqNum(f + 1);
                            field.setServiceModelId(serviceModelVO.getId());
                            serviceModelFieldMapper.insert(field);
                        }
                    }
                }
            }
            if (serviceModelVO.getServiceModelParams() != null && serviceModelVO.getServiceModelParams().size() > 0) {
                List<ServiceModelParam> params = serviceModelVO.getServiceModelParams();
                if (serviceModel.getExpressionType() != null && ExpressionTypeEnum.sql.toString().equalsIgnoreCase(serviceModel.getExpressionType())) {
                    this.modifyModelParams(params, serviceModelVO);
                } else {
                    for (int p = 0; p < params.size(); p++) {
                        ServiceModelParam param = params.get(p);
                        if (param.getId() != null) {
                            param.setSeqNum(p + 1);
                            serviceModelParamMapper.update(param);
                        } else {
                            param.setSeqNum(p + 1);
                            param.setServiceModelId(serviceModelVO.getId());
                            serviceModelParamMapper.insert(param);
                        }
                    }
                }
            }
            //修改SQL表达式<修改表达式 + 表字段新增和修改>
            this.modifyDataService(serviceModel, serviceModelVO);

            //当模型发布时, 新增服务
            if (serviceModel.getStatusCode() != null && serviceModel.getStatusCode().equalsIgnoreCase(ServiceStatusEnum.published.toString())) {
                if (serviceModel.getTypeCode() != null && ModelTypesEnum.asset.toString().equals(serviceModel.getTypeCode())
                        && dataServiceMapper.queryByModelId(serviceModel.getId()) == null) {
                    this.addDataService(serviceModel, serviceModelVO, loginUser, "read");
                }
                //add abilityModel<Message:write>
                if (serviceModel.getTypeCode() != null && ModelTypesEnum.ability.toString().equals(serviceModel.getTypeCode())) {
                    String topicId = serviceModel.getTopicId();
                    if (StringUtils.isNotEmpty(topicId)) {
                        ModelCode modelCode = modelCodeMapper.queryByPrimaryKey(Long.parseLong(topicId));
                        if (modelCode != null) {
                            List<DataService> services = dataServiceMapper.queryByModelId(serviceModel.getId());
                            if (modelCode.getCode() != null && "MQ".equalsIgnoreCase(modelCode.getCode())) {
                                if (services != null && services.size() < 2) {
                                    this.addDataService(serviceModel, serviceModelVO, loginUser, "read");
                                    this.addDataService(serviceModel, serviceModelVO, loginUser, "write");
                                }
                            } else {
                                if (services == null) {
                                    this.addDataService(serviceModel, serviceModelVO, loginUser, "read");
                                }
                            }
                        }
                    }
                }
            }
            return ResponseOk.create("模型更新成功");
        } else {
            return ResponseError.create(500, "模型更新失败");
        }
    }


    @Override
    public int countAll(ServiceModelQueryVO queryVO) throws Exception {
        int i = 0;
        if (ModelTypesEnum.asset.toString().equals(queryVO.getTypeCode())) {
            if (StringUtils.isNotBlank(queryVO.getQueryType())) {
                i = serviceModelMapper.countAssetByCondition(queryVO.getQueryString(), queryVO.getUserId());
            } else {
                i = serviceModelMapper.countAssetsModel(CommonUtils.elementToMap(queryVO));
            }
        } else if (ModelTypesEnum.ability.toString().equals(queryVO.getTypeCode())) {
            if (StringUtils.isNotBlank(queryVO.getQueryType())) {
                i = serviceModelMapper.countAbilityByCondition(queryVO.getQueryString(), queryVO.getUserId());
            } else {
                i = serviceModelMapper.countAbilityModel(CommonUtils.elementToMap(queryVO));
            }
        }
        return i;
    }

    @Override
    public ServiceModel queryByName(String modelName) {
        return serviceModelMapper.queryByName(modelName);
    }

    @Override
    public List<ServiceModel> queryAllParent() {
        return serviceModelMapper.queryAllParent(null);
    }

    private boolean verifyParams(List<ServiceModelParam> params) {
        for (int p = 0; p < params.size(); p++) {
            ServiceModelParam param = params.get(p);
            if (param.getId() == null) {
                return false;
            }
        }
        return true;
    }

    private boolean verifyFields(List<ServiceModelField> fields) {
        for (int f = 0; f < fields.size(); f++) {
            ServiceModelField field = fields.get(f);
            if (field.getId() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 修改模型参数
     *
     * @param params
     * @param serviceModelVO
     */
    private void modifyModelParams(List<ServiceModelParam> params, ServiceModelVO serviceModelVO) {
        boolean paramFlag = verifyParams(params);
        if (paramFlag) {
            //修改
            for (int p = 0; p < params.size(); p++) {
                ServiceModelParam param = params.get(p);
                param.setSeqNum(p + 1);
                serviceModelParamMapper.update(param);
            }
        } else {
            //新增<解析后新增>
            int i = serviceModelParamMapper.deleteByServiceId(serviceModelVO.getId());
            if (i > 0) {
                for (int f = 0; f < params.size(); f++) {
                    ServiceModelParam param = params.get(f);
                    param.setSeqNum(f + 1);
                    param.setServiceModelId(serviceModelVO.getId());
                    serviceModelParamMapper.insert(param);
                }
            }
        }
    }

    /**
     * 修改模型字段
     *
     * @param fields
     * @param serviceModelVO
     */
    private void modifyModelFields(List<ServiceModelField> fields, ServiceModelVO serviceModelVO) {
        boolean fieldFlag = verifyFields(fields);
        if (fieldFlag) {
            //修改
            for (int f = 0; f < fields.size(); f++) {
                ServiceModelField field = fields.get(f);
                field.setSeqNum(f + 1);
                serviceModelFieldMapper.update(field);
            }
        } else {
            //新增<解析后新增>
            int i = serviceModelFieldMapper.deleteByServiceId(serviceModelVO.getId());
            if (i > 0) {
                for (int f = 0; f < fields.size(); f++) {
                    ServiceModelField field = fields.get(f);
                    field.setSeqNum(f + 1);
                    field.setServiceModelId(serviceModelVO.getId());
                    serviceModelFieldMapper.insert(field);
                }
            }
        }
    }


    /**
     * 查询更新用户
     *
     * @param serviceModel
     */
    private void queryUpdateUser(ServiceModel serviceModel) {
        AuthUser authUser = null;
        if (serviceModel.getCreateBy() != null) {
            authUser = authUserMapper.selectByloginId(serviceModel.getCreateBy());
            serviceModel.setCreateUser(authUser != null ? authUser.getLoginName() : null);
        }
        if (serviceModel.getUpdateBy() != null) {
            authUser = authUserMapper.selectByloginId(serviceModel.getUpdateBy());
            serviceModel.setUpdateUser(authUser != null ? authUser.getLoginName() : null);
        }
    }

    /**
     * 返回LoginUser
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
     * 查询能力模型主题
     *
     * @param exsitArea
     * @param exsitTopic
     * @param serviceModel
     */
    private void queryAbilityTopic(Map<String, Object> exsitArea, Map<String, Object> exsitTopic, ServiceModel serviceModel) {
        ModelCode queryTopic = modelCodeMapper.queryByPrimaryKey(Long.parseLong(serviceModel.getTopicId()));
        if (queryTopic != null) {
            exsitTopic.put("id", queryTopic.getId());
            exsitTopic.put("topicName", queryTopic.getDisplayName());
            exsitTopic.put("code", queryTopic.getCode());
            ModelCode queryArea = modelCodeMapper.queryByCode(queryTopic.getParentCode());
            if (queryArea != null) {
                exsitArea.put("id", queryArea.getId());
                exsitArea.put("areaName", queryArea.getDisplayName());
                exsitArea.put("code", queryArea.getCode());
            }
        }
    }

    /**
     * 查询资产模型主题
     *
     * @param exsitArea
     * @param exsitTopic
     * @param serviceModel
     */
    private void queryAssetTopic(Map<String, Object> exsitArea, Map<String, Object> exsitTopic, ServiceModel serviceModel) {
        DataAssetTopic queryTopic = dataAssetTopicMapper.queryByPrimaryKey(serviceModel.getTopicId());
        if (queryTopic != null) {
            exsitTopic.put("id", queryTopic.getId());
            exsitTopic.put("topicName", queryTopic.getTopicName());
            DataAssetArea queryArea = dataAssetAreaMapper.queryByPrimaryKey(queryTopic.getAreaId());
            if (queryArea != null) {
                exsitArea.put("id", queryArea.getId());
                exsitArea.put("areaName", queryArea.getAreaName());
            }
        }
    }

    /**
     * 查询数据源
     *
     * @param exsitSource
     * @param serviceModel
     */
    private void querySource(Map<String, Object> exsitSource, ServiceModel serviceModel) {
        ServiceSource querySource = serviceSourceMapper.queryByPrimaryKey(serviceModel.getServiceSourceId());
        if (querySource != null) {
            exsitSource.put("id", querySource.getId());
            exsitSource.put("serviceSourceName", querySource.getServiceSourceName());
        }
    }

    /**
     * 新增模型属性
     *
     * @param serviceModel
     * @param serviceModelFields
     */
    private void insertModelFields(ServiceModel serviceModel, List<ServiceModelField> serviceModelFields) {
        if (serviceModelFields != null && serviceModelFields.size() > 0) {
            for (ServiceModelField serviceModelField : serviceModelFields) {
                serviceModelField.setServiceModelId(serviceModel.getId());
                serviceModelField.setSeqNum(serviceModelFields.indexOf(serviceModelField) + 1);
                serviceModelFieldMapper.insert(serviceModelField);
            }
        }
    }

    /**
     * 新增模型参数
     *
     * @param serviceModel
     * @param serviceModelParams
     */
    private void insertModelParams(ServiceModel serviceModel, List<ServiceModelParam> serviceModelParams) {
        if (serviceModelParams != null && serviceModelParams.size() > 0) {
            for (ServiceModelParam serviceModelParam : serviceModelParams) {
                serviceModelParam.setServiceModelId(serviceModel.getId());
                serviceModelParam.setSeqNum(serviceModelParams.indexOf(serviceModelParam) + 1);
                serviceModelParamMapper.insert(serviceModelParam);
            }
        }
    }

    /**
     * 新增数据服务
     *
     * @param serviceModel
     * @param serviceModelVO
     * @param loginUser
     * @throws Exception
     */
    private void addDataService(ServiceModel serviceModel, ServiceModelVO serviceModelVO, LoginUserProtos.LoginUser loginUser, String operateType) {
        DataService dataService = new DataService();
        dataService.setId(dataServiceMapper.queryPrimaryKey());
        dataService.setServiceModelId(serviceModel.getId());
        dataService.setTypeCode(serviceModel.getTypeCode());
        dataService.setStatusCode(ServiceStatusEnum.active.toString());
        dataService.setOperateType(operateType);
        dataService.setRequestMethod(serviceModel.getRequestMethod());
        dataService.setBodyPattern(serviceModel.getBodyPattern());

        //生成服务名称
        if (ModelTypesEnum.asset.toString().equals(serviceModel.getTypeCode())) {
            DataAssetTopic assetTopic = dataAssetTopicMapper.queryByPrimaryKey(serviceModel.getTopicId());
            if (assetTopic != null) {
                DataAssetArea assetArea = dataAssetAreaMapper.queryByPrimaryKey(assetTopic.getAreaId());
                if (assetArea != null) {
                    dataService.setDataServiceName(assetArea.getAreaName() + "-" + assetTopic.getTopicName() + "-" + serviceModel.getModelName());
                }
            }
        } else if (ModelTypesEnum.ability.toString().equals(serviceModel.getTypeCode())) {
            ModelCode assetTopic = modelCodeMapper.queryByPrimaryKey(Long.parseLong(serviceModel.getTopicId()));
            if (assetTopic != null) {
                ModelCode assetArea = modelCodeMapper.queryByCode(assetTopic.getParentCode());
                if (assetArea != null) {
                    dataService.setDataServiceName(assetArea.getDisplayName() + "-" + assetTopic.getDisplayName() + "-" + serviceModel.getModelName());
                }
            }
        }

        //生成SQL表达式
        dataService.setExpression(this.createSQL(serviceModel, serviceModelVO));
        dataService.setCreateBy(loginUser != null ? loginUser.getUserId() : null);
        dataService.setUpdateBy(loginUser != null ? loginUser.getUserId() : null);
        dataServiceMapper.insert(dataService);
    }

    /**
     * 修改服务表达式
     *
     * @param serviceModel
     * @param serviceModelVO
     */
    private void modifyDataService(ServiceModel serviceModel, ServiceModelVO serviceModelVO) {
        ServiceModel queryModel = serviceModelMapper.queryByPrimaryKey(serviceModel.getId());
        if (queryModel != null && ExpressionTypeEnum.table.toString().equalsIgnoreCase(queryModel.getExpressionType())) {
            List<DataService> dataServices = dataServiceMapper.queryByModelId(serviceModel.getId());
            if (dataServices != null && dataServices.size() > 0) {
                DataService dataService = dataServices.get(0);
                dataService.setExpression(this.createSQL(serviceModel, serviceModelVO));
                dataServiceMapper.update(dataService);
            }
        } else if (queryModel != null && ExpressionTypeEnum.sql.toString().equalsIgnoreCase(queryModel.getExpressionType())) {
            if (queryModel.getExpression() != null && serviceModel.getExpression() != null
                    && (!queryModel.getExpression().equals(serviceModel.getExpression()))) {
                List<DataService> dataServices = dataServiceMapper.queryByModelId(serviceModel.getId());
                if (dataServices != null && dataServices.size() > 0) {
                    DataService dataService = dataServices.get(0);
                    dataService.setExpression(queryModel.getExpression());
                    dataServiceMapper.update(dataService);
                }
            }
        }
    }

    /**
     * 生成SQL表达式<服务>
     *
     * @param serviceModel
     * @param serviceModelVO
     */
    private String createSQL(ServiceModel serviceModel, ServiceModelVO serviceModelVO) {
        String expression = null;
        if (serviceModel.getExpressionType() != null && ExpressionTypeEnum.table.toString().equalsIgnoreCase(serviceModel.getExpressionType())) {
            List<ServiceModelField> fields = serviceModelVO.getServiceModelFields();
            List<ServiceModelParam> params = serviceModelVO.getServiceModelParams();
            ServiceSource serviceSource = serviceSourceMapper.queryByPrimaryKey(serviceModel.getServiceSourceId());
            if (serviceSource != null) {
                ServiceDriver serviceDriver = serviceDriverMapper.queryByPrimaryKey(serviceSource.getDriverId());
                if (serviceDriver != null && serviceDriver.getDriverClass().contains("Hbase")) {
                    expression = serviceModel.getExpression();
                } else {
                    try {
                        expression = SqlTemplateUtil.create(CommonUtils.elementToArrays(fields),
                                CommonUtils.elementToArrays(params),
                                serviceDriver.getDriverClass(),
                                serviceModel.getExpression());
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        } else {
            expression = serviceModel.getExpression();
        }
        return expression;
    }

    /**
     * 校验SQL
     * Redis & Hbase & Kafka(API验证) 默认是通的,不需要验证
     */
    private ResponseEntity<?> verifySQL(ServiceModelVO serviceModelVO) {
        ServiceSource serviceSource = serviceSourceMapper.queryByPrimaryKey(serviceModelVO.getServiceSourceId());
        if (serviceSource != null) {
            Map<String, Object> params = new HashMap<>();
            //表校验
            if (serviceModelVO.getExpressionType() != null && ExpressionTypeEnum.table.toString().equalsIgnoreCase(serviceModelVO.getExpressionType())) {
                String driverId = serviceSource.getDriverId();
                ServiceDriver serviceDriver = serviceDriverMapper.queryByPrimaryKey(driverId);
                if (serviceDriver != null && !serviceDriver.getDriverClass().contains("Hbase")) {
                    params.put("id", serviceSource.getId());
                    params.put("tableName", serviceModelVO.getExpression());
                    params.put("modelFields", CommonUtils.elementToArrays(serviceModelVO.getServiceModelFields()));
                    params.put("modelParams", CommonUtils.elementToArrays(serviceModelVO.getServiceModelParams()));
                    try {
                        return serviceSourceService.executeSQL(params);
                    } catch (Exception e) {
                        return ResponseError.create(500, "SQL验证错误,请检查参数是否填写正确");
                    }
                }
            }
            //sql表达式校验
            else if (serviceModelVO.getExpressionType() != null && ExpressionTypeEnum.sql.toString().equalsIgnoreCase(serviceModelVO.getExpressionType())) {
                params.put("id", serviceSource.getId());
                params.put("expression", serviceModelVO.getExpression());
                params.put("modelParams", CommonUtils.elementToArrays(serviceModelVO.getServiceModelParams()));
                try {
                    return serviceSourceService.executeSQL(params);
                } catch (Exception e) {
                    return ResponseError.create(500, "SQL验证错误,请检查SQL表达式是否拼写正确");
                }
            }
        }
        return ResponseOk.create("200", "SQL验证成功");
    }

    /**
     * 将DataModelVO转化为DataModel
     *
     * @param serviceModelVO
     * @return
     */
    private ServiceModel parseModelVO2Assetmodel(ServiceModelVO serviceModelVO) {
        if (serviceModelVO != null) {
            ServiceModel serviceModel = new ServiceModel();
            serviceModel.setId(serviceModelVO.getId());
            serviceModel.setParentId(serviceModelVO.getParentId());
            serviceModel.setServiceSourceId(serviceModelVO.getServiceSourceId());
            serviceModel.setTopicId(serviceModelVO.getTopicId());
            serviceModel.setModelName(serviceModelVO.getModelName());
            serviceModel.setModelDesc(serviceModelVO.getModelDesc());
            serviceModel.setCacheDuration(serviceModelVO.getCacheDuration());
            serviceModel.setRequestMethod(serviceModelVO.getRequestMethod());
            serviceModel.setExpression(serviceModelVO.getExpression());
            serviceModel.setExpressionType(serviceModelVO.getExpressionType());
            serviceModel.setStatusCode(serviceModelVO.getStatusCode());
            serviceModel.setTypeCode(serviceModelVO.getTypeCode());
            serviceModel.setCreateBy(serviceModelVO.getCreateBy());
            serviceModel.setCreateTime(serviceModelVO.getCreateTime());
            serviceModel.setUpdateBy(serviceModelVO.getUpdateBy());
            serviceModel.setUpdateTime(serviceModelVO.getUpdateTime());
            serviceModel.setBodyPattern(serviceModelVO.getBodyPattern());

            return serviceModel;
        }
        return null;
    }


    /**
     * 将DataModel转化为DataModelVO
     *
     * @param
     * @return
     */
    private ServiceModelVO parseModel2AssetmodelVO(ServiceModel serviceModel, Map<String, Object> serviceSource,
                                                   Map<String, Object> dataAssetTopic, Map<String, Object> dataAssetArea,
                                                   List<ServiceModelField> serviceModelFields, List<ServiceModelParam> headerParams,
                                                   List<ServiceModelParam> bodyParams, List<ServiceModelParam> serviceModelParams) {
        if (serviceModel != null) {
            ServiceModelVO serviceModelVO = new ServiceModelVO();
            serviceModelVO.setId(serviceModel.getId());
            serviceModelVO.setParentId(serviceModel.getParentId());
            serviceModelVO.setServiceSourceId(serviceModel.getServiceSourceId());
            serviceModelVO.setTopicId(serviceModel.getTopicId());
            serviceModelVO.setModelName(serviceModel.getModelName());
            serviceModelVO.setModelDesc(serviceModel.getModelDesc());
            serviceModelVO.setCacheDuration(serviceModel.getCacheDuration());
            serviceModelVO.setExpression(serviceModel.getExpression());
            serviceModelVO.setExpressionType(serviceModel.getExpressionType());
            serviceModelVO.setRequestMethod(serviceModel.getRequestMethod());
            serviceModelVO.setStatusCode(serviceModel.getStatusCode());
            serviceModelVO.setTypeCode(serviceModel.getTypeCode());
            serviceModelVO.setCreateBy(serviceModel.getCreateBy());
            serviceModelVO.setCreateTime(serviceModel.getCreateTime());
            serviceModelVO.setUpdateBy(serviceModel.getUpdateBy());
            serviceModelVO.setUpdateTime(serviceModel.getUpdateTime());
            serviceModelVO.setCreateUser(serviceModel.getCreateUser());
            serviceModelVO.setUpdateUser(serviceModel.getUpdateUser());
            serviceModelVO.setBodyPattern(serviceModel.getBodyPattern());
            if (serviceModel.getParentId() != null) {
                ServiceModel parentModel = serviceModelMapper.queryByPrimaryKey(serviceModel.getParentId());
                if (parentModel != null) {
                    serviceModelVO.setParentModelName(parentModel.getModelName());
                }
            }

            serviceModelVO.setServiceSource(serviceSource);
            serviceModelVO.setDataAssetArea(dataAssetArea);
            serviceModelVO.setDataAssetTopic(dataAssetTopic);
            serviceModelVO.setServiceModelFields(serviceModelFields);
            serviceModelVO.setServiceModelParams(serviceModelParams);
            serviceModelVO.setFieldLoad(false);
            return serviceModelVO;
        }
        return null;
    }

}
