package com.stackstech.dcp.connector.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stackstech.dcp.connector.core.entity.DriverMetaData;
import com.stackstech.dcp.connector.core.entity.DriverModel;
import com.stackstech.dcp.connector.core.exception.RessourceSessionException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 资源会话管理
 * 用与管理资源会话、资源类加载
 */
public class ResourceSessionManager {

    private final Logger logger = LoggerFactory.getLogger(ResourceSessionManager.class);

    /**
     * 资源会话工程池
     */
    private static final Map<String, ResourceSessionFactory> resource = Maps.newHashMap();

    /**
     * 驱动包类加载器缓存
     */
    private static final Map<String, ClassLoader> classLoaderCache = Maps.newHashMap();

    private String uploadFilePath;

    private final List<AbstractResourceObserver> resourceObservers = Lists.newArrayList();

    public ResourceSessionManager(String uploadFilePath) {
        if (StringUtils.isNotEmpty(uploadFilePath)) {
            this.uploadFilePath = uploadFilePath;
        }
    }

    /**
     * 获取所有资源会话工厂
     *
     * @return
     */
    public Map<String, ResourceSessionFactory> getResource() {
        return resource;
    }

    /**
     * 获取指定资源会话工厂
     *
     * @param resourceId
     * @return
     */
    public ResourceSessionFactory getResource(String resourceId) {
        ResourceSessionFactory sessionFactory = resource.get(resourceId);

        return sessionFactory;
    }

    /**
     * 打开新的资源会话
     *
     * @param resourceId 资源编号
     * @param config     配置信息
     * @param jarFile    jar包文件路径
     * @param mainClass  驱动执行主类
     * @return
     */
    public ResourceSessionFactory openSession(String resourceId, Map<String, Object> config, String jarFile, String mainClass) {
        return openSession(resourceId, config, jarFile, mainClass, true);
    }

    /**
     * 打开新的资源会话
     *
     * @param resourceId 资源编号
     * @param config     配置信息
     * @param jarFile    jar包文件路径
     * @param mainClass  驱动执行主类
     * @param serialize  是否保存
     * @return
     */
    public ResourceSessionFactory openSession(String resourceId, Map<String, Object> config, String jarFile, String mainClass, boolean serialize) {
        ResourceSessionFactory sessionFactory = null;
        URLClassLoader classLoader = null;
        try {
            // 获取类加载器
            classLoader = (URLClassLoader) getClassLoader(jarFile);
            // 类加载器加载主类
            Class<?> targetClass = classLoader.loadClass(mainClass);
            // 实例化主类
            sessionFactory = (ResourceSessionFactory) targetClass.newInstance();

        } catch (MalformedURLException e) {
            throw new RessourceSessionException("驱动包读取异常", e);
        } catch (ClassNotFoundException e) {
            throw new RessourceSessionException("驱动包未找到 - " + jarFile, e);
        } catch (IllegalAccessException e) {
            throw new RessourceSessionException("驱动包初始化异常", e);
        } catch (InstantiationException e) {
            throw new RessourceSessionException("驱动包初始化异常" + jarFile, e);
        } catch (IOException e) {
            throw new RessourceSessionException("驱动包读取异常", e);
        }

        // 注册
        return regist(resourceId, sessionFactory.openSession(classLoader, config), serialize);
    }

    /**
     * 获取类加载器
     *
     * @param jarFile jar包文件路径
     * @return
     * @throws IOException
     */
    protected ClassLoader getClassLoader(String jarFile) throws IOException {
        // 从缓存获取类加载器
        ClassLoader classLoader = classLoaderCache.get(jarFile);
        if (null != classLoader) {
            return classLoader;
        }

        // 在本地创建文件
        File file = new File(uploadFilePath + jarFile);
        // 文件不存在 - 创建动作
        if (!file.exists()) {
            throw new FileNotFoundException("Driver jar not found, please check the file path [" + file.getPath() + "]");
        }

        URL url = file.toURI().toURL();
        // 创建类加载器
        classLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
        // 类加载器写入缓存
        classLoaderCache.put(jarFile, classLoader);
        return classLoader;
    }

    @Deprecated
    public ResourceSessionFactory reconfigure(String resourceId, Map<String, Object> config) {
//        ResourceSessionFactory sessionFactory = getResource(resourceId);
//
//        return regist(resourceId, sessionFactory.reconfigure(config), true);

        return null;
    }

    private ResourceSessionFactory regist(String resourceId, ResourceSessionFactory sessionFactory, boolean serialize) {
        if (serialize) {
            resource.put(resourceId, sessionFactory);
        }

        notifyUpdate(resourceId, sessionFactory);

        return sessionFactory;
    }

    public boolean valid(String resourceId) {
        return valid(getResource(resourceId).getSession());
    }

    public boolean valid(ResourceSession resourceSession) {
        return resourceSession.valid();
    }

    public void close(String resourceId) {
        try {
            getResource(resourceId).getSession().close();

            resource.remove(resourceId);

            notifyClose(resourceId);
        } catch (Exception e) {
            logger.error("Resource release exception:" + resourceId, e);
        }
    }

    public void closeAll() {
        Set<String> resourceIds = new HashSet<>(resource.keySet());

        for (String resourceId : resourceIds) {
            close(resourceId);
        }
    }

    public void attach(AbstractResourceObserver observer) {
        this.resourceObservers.add(observer);
    }

    public void notifyAdd(String resourceId, ResourceSessionFactory sessionFactory) {
        for (AbstractResourceObserver observer : this.resourceObservers) {
            observer.add(resourceId, sessionFactory);
        }
    }

    public void notifyUpdate(String resourceId, ResourceSessionFactory sessionFactory) {
        for (AbstractResourceObserver observer : this.resourceObservers) {
            observer.update(resourceId, sessionFactory);
        }
    }

    public void notifyClose(String resourceId) {
        for (AbstractResourceObserver observer : this.resourceObservers) {
            observer.close(resourceId);
        }
    }


    public Object get(String resourceId, String statement) {
        return get(getResource(resourceId).getSession(), statement);
    }

    public Object get(ResourceSession resourceSession, String statement) {
        return resourceSession.get(statement);
    }

    public DriverMetaData put(String resourceId, DriverModel driverModel) {
        return put(getResource(resourceId).getSession(), driverModel);
    }

    public DriverMetaData put(ResourceSession resourceSession, DriverModel driverModel) {
        return resourceSession.put(driverModel);
    }

}
