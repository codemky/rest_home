package com.entity;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;

/**
 * (DiseaseType)实体类
 *
 * @author codemky
 * @since 2020-04-20 16:29:54
 */
@Data
public class DiseaseType implements Serializable {
    private static final long serialVersionUID = -70154608441663383L;
     
    private Integer id; //自增主键id    
     
    private String name; //病种名称    
     
    private String symptom; //病种常见症状    
    
    private Integer page; //第几页

    private Integer pageSize; //页面大小
    
    //如果页面参数为空，则设置默认值，默认第一页，默认每页10个记录
    public void setDefaultPage(){
        if( null == this.page) this.page = 1;
        if( null == this.pageSize) this.pageSize = 10;
    }
}