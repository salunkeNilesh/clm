package com.org.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class LogMessageRequestVO {

    private BigDecimal applicationId;

    private String applicationName;

    private Map<String,Object> message;

}
