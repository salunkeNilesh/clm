package com.org.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name="logs")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class LogsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logid;

    private BigDecimal applicationId;

    private String applicationName;

    @Type(type = "json")
    @Column( columnDefinition = "json" )
    private Map<String,Object> message;


    public Long getLogid() {
        return logid;
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

    public void setLogid(Long logId) {
        this.logid=logId;
    }
}
