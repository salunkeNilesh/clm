package com.org.models;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Map;

public class LogMessageRequestVO {

    private BigDecimal applicationId;

    private String applicationName;

    private Map<String,Object> message;


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
