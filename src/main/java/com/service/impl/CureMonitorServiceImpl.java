package com.service.impl;

import com.entity.CureMonitor;
import com.dao.CureMonitorDao;
import com.service.CureMonitorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (CureMonitor)表服务实现类
 *
 * @author makejava
 * @since 2020-04-23 17:36:38
 */
@Service("cureMonitorService")
public class CureMonitorServiceImpl implements CureMonitorService {
    @Resource
    private CureMonitorDao cureMonitorDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public CureMonitor queryById(Integer id) {
        return this.cureMonitorDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<CureMonitor> queryAllByLimit(int offset, int limit) {
        return this.cureMonitorDao.queryAllByLimit(offset, limit);
    }
    
    /**
     * 根据实例对象查询数据
     * @param cureMonitor 实例对象
     * @return 对象列表
     */
    @Override
    public List<CureMonitor> queryAllByCureMonitor(CureMonitor cureMonitor){
        return this.cureMonitorDao.queryAll(cureMonitor); 
    }
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    @Override
    public List<CureMonitor> queryAll() {
        return this.cureMonitorDao.queryAll(null);
    }

    /**
     * 新增数据
     *
     * @param cureMonitor 实例对象
     * @return 实例对象
     */
    @Override
    public CureMonitor insert(CureMonitor cureMonitor) {
        this.cureMonitorDao.insert(cureMonitor);
        return cureMonitor;
    }

    /**
     * 修改数据
     *
     * @param cureMonitor 实例对象
     * @return 更新记录数
     */
    @Override
    public int update(CureMonitor cureMonitor) {
        return this.cureMonitorDao.update(cureMonitor);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    @Override
    public int deleteById(Integer id) {
        return this.cureMonitorDao.deleteById(id);
    }
}