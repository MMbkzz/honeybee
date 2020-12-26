package com.stackstech.honeybee.server.quality.repo;

import com.stackstech.honeybee.server.quality.entity.JobInstanceBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.stackstech.honeybee.server.quality.entity.LivySessionStates.State;

public interface JobInstanceRepo extends BaseJpaRepository<JobInstanceBean, Long> {

    JobInstanceBean findByPredicateName(String name);

    @Query("select s from JobInstanceBean s where s.id = ?1")
    JobInstanceBean findByInstanceId(Long id);

    @Query("select s from JobInstanceBean s where s.job.id = ?1")
    List<JobInstanceBean> findByJobId(Long jobId, Pageable pageable);

    @Query("select s from JobInstanceBean s where s.job.id = ?1")
    List<JobInstanceBean> findByJobId(Long jobId);

    List<JobInstanceBean> findByExpireTmsLessThanEqual(Long expireTms);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("delete from JobInstanceBean j " +
            "where j.expireTms <= ?1 and j.deleted = false ")
    int deleteByExpireTimestamp(Long expireTms);

    @Query("select DISTINCT s from JobInstanceBean s where s.state in ?1")
    List<JobInstanceBean> findByActiveState(State[] states);

    List<JobInstanceBean> findByTriggerKey(String triggerKey);
}
