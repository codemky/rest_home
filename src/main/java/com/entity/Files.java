package com.entity;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;

/**
 * (Files)实体类
 *
 * @author codemky
 * @since 2020-04-20 16:29:54
 */
@Data
public class Files implements Serializable {
    private static final long serialVersionUID = -16730903996450189L;
     
    private Integer id; //自增主键id    
     
    private String module; //判断属于哪个模块（病例、监测、治疗记录）    
     
    private Integer infoid; //模块相关的id    
     
    private String name; //文件原名称    
     
    private String path; //文件路径    
    
    private Integer page; //第几页

    private Integer pageSize; //页面大小
    
    //如果页面参数为空，则设置默认值，默认第一页，默认每页10个记录
    public void setDefaultPage(){
        if( null == this.page) this.page = 1;
        if( null == this.pageSize) this.pageSize = 10;
    }
}