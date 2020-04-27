package com.entity.criteria;

import lombok.Data;

import java.util.Date;

/**
 * ClassName StatictisCriteria
 * Description TODO
 *
 * @author mokuanyuan
 * @version 1.0
 * @date 2020/4/25 8:24
 **/
@Data
public class StatictisCriteria {

    private String sexy;

    private Date beginattack;

    private Date endattack;

    private Integer beginage;

    private Integer endage;

    private Integer limit;

    public void setDefaultValue(){
        this.limit = 10;
    }


}
