package com.service.impl;

import com.entity.CureMedicine;
import com.dao.CureMedicineExtDao;
import com.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (CureMedicine)表服务实现类
 *
 * @author makejava
 * @since 2020-04-20 11:50:01
 */
@Service("cureMedicineExtService")
public class CureMedicineExtServiceImpl implements CureMedicineExtService {
    @Resource
    private CureMedicineExtDao cureMedicineExtDao;


    @Override
    public int insertList(List<CureMedicine> cureMedicines) {
        return cureMedicineExtDao.insertList(cureMedicines);
    }

    @Override
    public int deleteList(List<Integer> integerList) {
        return cureMedicineExtDao.deleteList(integerList);
    }
}