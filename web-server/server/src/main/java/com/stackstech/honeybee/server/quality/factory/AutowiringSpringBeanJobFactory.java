package com.stackstech.honeybee.server.quality.factory;

import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * Auto-wiring SpringBeanJobFactory is special  BeanJobFactory that adds auto-wiring support against
 * {@link SpringBeanJobFactory} allowing you to inject properties from the scheduler context, job data map
 * and trigger data entries into the job bean.
 *
 * @see SpringBeanJobFactory
 * @see ApplicationContextAware
 */
public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory
        implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AutowiringSpringBeanJobFactory.class);

    private transient AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle) {
        try {
            final Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        } catch (Exception e) {
            LOGGER.error("fail to create job instance. {}", e);
        }
        return null;
    }
}
