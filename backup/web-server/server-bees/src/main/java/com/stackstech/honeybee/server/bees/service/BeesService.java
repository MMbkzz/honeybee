package com.stackstech.honeybee.server.bees.service;


import com.stackstech.honeybee.server.bees.entity.Bees;
import org.quartz.SchedulerException;

import java.util.List;

public interface BeesService {

    List<? extends Bees> getAllAliveBees(String type);

    Bees getBeesById(long id);

    void deleteBeesById(Long id) throws SchedulerException;

    void deleteBees() throws SchedulerException;

    Bees updateBees(Bees measure);

    List<Bees> getAliveBeesByOwner(String owner);

    Bees createBees(Bees measure);
}
