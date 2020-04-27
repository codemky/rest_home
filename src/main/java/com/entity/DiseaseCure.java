package com.entity;

import java.util.Date;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;

/**
 * (DiseaseCure)实体类
 *
 * @author codemky
 * @since 2020-04-22 10:56:59
 */
@Data
public class DiseaseCure implements Serializable {
    private static final long serialVersionUID = -12543744994690231L;
     
    private Integer id; //自增主键    
     
    private String num; //治疗记录编号    
     
    private String content; //治疗方案    
     
    private String result; //治疗结果    
     
    private String remark; //结果评价    
     
    private Integer caseid; //病例id    
     
    private Integer patientid; //患者id    
     
    private Integer doctorid; //医生id    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date createtime; //记录创建时间    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date updatetime; //记录更新时间    
     
    private Boolean state; //记录状态（未报告，已报告）    
    
    private Integer page; //第几页

    private Integer pageSize; //页面大小
    
    //如果页面参数为空，则设置默认值，默认第一页，默认每页10个记录
    public void setDefaultPage(){
        if( null == this.page) this.page = 1;
        if( null == this.pageSize) this.pageSize = 10;
    }
}