package com.entity;

import java.util.Date;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;

/**
 * (CureMonitor)实体类
 *
 * @author codemky
 * @since 2020-04-23 17:36:38
 */
@Data
public class CureMonitor implements Serializable {
    private static final long serialVersionUID = -14666347131920995L;
     
    private Integer id; //自增主键    
     
    private String num; //记录编号    
     
    private String curecontent; //治疗方案    
     
    private String monitorcontent; //其他监测内容    
     
    private Float sugar; //血糖值    
     
    private Integer highpressure; //收缩压    
     
    private Integer lowpressure; //舒张压    
     
    private String cureresult; //治疗结果    
     
    private String monitorresult; //监测结果    
     
    private String remark; //治疗监测评价    
     
    private Integer caseid; //病例id    
     
    private Integer patientid; //患者id    
     
    private Integer doctorid; //医生id    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date createtime; //记录创建时间    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date updatetime; //记录更新时间    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date remindtime; //提醒时间    
     
    private Boolean state; //记录状态（未报告，已报告）    
    
    private Integer page; //第几页

    private Integer pageSize; //页面大小
    
    //如果页面参数为空，则设置默认值，默认第一页，默认每页10个记录
    public void setDefaultPage(){
        if( null == this.page) this.page = 1;
        if( null == this.pageSize) this.pageSize = 10;
    }
}