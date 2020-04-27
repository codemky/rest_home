package com.service;

import com.entity.DiseaseMonitor;
import java.util.List;

/**
 * (DiseaseMonitor)表服务接口
 *
 * @author makejava
 * @since 2020-04-19 19:30:57
 */
public interface DiseaseMonitorService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DiseaseMonitor queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<DiseaseMonitor> queryAllByLimit(int offset, int limit);
    
    /**
     * 根据实例对象查询数据
     * @param diseaseMonitor 实例对象
     * @return 对象列表
     */
    List<DiseaseMonitor> queryAllByDiseaseMonitor(DiseaseMonitor diseaseMonitor);
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    public List<DiseaseMonitor> queryAll();

    /**
     * 新增数据
     *
     * @param diseaseMonitor 实例对象
     * @return 实例对象
     */
    DiseaseMonitor insert(DiseaseMonitor diseaseMonitor);

    /**
     * 修改数据
     *
     * @param diseaseMonitor 实例对象
     * @return 更新记录数
     */
    int update(DiseaseMonitor diseaseMonitor);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    int deleteById(Integer id);

}