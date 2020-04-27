package com.service;

import com.entity.Medicine;
import com.entity.User;

import java.util.List;

/**
 * (Medicine)表服务接口
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface MedicineExtService {

    public int insertList(List<Medicine> medicines);


}