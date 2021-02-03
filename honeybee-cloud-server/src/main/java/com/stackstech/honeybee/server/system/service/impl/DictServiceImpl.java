package com.stackstech.honeybee.server.system.service.impl;

import com.stackstech.honeybee.server.core.enums.DictCatalog;
import com.stackstech.honeybee.server.system.dao.DictMapper;
import com.stackstech.honeybee.server.system.entity.DictEntity;
import com.stackstech.honeybee.server.system.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper mapper;

    @Override
    public List<DictEntity> getDictByCatalog(@Nullable DictCatalog catalog) {
        if (catalog != null) {
            return mapper.selectByCatalogName(catalog.name());
        }
        return mapper.selectByCatalogName(null);
    }
}
