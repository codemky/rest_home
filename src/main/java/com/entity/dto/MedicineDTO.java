package com.entity.dto;

import lombok.Data;

/**
 * ClassName MedicineDTO
 * Description TODO
 *
 * @author mokuanyuan
 * @version 1.0
 * @date 2020/4/20 20:27
 **/
@Data
public class MedicineDTO {

    private String name; //药品名称+规格

    private String unit; //单位（片、盒、罐）

}
