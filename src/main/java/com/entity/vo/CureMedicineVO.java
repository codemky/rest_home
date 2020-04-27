package com.entity.vo;

import lombok.Data;

/**
 * ClassName CureMedicineVO
 * Description TODO
 *
 * @author mokuanyuan
 * @version 1.0
 * @date 2020/4/21 8:37
 **/
@Data
public class CureMedicineVO {

    private Integer id; //自增主键

    private String name; //药品名称+规格

    private String unit; //单位（片、盒、罐）

    private Integer number; //数量

}
