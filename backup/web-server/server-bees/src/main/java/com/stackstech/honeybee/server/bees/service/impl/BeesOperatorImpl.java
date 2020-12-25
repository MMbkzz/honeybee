package com.stackstech.honeybee.server.bees.service.impl;

import com.stackstech.honeybee.server.bees.entity.Bees;
import com.stackstech.honeybee.server.bees.repo.BeesRepoService;
import com.stackstech.honeybee.server.bees.service.BeesOperator;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.stackstech.honeybee.server.bees.util.BeesUtil.validateMeasure;

@Component("BeesOperation")
public class BeesOperatorImpl implements BeesOperator {
    private final BeesRepoService<Bees> beesRepoService;

    private final JobServiceImpl jobService;

    @Autowired
    public BeesOperatorImpl(BeesRepoService<Bees> beesRepoService,
                            JobServiceImpl jobService) {
        this.beesRepoService = beesRepoService;
        this.jobService = jobService;
    }


    @Override
    public Bees create(Bees measure) {
        validateMeasure(measure);
        return beesRepoService.save(measure);
    }

    @Override
    public Bees update(Bees measure) {
        validateMeasure(measure);
        measure.setDeleted(false);
        measure = beesRepoService.save(measure);
        return measure;
    }

    @Override
    public void delete(Bees measure) throws SchedulerException {
        jobService.deleteJobsRelateToMeasure(measure.getId());
        measure.setDeleted(true);
        beesRepoService.save(measure);
    }
}
