package com.stackstech.honeybee.server.quality.repo;

import com.stackstech.honeybee.server.quality.entity.DataConnector;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DataConnectorRepo extends CrudRepository<DataConnector, Long> {

    @Query("select dc from DataConnector dc where dc.name in ?1")
    List<DataConnector> findByConnectorNames(List<String> names);
}
