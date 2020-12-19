package com.stackstech.honeybee.server.bees.service;


import com.stackstech.honeybee.server.bees.entity.Bees;
import org.quartz.SchedulerException;

public interface BeesOperator {

    Bees create(Bees measure);

    Bees update(Bees measure);

    void delete(Bees measure) throws SchedulerException;

}
