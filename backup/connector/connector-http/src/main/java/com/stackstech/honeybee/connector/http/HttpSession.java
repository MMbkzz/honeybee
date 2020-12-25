package com.stackstech.honeybee.connector.http;


import com.stackstech.honeybee.connector.core.AbstractPoolSession;
import com.stackstech.honeybee.connector.core.DriverPoolFactory;
import com.stackstech.honeybee.connector.core.ResourceSession;
import com.stackstech.honeybee.connector.core.entity.DriverApiModel;
import com.stackstech.honeybee.connector.core.entity.DriverMetaData;
import com.stackstech.honeybee.connector.core.entity.DriverModel;
import com.stackstech.honeybee.connector.core.enums.MetaDataTypeEnum;
import com.stackstech.honeybee.connector.http.execute.HttpAdapter;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpSession extends AbstractPoolSession implements ResourceSession {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private HttpHost proxy = null;
    private final CloseableHttpClient httpclient = HttpClients.createDefault();

    private final Map<String, Object> paramHead = new HashMap<>();

    private HttpSession(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static HttpSession getInstance(ClassLoader classLoader) {
        return new HttpSession(classLoader);
    }

    public HttpSession initialize(Map<String, Object> config) {
        pool = new GenericObjectPool(new DriverPoolFactory());
        pool.setMaxTotal(Integer.valueOf((String) config.get("http.datasource.max-total")));
        pool.setMaxIdle(Integer.valueOf((String) config.get("http.datasource.max-idle")));
        pool.setMinIdle(Integer.valueOf((String) config.get("http.datasource.min-idle")));
        if (config.get("http.datasource.maxwait") != null) {
            pool.setMaxWaitMillis(Integer.valueOf((String) config.get("http.datasource.maxwait")) * 1000);
        }
        paramHead.put("Content-Type", config.get("http.datasource.Content-Type"));

        config.keySet().forEach(key -> {
            if (key.startsWith("http.datasource.head")) {
                paramHead.put(key.split("http.datasource.head.")[1], config.get(key));
            } else if (key.startsWith("http.datasource.proxy.hostname")) { //主机  端口共存
                String hostname = (String) config.get("http.datasource.proxy.hostname");
                int port = Integer.valueOf((String) config.get("http.datasource.proxy.port"));
                String httpType = (String) config.get("http.datasource.proxy.type");
                proxy = new HttpHost(hostname, port);
            }
        });
        return this;
    }

    @Override
    public List<Map<String, Object>> get(String statement) {
        return null;
    }

    @Override
    public DriverMetaData get(DriverModel driverModel) {

        DriverApiModel driverApiModel = (DriverApiModel) driverModel;

        String type = driverApiModel.getRequestMethod().toLowerCase();

        Object execute = null;

        Object borrow = null;
        try {
            borrow = pool.borrowObject();
            execute = new HttpAdapter(type).execute(httpclient, driverModel, paramHead, proxy);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            pool.returnObject(borrow);
        }

        return new DriverMetaData(MetaDataTypeEnum.NOSQL, execute);
    }

    @Override
    public DriverMetaData put(DriverModel driverModel) {
        return null;
    }

    @Override
    public boolean valid() {
        return true;
    }

    @Override
    public void close() {
        try {
            httpclient.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
