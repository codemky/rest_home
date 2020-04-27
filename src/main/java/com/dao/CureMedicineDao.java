package com.dao;

import com.entity.CureMedicine;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (CureMedicine)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
public interface CureMedicineDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    CureMedicine queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<CureMedicine> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param cureMedicine 实例对象
     * @return 对象列表
     */
    List<CureMedicine> queryAll(CureMedicine cureMedicine);

    /**
     * 新增数据
     *
     * @param cureMedicine 实例对象
     * @return 影响行数
     */
    int insert(CureMedicine cureMedicine);

    /**
     * 修改数据
     *
     * @param cureMedicine 实例对象
     * @return 影响行数
     */
    int update(CureMedicine cureMedicine);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}