package com.org.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.constants.QueryTextConstants;
import com.org.entity.LogsEntity;
import com.org.models.LogMessageRequestVO;
import com.org.models.LogMessageResponseVO;
import com.org.repository.LogsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {

    @Autowired
    MessageService messageService;

    @Autowired
    LogsRepository logsRepository;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    public static final Logger logger= LoggerFactory.getLogger(MessageService.class);

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    public LogMessageResponseVO saveLogs(LogMessageRequestVO requestVO) {
        ObjectMapper mapper = new ObjectMapper();
        LogsEntity logsEntity = mapper.convertValue(requestVO, LogsEntity.class);
        LogMessageResponseVO responseVO = mapper.convertValue(logsRepository.save(logsEntity), LogMessageResponseVO.class);

        logger.info(responseVO.toString());

        return responseVO;

    }

    public Collection<LogsEntity> fetchRecordsFromDb(Map<String, Object> params) {
        Collection<LogsEntity> logRecords = null;

        if (params.containsKey(QueryTextConstants.APP_ID) & params.containsKey(QueryTextConstants.APP_NAME)) {
            logRecords = logsRepository.findLogs(new BigDecimal(params.get("appId").toString()), params.get(QueryTextConstants.APP_NAME).toString());
            params.remove(QueryTextConstants.APP_ID);
            params.remove(QueryTextConstants.APP_NAME);
        } else if (params.containsKey(QueryTextConstants.APP_ID)) {
            logRecords = logsRepository.findLogs(new BigDecimal(params.get("appId").toString()));
            params.remove(QueryTextConstants.APP_ID);
        } else if (params.containsKey(QueryTextConstants.APP_NAME)) {
            logRecords = logsRepository.findLogs(params.get(QueryTextConstants.APP_NAME).toString());
            params.remove(QueryTextConstants.APP_NAME);
        } else {
            logRecords = logsRepository.getLogs();
        }

        return logRecords;
    }


    public Collection<?> fetchLogs(Map<String, Object> params) {
        Collection<LogsEntity> logRecords = fetchRecordsFromDb(params);
        Collection<LogsEntity> result = new ArrayList<LogsEntity>();

        if (!params.isEmpty()) {

            for (LogsEntity log : logRecords) {
                if (log.getMessage().keySet().containsAll(params.keySet())) {
                    if (checkPairsForExactValue(log.getMessage(), params)) {
                        result.add(log);
                    }
                }

            }
        } else {
            result = logRecords;
        }

        return result;
    }


    public String createSqlQueryForAppIDAppName(Map<String, Object> params) {
        String sqlQuery = "select * from logs where ";

        if (!params.isEmpty()) {
            if ((params.containsKey(QueryTextConstants.APP_ID)) && (params.containsKey(QueryTextConstants.APP_NAME))) {
                sqlQuery = sqlQuery + "application_id=" + params.get(QueryTextConstants.APP_ID);
                sqlQuery = sqlQuery + "and application_name='" + params.get(QueryTextConstants.APP_NAME) + "'";
                params.remove(QueryTextConstants.APP_ID);
                params.remove(QueryTextConstants.APP_NAME);
            } else if (params.containsKey(QueryTextConstants.APP_ID)) {
                sqlQuery = sqlQuery + "application_id=" + params.get(QueryTextConstants.APP_ID);
                params.remove(QueryTextConstants.APP_ID);
            } else if (params.containsKey(QueryTextConstants.APP_NAME)) {
                sqlQuery = sqlQuery + "application_name='" + params.get(QueryTextConstants.APP_NAME) + "'";
                params.remove(QueryTextConstants.APP_NAME);
            }
        }

        return sqlQuery;
    }


    public Collection<?> filterLogs(Map<String, Object> params) {
        Collection<LogsEntity> result = new ArrayList<LogsEntity>();

        String sqlQuery = createSqlQueryForAppIDAppName(params);
        ArrayList<String> sqlFiltersToAdd = new ArrayList<String>();

        params.remove(QueryTextConstants.APP_ID);
        params.remove(QueryTextConstants.APP_NAME);

        if (!params.isEmpty()) {
            String keys[] = params.keySet().toArray(new String[0]);
            for (String key : keys) {
                // .*"message": ["]{0,1}[:alnum:]{0,}[:space:]{0,}[:alnum:]{0,}his statement
                String filter = String.format("message rlike '.*\"%s\": [\"]{0,1}[:alnum:]{0,}[:space:]{0,}[:alnum:]{0,}%s'", key, params.get(key));
                sqlFiltersToAdd.add(filter);
            }

            if (!sqlQuery.endsWith(" where ")) {
                sqlQuery = sqlQuery + " and ";
            }
            for (int i = 0; i < sqlFiltersToAdd.size(); i++) {
                sqlQuery = sqlQuery + sqlFiltersToAdd.get(i);

                if (i < sqlFiltersToAdd.size() - 1) {
                    sqlQuery = sqlQuery + " and ";
                }

            }

        } else {

        }

        sqlQuery = sqlQuery + ";";
        logger.info("SQL Query --> {}",sqlQuery);

        result = getFilteredRecords(sqlQuery);
        return result;
    }


    public Collection<LogsEntity> getFilteredRecords(String sql) {

        Collection<LogsEntity> logs = new ArrayList<LogsEntity>();
        ObjectMapper mapper = new ObjectMapper();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        logs = jdbcTemplate.query(sql, new RowMapper<LogsEntity>() {
            @Override
            public LogsEntity mapRow(ResultSet resultSet, int i) throws SQLException {
                LogsEntity log = new LogsEntity();

                log.setLogid((long) resultSet.getInt(1));
                log.setApplicationId(resultSet.getBigDecimal(2));
                log.setApplicationName(resultSet.getString(3));
                Map<String, Object> jsonMap = null;
                try {
                    jsonMap = mapper.readValue(resultSet.getString(4), Map.class);
                    logger.info("Filtered messags(json) column : {}",jsonMap);
                }catch (IOException e){
                    e.printStackTrace();
                }

                log.setMessage(jsonMap);
                return log;
            }
        });

        logger.info("Total Filtered mrecords : {}",logs.size());
        return logs;
    }



    public boolean checkPairsForExactValue(Map<String, Object> logModelMessage, Map<String, Object> paramsMap) {
        String[] jsonKeys = paramsMap.keySet().toArray(new String[0]);
        for (String key : jsonKeys) {
            if (!(paramsMap.get(key).toString().equals(logModelMessage.get(key).toString()))) {
                return false;
            }
        }
        return true;
    }

    public boolean checkPairsForContainsValue(Map<String, Object> logModelMessage, Map<String, Object> paramsMap) {
        String[] jsonKeys = paramsMap.keySet().toArray(new String[0]);
        for (String key : jsonKeys) {
            if (!(logModelMessage.get(key).toString().contains(paramsMap.get(key).toString()))) {
                return false;
            }

        }
        return true;
    }

}
