package com.service;

import com.entity.CureMedicine;

import java.util.List;

/**
 * (CureMedicine)表服务接口
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface CureMedicineExtService {

    int insertList(List<CureMedicine> cureMedicines);

    int deleteList(List<Integer> integerList);

}