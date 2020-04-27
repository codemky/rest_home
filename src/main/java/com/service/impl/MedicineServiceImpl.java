package com.service.impl;

import com.entity.Medicine;
import com.dao.MedicineDao;
import com.service.MedicineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Medicine)表服务实现类
 *
 * @author makejava
 * @since 2020-04-19 19:33:27
 */
@Service("medicineService")
public class MedicineServiceImpl implements MedicineService {
    @Resource
    private MedicineDao medicineDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Medicine queryById(Integer id) {
        return this.medicineDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Medicine> queryAllByLimit(int offset, int limit) {
        return this.medicineDao.queryAllByLimit(offset, limit);
    }
    
    /**
     * 根据实例对象查询数据
     * @param medicine 实例对象
     * @return 对象列表
     */
    @Override
    public List<Medicine> queryAllByMedicine(Medicine medicine){
        return this.medicineDao.queryAll(medicine); 
    }
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    @Override
    public List<Medicine> queryAll() {
        return this.medicineDao.queryAll(null);
    }

    /**
     * 新增数据
     *
     * @param medicine 实例对象
     * @return 实例对象
     */
    @Override
    public Medicine insert(Medicine medicine) {
        this.medicineDao.insert(medicine);
        return medicine;
    }

    /**
     * 修改数据
     *
     * @param medicine 实例对象
     * @return 更新记录数
     */
    @Override
    public int update(Medicine medicine) {
        return this.medicineDao.update(medicine);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    @Override
    public int deleteById(Integer id) {
        return this.medicineDao.deleteById(id);
    }
}