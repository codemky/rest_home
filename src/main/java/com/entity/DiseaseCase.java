package com.entity;

import java.util.Date;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;

/**
 * (DiseaseCase)实体类
 *
 * @author codemky
 * @since 2020-04-22 11:01:04
 */
@Data
public class DiseaseCase implements Serializable {
    private static final long serialVersionUID = -27860236400416107L;
     
    private Integer id; //自增主键    
     
    private String num; //病例编号    
     
    private Float sugar; //血糖值    
     
    private Integer highpressure; //收缩压    
     
    private Integer lowpressure; //舒张压    
     
    private String symptom; //症状    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date attacktime; //发病时间    
     
    private Integer applyid; //申报用户id    
     
    private Integer checkid; //审批用户id    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date applytime; //申报时间    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date checktime; //审批时间    
     
    private String remark; //审批意见    
     
    private Boolean result; //审批结果    
     
    private Integer typeid; //病种id    
     
    private Integer patientid; //患者用户id    
     
    private Integer state; //记录状态（0:未审批，1:通过，2:不通过，3:已作废，4：已监测）

    private Integer page; //第几页

    private Integer pageSize; //页面大小
    
    //如果页面参数为空，则设置默认值，默认第一页，默认每页10个记录
    public void setDefaultPage(){
        if( null == this.page) this.page = 1;
        if( null == this.pageSize) this.pageSize = 10;
    }
}