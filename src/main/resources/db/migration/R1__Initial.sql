create table if not exists logs(applicationId numeric, applicationName varcharacter(30), message json);
describe logs;
select * from `clm_db`.`flyway_schema_history`;