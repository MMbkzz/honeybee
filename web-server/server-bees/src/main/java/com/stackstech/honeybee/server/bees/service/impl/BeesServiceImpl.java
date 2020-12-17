package com.stackstech.honeybee.server.bees.service.impl;


import com.stackstech.honeybee.server.bees.entity.Bees;
import com.stackstech.honeybee.server.bees.entity.ExternalBees;
import com.stackstech.honeybee.server.bees.entity.HoneyBees;
import com.stackstech.honeybee.server.bees.exception.BeesException;
import com.stackstech.honeybee.server.bees.repo.BeesRepo;
import com.stackstech.honeybee.server.bees.repo.BeesRepoService;
import com.stackstech.honeybee.server.bees.repo.ExternalBeesRepo;
import com.stackstech.honeybee.server.bees.service.BeesService;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.stackstech.honeybee.server.bees.exception.BeesExceptionMessage.*;

@Service
public class BeesServiceImpl implements BeesService {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(BeesServiceImpl.class);
    private static final String GRIFFIN = "honeybee";
    private static final String EXTERNAL = "external";

    @Autowired
    private BeesRepoService<Bees> beesRepoService;
    @Autowired
    private BeesRepo griffinMeasureRepo;
    @Autowired
    private ExternalBeesRepo externalMeasureRepo;
    @Autowired
    @Qualifier("griffinOperation")
    private BeesOperator griffinOp;
    @Autowired
    @Qualifier("externalOperation")
    private BeesOperator externalOp;

    @Override
    public List<? extends Bees> getAllAliveBees(String type) {
        if (type.equals(GRIFFIN)) {
            return griffinMeasureRepo.findByDeleted(false);
        } else if (type.equals(EXTERNAL)) {
            return externalMeasureRepo.findByDeleted(false);
        }
        return beesRepoService.findByDeleted(false);
    }

    @Override
    public Bees getBeesById(long id) {
        Bees measure = beesRepoService.findByIdAndDeleted(id, false);
        if (measure == null) {
            throw new BeesException
                    .NotFoundException(MEASURE_ID_DOES_NOT_EXIST);
        }
        return measure;
    }

    @Override
    public List<Bees> getAliveBeesByOwner(String owner) {
        return beesRepoService.findByOwnerAndDeleted(owner, false);
    }

    @Override
    public Bees createBees(Bees measure) {
        List<Bees> aliveMeasureList = beesRepoService
                .findByNameAndDeleted(measure.getName(), false);
        if (!CollectionUtils.isEmpty(aliveMeasureList)) {
            LOGGER.warn("Failed to create new measure {}, it already exists.",
                    measure.getName());
            throw new BeesException.ConflictException(
                    MEASURE_NAME_ALREADY_EXIST);
        }
        BeesOperator op = getOperation(measure);
        return op.create(measure);
    }

    @Override
    public Bees updateBees(Bees measure) {
        Bees m = beesRepoService.findByIdAndDeleted(measure.getId(), false);
        if (m == null) {
            throw new BeesException.NotFoundException(
                    MEASURE_ID_DOES_NOT_EXIST);
        }
        if (!m.getType().equals(measure.getType())) {
            LOGGER.warn("Can't update measure to different type.");
            throw new BeesException.BadRequestException(
                    MEASURE_TYPE_DOES_NOT_MATCH);
        }
        BeesOperator op = getOperation(measure);
        return op.update(measure);
    }

    @Override
    public void deleteBeesById(Long measureId) throws SchedulerException {
        Bees measure = beesRepoService.findByIdAndDeleted(measureId, false);
        if (measure == null) {
            throw new BeesException.NotFoundException(
                    MEASURE_ID_DOES_NOT_EXIST);
        }
        BeesOperator op = getOperation(measure);
        op.delete(measure);
    }

    @Override
    public void deleteBees() throws SchedulerException {
        List<Bees> measures = beesRepoService.findByDeleted(false);
        for (Bees m : measures) {
            BeesOperator op = getOperation(m);
            op.delete(m);
        }
    }

    private BeesOperator getOperation(Bees measure) {
        if (measure instanceof HoneyBees) {
            return griffinOp;
        } else if (measure instanceof ExternalBees) {
            return externalOp;
        }
        throw new BeesException.BadRequestException(
                MEASURE_TYPE_DOES_NOT_SUPPORT);
    }

}
