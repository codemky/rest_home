package com.entity;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;

/**
 * (Medicine)实体类
 *
 * @author codemky
 * @since 2020-04-20 16:29:54
 */
@Data
public class Medicine implements Serializable {
    private static final long serialVersionUID = 799518881266981679L;
     
    private Integer id; //自增主键    
     
    private String name; //药品名称+规格    
     
    private String unit; //单位（片、盒、罐）    
    
    private Integer page; //第几页

    private Integer pageSize; //页面大小
    
    //如果页面参数为空，则设置默认值，默认第一页，默认每页10个记录
    public void setDefaultPage(){
        if( null == this.page) this.page = 1;
        if( null == this.pageSize) this.pageSize = 10;
    }
}