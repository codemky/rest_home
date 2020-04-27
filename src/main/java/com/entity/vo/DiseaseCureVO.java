package com.entity.vo;

import com.entity.Files;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * ClassName CureMedicineVo
 * Description TODO
 *
 * @author mokuanyuan
 * @version 1.0
 * @date 2020/4/21 8:34
 **/
@Data
public class DiseaseCureVO {

    private Integer id; //自增主键

    private String num; //治疗记录编号

    private String content; //治疗方案

    private String result; //治疗结果

    private String remark; //结果评价

    private Integer caseid; //病例id

    private Integer patientid; //患者id

    private String patientname; //患者名称

    private String idcard; //患者身份证

    private Integer doctorid; //医生id

    private String doctorname; //患者id

    private String typename; //病种名称

    private Boolean state; //记录状态（ false未报告 true已报告）

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime; //记录创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatetime; //记录更新时间

    private List<CureMedicineVO> medicineVOList;  //用药信息

    private List<Integer> medicineList;

    private List<Integer> numberList;

    private List<Files> fileList;

    private List<Integer> fileIds;

    public int checkMedicines(){

        for(int i=0;i<medicineList.size();i++){
            if( null == medicineList.get(i)){
                return 1;
            }
        }
        for(int i=0;i<numberList.size();i++){
            if( null == numberList.get(i)){
                return 2;
            }
        }
        if(medicineList.size() != numberList.size()){
            return 3;
        }

        return 0;
    }

}
