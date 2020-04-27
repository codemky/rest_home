package com.dao;

import com.entity.Medicine;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (Medicine)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
public interface MedicineDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Medicine queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Medicine> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param medicine 实例对象
     * @return 对象列表
     */
    List<Medicine> queryAll(Medicine medicine);

    /**
     * 新增数据
     *
     * @param medicine 实例对象
     * @return 影响行数
     */
    int insert(Medicine medicine);

    /**
     * 修改数据
     *
     * @param medicine 实例对象
     * @return 影响行数
     */
    int update(Medicine medicine);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}