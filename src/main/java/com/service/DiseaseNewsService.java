package com.service;

import com.entity.DiseaseNews;
import java.util.List;

/**
 * (DiseaseNews)表服务接口
 *
 * @author makejava
 * @since 2020-04-19 19:30:57
 */
public interface DiseaseNewsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DiseaseNews queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<DiseaseNews> queryAllByLimit(int offset, int limit);
    
    /**
     * 根据实例对象查询数据
     * @param diseaseNews 实例对象
     * @return 对象列表
     */
    List<DiseaseNews> queryAllByDiseaseNews(DiseaseNews diseaseNews);
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    public List<DiseaseNews> queryAll();

    /**
     * 新增数据
     *
     * @param diseaseNews 实例对象
     * @return 实例对象
     */
    DiseaseNews insert(DiseaseNews diseaseNews);

    /**
     * 修改数据
     *
     * @param diseaseNews 实例对象
     * @return 更新记录数
     */
    int update(DiseaseNews diseaseNews);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    int deleteById(Integer id);

}