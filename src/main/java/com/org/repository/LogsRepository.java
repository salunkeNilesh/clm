package com.org.repository;

import com.org.entity.LogsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;


@Repository
public interface LogsRepository extends JpaRepository<LogsEntity, Long> {

    @Query("select l from LogsEntity l")
    Collection<LogsEntity> getLogs();

    @Query("select l from LogsEntity l where l.applicationId=?1")
    Collection<LogsEntity> findLogs(BigDecimal appId);

    @Query("select l from LogsEntity l where l.applicationName=?1")
    Collection<LogsEntity> findLogs(String appName);

    @Query("select l from LogsEntity l where l.applicationId=?1 AND applicationName=?2")
    Collection<LogsEntity> findLogs(BigDecimal appId, String appName);

    @Query(value = "?1",nativeQuery = true)
    Collection<LogsEntity> executeFilterQuery(String sqlQuery);

  /*  @Query(value = "select * from logs ",nativeQuery = true)
    Collection<LogsEntity> executeFilterQuery(String sqlQuery);*/

}
