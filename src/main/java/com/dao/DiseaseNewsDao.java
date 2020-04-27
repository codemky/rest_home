package com.dao;

import com.entity.DiseaseNews;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (DiseaseNews)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
public interface DiseaseNewsDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DiseaseNews queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<DiseaseNews> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param diseaseNews 实例对象
     * @return 对象列表
     */
    List<DiseaseNews> queryAll(DiseaseNews diseaseNews);

    /**
     * 新增数据
     *
     * @param diseaseNews 实例对象
     * @return 影响行数
     */
    int insert(DiseaseNews diseaseNews);

    /**
     * 修改数据
     *
     * @param diseaseNews 实例对象
     * @return 影响行数
     */
    int update(DiseaseNews diseaseNews);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}