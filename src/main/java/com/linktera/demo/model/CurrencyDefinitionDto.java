package com.linktera.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDefinitionDto implements Serializable {

    private static final long serialVersionUID = -5924028033325088462L;


    private String isoCountryCode;
    private String currencyCode;
    private String currencyName;
    private String currencyNameTr;
    private String currencyCountry;
    private String currencySubName;
    private Long currencyDecimal;
    private String currencyBasis;
    private String currencyDateFormat;
    private String currencyComplianceFlag;
    private String currencyStatus;
    private String currencySource;
    private String updatedBy;
    private Date updateTime;
    private Date localUpdateTime;
    private String approvedBy;
    private Date approvedTime;
    private List<String> roleList = new ArrayList<>();
    private String entryUser;
}
