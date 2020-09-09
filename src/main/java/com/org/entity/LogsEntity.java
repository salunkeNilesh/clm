package com.org.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
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

}
