package com.stackstech.honeybee.server.system.scheduler.task;

import com.stackstech.honeybee.core.enums.InstanceStageEnum;
import com.stackstech.honeybee.core.enums.InstanceStatusEnum;
import com.stackstech.honeybee.server.platform.dao.InstanceMapper;
import com.stackstech.honeybee.server.platform.dao.MessageMapper;
import com.stackstech.honeybee.server.platform.model.Instance;
import com.stackstech.honeybee.server.platform.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @tips : 实例检查Task
 */
@Component
public class InstanceCheckTask {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InstanceMapper instanceMapper;
    @Autowired
    private MessageMapper messageMapper;


    public void run() {

        logger.info("InstanceCheckTask start!!! 实例检查调度开始 >>> " + System.currentTimeMillis());

        Message message = new Message();
        message.setMessageLevel("warning");
        message.setTitle("实例检查告警");

        List<Instance> instances = instanceMapper.queryAll(new Instance());

        if (instances == null || instances.size() == 0) {
            logger.warn("InstanceCheckTask is running!!! 没有可用的实例");
            message.setContext("没有可用的实例");
            messageMapper.insert(message);
            return;
        }

        for (Instance instance : instances) {
            try {
                if (!InstanceStatusEnum.normal.code.equals(instance.getStatusCode()) ||
                        !InstanceStageEnum.online.code.equals(instance.getStageCode())) {
                    logger.warn("InstanceCheckTask is running!!! 实例 : " + instance.getId() + " >>> 状态异常,请检查实例状态");
                    message.setContext("实例 : " + instance.getId() + " >>> 状态异常,请检查实例状态");
                    messageMapper.insert(message);
                }
            } catch (Exception e) {
                logger.error("InstanceCheckTask is running!!! 实例检查告警 >>> 实例 : " + instance.getId() + " 程序运行异常 >>> e :" + e.toString());
                message.setContext("实例 : " + instance.getId() + " 程序运行异常 >>> e :" + e.toString());
                messageMapper.insert(message);
                e.printStackTrace();
            }
        }

        logger.info("InstanceCheckTask end!!! 实例检查调度结束 >>> " + System.currentTimeMillis());
    }

}
