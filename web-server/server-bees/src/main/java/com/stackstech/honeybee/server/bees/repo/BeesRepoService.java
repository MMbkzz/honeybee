package com.stackstech.honeybee.server.bees.repo;


import com.stackstech.honeybee.server.bees.entity.Bees;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Interface to access measure repository
 *
 * @param <T> Measure and its subclass
 */
public interface BeesRepoService<T extends Bees> extends BaseJpaRepository<T, Long> {

    /**
     * search repository by name and deletion state
     *
     * @param name    query condition
     * @param deleted query condition
     * @return measure collection
     */
    List<T> findByNameAndDeleted(String name, Boolean deleted);

    /**
     * search repository by deletion state
     *
     * @param deleted query condition
     * @return measure collection
     */
    List<T> findByDeleted(Boolean deleted);

    /**
     * search repository by owner and deletion state
     *
     * @param owner   query condition
     * @param deleted query condition
     * @return measure collection
     */
    List<T> findByOwnerAndDeleted(String owner, Boolean deleted);

    /**
     * search repository by id and deletion state
     *
     * @param id      query condition
     * @param deleted query condition
     * @return measure collection
     */
    T findByIdAndDeleted(Long id, Boolean deleted);

    /**
     * search repository by deletion state
     *
     * @param deleted query condition
     * @return organization collection
     */
    @Query("select DISTINCT m.organization from #{#entityName} m "
            + "where m.deleted = ?1 and m.organization is not null")
    List<String> findOrganizations(Boolean deleted);

    /**
     * search repository by organization and deletion state
     *
     * @param organization query condition
     * @param deleted      query condition
     * @return organization collection
     */
    @Query("select m.name from #{#entityName} m "
            + "where m.organization= ?1 and m.deleted= ?2")
    List<String> findNameByOrganization(String organization, Boolean deleted);
}
