package com.stackstech.honeybee.server.quality.service;

import org.apache.hadoop.hive.metastore.api.Table;

import java.util.List;
import java.util.Map;

public interface HiveMetaStoreService {

    Iterable<String> getAllDatabases();

    Iterable<String> getAllTableNames(String dbName);

    Map<String, List<String>> getAllTableNames();

    List<Table> getAllTable(String db);

    Map<String, List<Table>> getAllTable();

    Table getTable(String dbName, String tableName);

    void evictHiveCache();
}
