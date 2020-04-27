package com.service;

import com.entity.CureMonitor;
import java.util.List;

/**
 * (CureMonitor)表服务接口
 *
 * @author makejava
 * @since 2020-04-23 17:36:38
 */
public interface CureMonitorService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    CureMonitor queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<CureMonitor> queryAllByLimit(int offset, int limit);
    
    /**
     * 根据实例对象查询数据
     * @param cureMonitor 实例对象
     * @return 对象列表
     */
    List<CureMonitor> queryAllByCureMonitor(CureMonitor cureMonitor);
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    public List<CureMonitor> queryAll();

    /**
     * 新增数据
     *
     * @param cureMonitor 实例对象
     * @return 实例对象
     */
    CureMonitor insert(CureMonitor cureMonitor);

    /**
     * 修改数据
     *
     * @param cureMonitor 实例对象
     * @return 更新记录数
     */
    int update(CureMonitor cureMonitor);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    int deleteById(Integer id);

}