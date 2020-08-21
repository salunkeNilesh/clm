create table if not exists logs
(logid bigint NOT NULL AUTO_INCREMENT,application_id numeric, application_name varcharacter(30), message json, primary key(logid));

describe logs;
select * from `clm_db`.`flyway_schema_history`;