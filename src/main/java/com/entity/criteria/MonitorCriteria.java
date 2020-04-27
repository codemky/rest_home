package com.entity.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * ClassName MonitorCriteria
 * Description TODO
 *
 * @author mokuanyuan
 * @version 1.0
 * @date 2020/4/23 17:55
 **/
@Data
public class MonitorCriteria {

    private Integer id; //DiseaseCure记录id

    private String num; //监测记录编号

    private Integer diseasename; //病种名称

    private Integer patientid;

    private Integer doctorid;

    private String patientname; //患者姓名

    private String idcard; //患者身份证

    private String doctorname; //医生姓名

    private String typename;  //病种名称

    private Boolean state;  //记录状态（ false未报告 true已报告）

    //提醒时间段
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginremind;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endremind;

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
