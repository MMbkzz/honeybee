package com.stackstech.honeybee.server.system.service;

import com.stackstech.honeybee.server.core.enums.DictCatalog;
import com.stackstech.honeybee.server.system.entity.DictEntity;

import javax.annotation.Nullable;
import java.util.List;

public interface DictService {

    List<DictEntity> getDictByCatalog(@Nullable DictCatalog catalog);

}
