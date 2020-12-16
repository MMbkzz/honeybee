package com.stackstech.honeybee.server.dataasset.service.impl;

import com.stackstech.honeybee.server.dataasset.dao.ModelCodeMapper;
import com.stackstech.honeybee.server.dataasset.model.ModelCode;
import com.stackstech.honeybee.server.dataasset.service.ModelCodeService;
import com.stackstech.honeybee.server.dataasset.vo.ModelCodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模型快码Service
 */
@Service
public class ModelCodeServiceImpl implements ModelCodeService {

    @Autowired
    private ModelCodeMapper modelCodeMapper;

    /**
     * 获取模型主题和领域
     *
     * @param parentCode
     * @return
     */
    @Override
    public List<ModelCode> queryAll(String parentCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("parentCode", parentCode);
        List<ModelCode> modelCodes = modelCodeMapper.queryAll(map);
        return modelCodes;
    }

    @Override
    public ModelCode query(Long id) {
        return modelCodeMapper.queryByPrimaryKey(id);
    }

    /**
     * 获取状态码
     *
     * @param type
     * @return
     */
    @Override
    public List<ModelCode> queryStatus(String type) {
        return modelCodeMapper.queryByType(type);
    }

    /**
     * 递归获取ModelCode
     *
     * @param parentCode
     * @return
     */
    private List<ModelCodeVO> queryCodesInDeep(String parentCode) {

        List<ModelCodeVO> lists = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("parentCode", parentCode);
        List<ModelCode> modelCodes = modelCodeMapper.queryAll(map);
        if (modelCodes != null && modelCodes.size() > 0) {
            for (ModelCode modelCode : modelCodes) {
                ModelCodeVO modelCodeVO = parseModel2VO(modelCode);
                List<ModelCodeVO> childs = queryCodesInDeep(modelCodeVO.getCode());
                if (childs != null && childs.size() > 0) {
                    modelCodeVO.setModelCodes(childs);
                }
                lists.add(modelCodeVO);
            }
        }
        return lists;
    }

    /**
     * 将ModelCode转换成VO
     *
     * @param modelCode
     * @return
     */
    private ModelCodeVO parseModel2VO(ModelCode modelCode) {
        if (modelCode != null) {
            ModelCodeVO modelCodeVO = new ModelCodeVO();
            modelCodeVO.setId(modelCode.getId());
            modelCodeVO.setCode(modelCode.getCode());
            modelCodeVO.setCodeDesc(modelCode.getCodeDesc());
            modelCodeVO.setType(modelCode.getType());
            modelCodeVO.setParentCode(modelCode.getParentCode());
            modelCodeVO.setParentType(modelCode.getParentType());
            modelCodeVO.setDisplayName(modelCode.getDisplayName());
            modelCodeVO.setCreateBy(modelCode.getCreateBy());
            modelCodeVO.setCreateTime(modelCode.getCreateTime());
            modelCodeVO.setUpdateBy(modelCode.getUpdateBy());
            modelCodeVO.setUpdateTime(modelCode.getUpdateTime());

            return modelCodeVO;
        }
        return null;
    }

}
