package com.stackstech.honeybee.server.api.service;

import com.stackstech.honeybee.common.entity.DataAuthorityMeta;
import com.stackstech.honeybee.common.entity.JsonParameterList;
import com.stackstech.honeybee.server.api.entity.DataServiceAuthorityEntity;
import com.stackstech.honeybee.server.api.entity.DataServiceTenantEntity;
import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
import com.stackstech.honeybee.server.core.service.BaseDataService;

import java.util.List;

public interface TenantService extends BaseDataService<DataServiceTenantEntity> {

    List<DataServiceAuthorityEntity> getAuthorityList(Long tenantId) throws ServerException, DataNotFoundException;

    JsonParameterList getDataAuthorityMeta(Long authorityId, Long dataServiceId) throws ServerException, DataNotFoundException;

    boolean updateDataAuthorityMeta(Long authorityId, List<DataAuthorityMeta> dataAuthorityMete, Long ownerId) throws ServerException;

    boolean deleteDataAuthority(Long authorityId, Long ownerId) throws ServerException;

    DataServiceAuthorityEntity addDataAuthority(Long tenantId, Long dataServiceId, Long ownerId) throws ServerException, DataNotFoundException;
}
