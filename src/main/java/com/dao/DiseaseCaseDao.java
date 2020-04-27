package com.dao;

import com.entity.DiseaseCase;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (DiseaseCase)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
public interface DiseaseCaseDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DiseaseCase queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<DiseaseCase> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param diseaseCase 实例对象
     * @return 对象列表
     */
    List<DiseaseCase> queryAll(DiseaseCase diseaseCase);

    /**
     * 新增数据
     *
     * @param diseaseCase 实例对象
     * @return 影响行数
     */
    int insert(DiseaseCase diseaseCase);

    /**
     * 修改数据
     *
     * @param diseaseCase 实例对象
     * @return 影响行数
     */
    int update(DiseaseCase diseaseCase);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}