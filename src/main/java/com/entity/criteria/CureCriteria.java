package com.entity.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * ClassName CureCriteria
 * Description TODO
 *
 * @author mokuanyuan
 * @version 1.0
 * @date 2020/4/21 11:30
 **/
@Data
public class CureCriteria {

    private Integer id; //DiseaseCure记录id

    private String num; //治疗记录编号

    private Integer diseasename; //病种名称

    private String patientname; //患者姓名

    private String idcard; //患者身份证

    private String doctorname; //医生姓名

    private String typename;  //病种名称

    private Boolean state; //记录状态（ false未报告 true已报告）

    //记录创建时间段
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date begincreate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endcreate;

    //记录更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginupdate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endupdate;

    private String ordername;

    private String ordertype;

    private Integer page;
    private Integer pageSize;

    //如果页面参数为空，则设置默认值，默认第一页，默认每页10个记录
    public void setDefaultValue(){
        if( null == this.page) this.page = 1;
        if( null == this.pageSize) this.pageSize = 10;
        if( null == this.ordername) this.ordername = "createtime";
        if( null == this.ordertype) this.ordertype = "desc";
    }
}
