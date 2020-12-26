package com.stackstech.honeybee.server.quality.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseJpaRepository<T, ID> extends JpaRepository<T, ID> {
    /**
     * This method is to make findOne method from spring boot 1.5.x compatible with spring 2.x
     *
     * @param id id
     * @return object of specified id, return null if cannot find the id
     */
    default T findOne(ID id) {
        return (T) findById(id).orElse(null);
    }
}
