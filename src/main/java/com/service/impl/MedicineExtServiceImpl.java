package com.service.impl;

import com.entity.Medicine;
import com.dao.MedicineExtDao;
import com.entity.User;
import com.service.MedicineExtService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Medicine)表服务实现类
 *
 * @author makejava
 * @since 2020-04-20 11:50:01
 */
@Service("medicineExtService")
public class MedicineExtServiceImpl implements MedicineExtService {
    @Resource
    private MedicineExtDao medicineExtDao;

    @Override
    public int insertList(List<Medicine> medicines) {
        return medicineExtDao.insertList(medicines);
    }
}