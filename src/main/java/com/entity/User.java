package com.entity;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;

/**
 * (User)实体类
 *
 * @author codemky
 * @since 2020-04-20 16:29:54
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -97287898852962579L;
     
    private Integer id; //自增主键    
     
    private String username; //账号    
     
    private String password; //密码（带盐密文）    
     
    private String name; //姓名    
     
    private Integer age; //年龄    
     
    private String sexy; //性别    
     
    private String idcard; //身份证号码    
     
    private String phone; //手机号码    
     
    private String addr; //地址    
     
    private Integer role; //角色分类    
    
    private Integer page; //第几页

    private Integer pageSize; //页面大小

    private String newPwd;

    private String tempname;
    
    //如果页面参数为空，则设置默认值，默认第一页，默认每页10个记录
    public void setDefaultPage(){
        if( null == this.page) this.page = 1;
        if( null == this.pageSize) this.pageSize = 10;
    }
}