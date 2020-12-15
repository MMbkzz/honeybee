package com.stackstech.honeybee.connector.hbase;


import com.stackstech.honeybee.connector.core.AbstractPoolSession;
import com.stackstech.honeybee.connector.core.DriverPoolFactory;
import com.stackstech.honeybee.connector.core.ResourceSession;
import com.stackstech.honeybee.connector.core.entity.DriverDataModel;
import com.stackstech.honeybee.connector.core.entity.DriverMetaData;
import com.stackstech.honeybee.connector.core.entity.DriverModel;
import com.stackstech.honeybee.connector.core.enums.MetaDataTypeEnum;
import com.stackstech.honeybee.connector.core.util.SqlTemplateUtil;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class HbaseSession extends AbstractPoolSession implements ResourceSession {

    private static final Logger logger = LoggerFactory.getLogger(HbaseSession.class);

    private Configuration conf = null;

    private final HTable tableTemplate = null;

    private Connection connection = null;

    private HbaseSession(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static HbaseSession getInstance(ClassLoader classLoader) {
        return new HbaseSession(classLoader);
    }

    public HbaseSession initialize(Map<String, Object> config) throws IOException {
        pool = new GenericObjectPool(new DriverPoolFactory());
        pool.setMaxTotal(Integer.parseInt((String) config.get("hbase.datasource.max-total")));
        pool.setMaxIdle(Integer.parseInt((String) config.get("hbase.datasource.max-idle")));
        pool.setMinIdle(Integer.parseInt((String) config.get("hbase.datasource.min-idle")));
        if (config.get("hbase.datasource.maxwait") != null) {
            pool.setMaxWaitMillis(Integer.parseInt((String) config.get("hbase.datasource.maxwait")) * 1000);
        }
        conf = HBaseConfiguration.create();
        conf.set("hbase.rootdir", (String) config.get("hbase.datasource.rootdir"));
        conf.set("hbase.zookeeper.quorum", (String) config.get("hbase.datasource.zookeeper.quorum"));
        conf.set("zookeeper.znode.parent", (String) config.get("hbase.datasource.zookeeper.znode.parent"));

        conf.setBoolean("hadoop.security.authorization", Boolean.parseBoolean((String) config.get("hbase.security.authentication")));
        //kerbos验证
        if (config.get("hbase.security.authentication") != null) {
            conf.set("hadoop.security.authentication", (String) config.get("hbase.security.authentication"));
            conf.set("hbase.security.authentication", (String) config.get("hbase.security.authentication"));
            //conf.set("hbase.security.kerberos.principal",(String) config.get("hbase.security.kerberos.principal"));
            conf.set("hbase.master.kerberos.principal", (String) config.get("hbase.master.kerberos.principal"));
            conf.set("hbase.regionserver.kerberos.principal", (String) config.get("hbase.regionserver.kerberos.principal"));
            try {
                UserGroupInformation.setConfiguration(conf);
                UserGroupInformation.loginUserFromKeytab((String) config.get("hbase.security.kerberos.principal"),
                        (String) config.get("hbase.security.keytab"));
            } catch (IOException e) {
                logger.error("驱动kerbors验证失败", e);
            }
        }
        connection = ConnectionFactory.createConnection(conf);
        return this;
    }

    @Override
    public List<Map<String, Object>> get(String statement) { //获取表列表

        if ("hbase".equals(statement)) {
            return this.getTables();
        } else {
            return this.getFields(statement); //根据表名获取所有属性
        }
    }

    @Override
    public DriverMetaData get(DriverModel driverModel) {

        DriverDataModel model = (DriverDataModel) driverModel;
        Map<String, Object> nameValueMap = (Map<String, Object>) model.getRequestData();

        List<Map<String, Object>> listParam = (List<Map<String, Object>>) nameValueMap.get("special");
        String rowkey = getRowKey(listParam);

        if (rowkey == null) {
            return new DriverMetaData(MetaDataTypeEnum.DATA, "rowkey不能为空");
        }

        String tableName = driverModel.getExpression();
        Result result = null;
        Get get = null;
        Object borrow = null;
        try {
            borrow = pool.borrowObject();
            //tableTemplate = new HTable(conf, tableName);
            Table table = connection.getTable(TableName.valueOf(tableName));
            get = new Get(rowkey.getBytes());
            //get.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("billImage"));
            //get.addFamily(Bytes.toBytes(""));
            //result = tableTemplate.get(get);
            result = table.get(get);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            pool.returnObject(borrow);
        }

        List<Map<String, Object>> fieldsResult = SqlTemplateUtil.removeField(parseResult(result, rowkey),
                (Map<String, Object>) nameValueMap.get("fields"));
        return new DriverMetaData(MetaDataTypeEnum.DATA, fieldsResult);
    }

    @Override
    public DriverMetaData put(DriverModel driverModel) {
        return null;
    }

    @Override
    public boolean valid() {
        return this.getTables().size() > 0;
    }

    @Override
    public void close() {

        try {
            tableTemplate.close();
            pool.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 获取表的所有字段
     *
     * @param tablename
     * @return
     */
    private List<Map<String, Object>> getFields(String tablename) {
        ResultScanner rs = null;
        HTable htable = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Scan scan = new Scan();
            htable = new HTable(conf, tablename);
            rs = htable.getScanner(scan);

            Map<String, Object> maprowkey = new HashMap<>();
            maprowkey.put("col_name", "rowkey");
            list.add(maprowkey);

            Arrays.stream(rs.next().rawCells()).forEach(cell -> {
                Map<String, Object> map = new HashMap<>();
                map.put("col_name", new String(CellUtil.cloneFamily(cell)) + ":" + new String(CellUtil.cloneQualifier(cell)));
                list.add(map);
            });
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            try {
                if (htable != null) {
                    htable.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return list;
    }


    /**
     * 获取表列表
     *
     * @return
     */
    private List<Map<String, Object>> getTables() {
        HBaseAdmin hba = null;
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            hba = new HBaseAdmin(conf);
            TableName[] listTableNames = hba.listTableNames();

            Arrays.stream(listTableNames).forEach(tableName -> {
                Map<String, Object> map = new HashMap<>();
                map.put(tableName.toString(), tableName.toString());
                list.add(map);
            });
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                hba.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return list;
    }

    /**
     * 获取rowkey
     *
     * @param list
     * @return
     */
    private String getRowKey(List<Map<String, Object>> list) {

        for (Map<String, Object> map : list) {
            if ("rowkey".equals(map.get("field").toString().toLowerCase())) {
                return map.get("value").toString();
            }
        }
        return null;
    }

    private List<Map<String, Object>> parseResult(Result result, String rowkeyValue) {

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        for (Cell cell : result.rawCells()) {
            map.put(new String(CellUtil.cloneFamily(cell)) + ":" + new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
        }
        map.put("rowkey", rowkeyValue);
        list.add(map);
        return list;
    }

}
