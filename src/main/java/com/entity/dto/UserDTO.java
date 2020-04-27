package com.entity.dto;

import lombok.Data;

/**
 * ClassName UserDTO
 * Description TODO
 *
 * @author mokuanyuan
 * @version 1.0
 * @date 2020/4/20 14:02
 **/
@Data
public class UserDTO {
    private String username; //账号

    private String password; //密码（带盐密文）

    private String name; //姓名

    private Integer age; //年龄

    private String sexy; //性别

    private String idcard; //身份证号码

    private String phone; //手机号码

    private String addr; //地址

    private Integer role; //角色分类
}
