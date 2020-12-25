package com.stackstech.honeybee.server.bees.repo;

import com.stackstech.honeybee.server.bees.entity.AbstractJob;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobRepo<T extends AbstractJob> extends BaseJpaRepository<T, Long> {

    @Query("select count(j) from #{#entityName} j " +
            "where j.jobName = ?1 and j.deleted = ?2")
    int countByJobNameAndDeleted(String jobName, Boolean deleted);

    List<T> findByDeleted(boolean deleted);

    List<T> findByJobNameAndDeleted(String jobName, boolean deleted);

    List<T> findByMeasureIdAndDeleted(Long measureId, boolean deleted);

    T findByIdAndDeleted(Long jobId, boolean deleted);
}
