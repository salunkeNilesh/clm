package com.org.models;

import java.math.BigDecimal;
import java.util.Map;

public class LogMessageResponseVO {


    private Long logid;

    private BigDecimal applicationId;

    private String applicationName;

    private Map<String,Object> message;

    public Long getLogid() {
        return logid;
    }

    public void setLogid(Long logid) {
        this.logid=logid;
    }
    public BigDecimal getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(BigDecimal applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Map<String,Object> getMessage() {
        return message;
    }

    public void setMessage(Map<String,Object> message) {
        this.message = message;
    }

}
