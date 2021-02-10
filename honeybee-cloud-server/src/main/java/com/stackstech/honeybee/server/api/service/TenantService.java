package com.stackstech.honeybee.server.api.service;

import com.stackstech.honeybee.common.entity.DataAuthorityMeta;
import com.stackstech.honeybee.common.entity.JsonParameterList;
import com.stackstech.honeybee.server.api.entity.DataServiceAuthorityEntity;
import com.stackstech.honeybee.server.api.entity.DataServiceTenantEntity;
import com.stackstech.honeybee.server.core.service.BaseDataService;

import java.util.List;

public interface TenantService extends BaseDataService<DataServiceTenantEntity> {

    List<DataServiceAuthorityEntity> getAuthorityList(Long tenantId);

    JsonParameterList getDataAuthorityMeta(Long authorityId, Long dataServiceId);

    boolean updateDataAuthorityMeta(Long authorityId, List<DataAuthorityMeta> dataAuthorityMete, Long ownerId);

    boolean deleteDataAuthority(Long authorityId, Long ownerId);

    DataServiceAuthorityEntity addDataAuthority(Long tenantId, Long dataServiceId, Long ownerId);
}
