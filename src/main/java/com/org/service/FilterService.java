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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Service
public class FilterService {

    @Autowired
    MessageService messageService;

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public static final Logger logger= LoggerFactory.getLogger(FilterService.class);

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void atriskdogs() {
        String sql = "select * from logs where application_name='demo' and message rlike '.*\"message\": [\"]{0,1}[:alnum:]{0,} this statement' and message rlike '.*\"timeMillis\": [\"]{0,1}[:alnum:]{0,}1551908' and message rlike '.*\"level\": [\"]{0,1}[:alnum:]{0,}ROR' and message rlike '.*\"pid\": [\"]{0,1}[:alnum:]{0,}6' and message rlike '.*\"logId\": [\"]{0,1}[:alnum:]{0,}45';";
        logger.info("SQL Query --> {}", sql);

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


        logger.info("Total filter records :{} ", logs.size());

    }


}