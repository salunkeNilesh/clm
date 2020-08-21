package com.org.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@ToString
public class LogMessageResponseVO {


    private Long logid;

    private BigDecimal applicationId;

    private String applicationName;

    private Map<String,Object> message;

}
