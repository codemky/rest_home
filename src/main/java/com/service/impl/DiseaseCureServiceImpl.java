package com.service.impl;

import com.entity.DiseaseCure;
import com.dao.DiseaseCureDao;
import com.service.DiseaseCureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (DiseaseCure)表服务实现类
 *
 * @author makejava
 * @since 2020-04-19 19:33:27
 */
@Service("diseaseCureService")
public class DiseaseCureServiceImpl implements DiseaseCureService {
    @Resource
    private DiseaseCureDao diseaseCureDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public DiseaseCure queryById(Integer id) {
        return this.diseaseCureDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<DiseaseCure> queryAllByLimit(int offset, int limit) {
        return this.diseaseCureDao.queryAllByLimit(offset, limit);
    }
    
    /**
     * 根据实例对象查询数据
     * @param diseaseCure 实例对象
     * @return 对象列表
     */
    @Override
    public List<DiseaseCure> queryAllByDiseaseCure(DiseaseCure diseaseCure){
        return this.diseaseCureDao.queryAll(diseaseCure); 
    }
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    @Override
    public List<DiseaseCure> queryAll() {
        return this.diseaseCureDao.queryAll(null);
    }

    /**
     * 新增数据
     *
     * @param diseaseCure 实例对象
     * @return 实例对象
     */
    @Override
    public DiseaseCure insert(DiseaseCure diseaseCure) {
        this.diseaseCureDao.insert(diseaseCure);
        return diseaseCure;
    }

    /**
     * 修改数据
     *
     * @param diseaseCure 实例对象
     * @return 更新记录数
     */
    @Override
    public int update(DiseaseCure diseaseCure) {
        return this.diseaseCureDao.update(diseaseCure);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    @Override
    public int deleteById(Integer id) {
        return this.diseaseCureDao.deleteById(id);
    }
}