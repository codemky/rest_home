package com.entity;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;

/**
 * (CureMedicine)实体类
 *
 * @author codemky
 * @since 2020-04-21 15:34:03
 */
@Data
public class CureMedicine implements Serializable {
    private static final long serialVersionUID = -13695723217997301L;
     
    private Integer id; //自增主键    
     
    private Integer cureid; //治疗记录id    
     
    private Integer medicineid; //药品id    
     
    private Integer number; //数量    
    
    private Integer page; //第几页

    private Integer pageSize; //页面大小
    
    //如果页面参数为空，则设置默认值，默认第一页，默认每页10个记录
    public void setDefaultPage(){
        if( null == this.page) this.page = 1;
        if( null == this.pageSize) this.pageSize = 10;
    }
}