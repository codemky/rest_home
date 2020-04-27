package com.service;

import com.entity.Medicine;
import java.util.List;

/**
 * (Medicine)表服务接口
 *
 * @author makejava
 * @since 2020-04-19 19:30:57
 */
public interface MedicineService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Medicine queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Medicine> queryAllByLimit(int offset, int limit);
    
    /**
     * 根据实例对象查询数据
     * @param medicine 实例对象
     * @return 对象列表
     */
    List<Medicine> queryAllByMedicine(Medicine medicine);
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    public List<Medicine> queryAll();

    /**
     * 新增数据
     *
     * @param medicine 实例对象
     * @return 实例对象
     */
    Medicine insert(Medicine medicine);

    /**
     * 修改数据
     *
     * @param medicine 实例对象
     * @return 更新记录数
     */
    int update(Medicine medicine);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    int deleteById(Integer id);

}