package com.entity.vo;

import com.entity.Files;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * ClassName DiseaseCaseVO
 * Description TODO
 *
 * @author mokuanyuan
 * @version 1.0
 * @date 2020/4/23 11:37
 **/
@Data
public class DiseaseCaseVO {

    private Integer id; //自增主键

    private String num; //病例编号

    private Float sugar; //血糖值

    private Integer highpressure; //收缩压

    private Integer lowpressure; //舒张压

    private String symptom; //症状
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date attacktime; //发病时间

    private Integer patientid; //患者用户id

    private Integer applyid; //申报用户id

    private Integer checkid; //审批用户id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applytime; //申报时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checktime; //审批时间

    private String remark; //审批意见

    private Integer typeid; //病种id


    private Boolean result; //审批结果

    private Integer state; //记录状态（0:未审批，1:通过，2:不通过，3:已作废，4：已监测）

    private String applyName;

    private String checkName;

    private String patientName;

    private String idcard;

    private String typeName;

    private List<Files> fileList;

    private List<Integer> fileIds;

    private Integer page; //第几页

    private Integer pageSize; //页面大小


}
