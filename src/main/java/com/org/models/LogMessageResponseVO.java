package com.org.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class LogMessageResponseVO {


    private Long logid;

    private BigDecimal applicationId;

    private String applicationName;

    private Map<String,Object> message;

}
