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
public class CaseCriteria {

    private Integer id; //DiseaseCure记录id

    private String num; //监测记录编号

    private Integer patientid; //患者用户id

    private Integer applyid; //申报用户id

    private Integer checkid; //审批用户id

    private String patientname; //患者姓名

    private String idcard; //患者身份证

    private String applyName;

    private String checkName;

    private String patientName;

    private String typename;  //病种名称

    private Integer state;  //记录状态（0:未审批，1:通过，2:不通过，3:已作废，4：已监测）

    //发病时间段
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginattack;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endattack;

    //记录创建时间段
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginapply;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endapply;

    //审批时间段
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date begincheck;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endcheck;

    private String ordername;

    private String ordertype;

    private Integer page;

    private Integer pageSize;

    //如果页面参数为空，则设置默认值，默认第一页，默认每页10个记录
    public void setDefaultValue(){
        if( null == this.page) this.page = 1;
        if( null == this.pageSize) this.pageSize = 10;
        if( null == this.ordername) this.ordername = "applytime";
        if( null == this.ordertype) this.ordertype = "desc";
    }

}
