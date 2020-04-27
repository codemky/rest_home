package com.entity;

import java.util.Date;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;

/**
 * (DiseaseNews)实体类
 *
 * @author codemky
 * @since 2020-04-20 16:29:54
 */
@Data
public class DiseaseNews implements Serializable {
    private static final long serialVersionUID = 494716884742185575L;
     
    private Integer id; //自增主键    
     
    private String title; //文章标题    
     
    private String type; //文章类型    
     
    private String content; //文章内容    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
    private Date createtime; //记录创建时间    
    
    private Integer page; //第几页

    private Integer pageSize; //页面大小
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    
    //如果页面参数为空，则设置默认值，默认第一页，默认每页10个记录
    public void setDefaultPage(){
        if( null == this.page) this.page = 1;
        if( null == this.pageSize) this.pageSize = 10;
    }
}