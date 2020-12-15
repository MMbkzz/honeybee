package com.stackstech.honeybee.server.system.scheduler;

import com.stackstech.honeybee.core.constants.CronConstant;
import com.stackstech.honeybee.server.system.scheduler.task.InstanceCheckTask;
import com.stackstech.honeybee.server.system.scheduler.task.ResourceCheckTask;
import com.stackstech.honeybee.server.system.util.ClientIPUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @tips : 资源-实例检查调度
 * @Content: 1.记录告警信息  2.发送告警邮件
 */
@Component
@EnableScheduling
public class ResourceCheckScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ResourceCheckScheduler.class);

    /*@Autowired
    private RedissonManager redissonManager;*/

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ResourceCheckTask resourceCheckTask;

    @Autowired
    private InstanceCheckTask instanceCheckTask;


    @Scheduled(cron = CronConstant.MAIN_RESOURCE_CHECK)
    public void schedule() {
        if (!ClientIPUtil.ipCompare(serviceUrl("dcp-server"))) {
            return;
        }
        resourceCheckTask.run();
        instanceCheckTask.run();
    }


    private List<URI> serviceUrl(String serviceName) {

        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(serviceName);

        List<URI> urlList = new ArrayList<URI>();

        if (CollectionUtils.isNotEmpty(serviceInstanceList)) {
            for (ServiceInstance serviceInstance : serviceInstanceList) {
                urlList.add(serviceInstance.getUri());
            }
        }
        return urlList;
    }


    //@Scheduled(cron = "0 */1 * * * ?")
    /*测试Redission//
    public void testRedisson() {
        RLock lock = redissonManager.getRedisson().getLock("testLock");
        boolean getLock = false;
        try{
            if (getLock = lock.tryLock(0,10, TimeUnit.SECONDS)) {
                logger.info("count ==="  + "获取锁" + Thread.currentThread().getName());
                sendEmailUtil.sendMail("测试数据源", " >>测试数据>> " + System.currentTimeMillis() + "===="
                                                        + Thread.currentThread().getName() + " >>> " );
            } else {
                logger.info("Redisson分布式锁没有获取到锁:{},ThreadName :{}", "testLock", Thread.currentThread().getName());
            }
        } catch (Exception e) {
            logger.error("Redisson 获取分布式锁异常",e);
            e.printStackTrace();
        } finally {
            if (!getLock) {
                return;
            }
            lock.unlock();
            logger.info("Redisson分布式锁释放锁:{},ThreadName :{}", "testLock", Thread.currentThread().getName());
        }
    }*/


}
