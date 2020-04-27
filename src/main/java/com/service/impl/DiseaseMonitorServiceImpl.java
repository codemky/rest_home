package com.service.impl;

import com.entity.DiseaseMonitor;
import com.dao.DiseaseMonitorDao;
import com.service.DiseaseMonitorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (DiseaseMonitor)表服务实现类
 *
 * @author makejava
 * @since 2020-04-19 19:33:27
 */
@Service("diseaseMonitorService")
public class DiseaseMonitorServiceImpl implements DiseaseMonitorService {
    @Resource
    private DiseaseMonitorDao diseaseMonitorDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public DiseaseMonitor queryById(Integer id) {
        return this.diseaseMonitorDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<DiseaseMonitor> queryAllByLimit(int offset, int limit) {
        return this.diseaseMonitorDao.queryAllByLimit(offset, limit);
    }
    
    /**
     * 根据实例对象查询数据
     * @param diseaseMonitor 实例对象
     * @return 对象列表
     */
    @Override
    public List<DiseaseMonitor> queryAllByDiseaseMonitor(DiseaseMonitor diseaseMonitor){
        return this.diseaseMonitorDao.queryAll(diseaseMonitor); 
    }
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    @Override
    public List<DiseaseMonitor> queryAll() {
        return this.diseaseMonitorDao.queryAll(null);
    }

    /**
     * 新增数据
     *
     * @param diseaseMonitor 实例对象
     * @return 实例对象
     */
    @Override
    public DiseaseMonitor insert(DiseaseMonitor diseaseMonitor) {
        this.diseaseMonitorDao.insert(diseaseMonitor);
        return diseaseMonitor;
    }

    /**
     * 修改数据
     *
     * @param diseaseMonitor 实例对象
     * @return 更新记录数
     */
    @Override
    public int update(DiseaseMonitor diseaseMonitor) {
        return this.diseaseMonitorDao.update(diseaseMonitor);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    @Override
    public int deleteById(Integer id) {
        return this.diseaseMonitorDao.deleteById(id);
    }
}