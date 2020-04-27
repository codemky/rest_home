package com.dao;

import com.entity.DiseaseType;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (DiseaseType)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
public interface DiseaseTypeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DiseaseType queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<DiseaseType> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param diseaseType 实例对象
     * @return 对象列表
     */
    List<DiseaseType> queryAll(DiseaseType diseaseType);

    /**
     * 新增数据
     *
     * @param diseaseType 实例对象
     * @return 影响行数
     */
    int insert(DiseaseType diseaseType);

    /**
     * 修改数据
     *
     * @param diseaseType 实例对象
     * @return 影响行数
     */
    int update(DiseaseType diseaseType);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}