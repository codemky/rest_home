package com.service.impl;

import com.entity.DiseaseCase;
import com.dao.DiseaseCaseDao;
import com.service.DiseaseCaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (DiseaseCase)表服务实现类
 *
 * @author makejava
 * @since 2020-04-19 19:33:27
 */
@Service("diseaseCaseService")
public class DiseaseCaseServiceImpl implements DiseaseCaseService {
    @Resource
    private DiseaseCaseDao diseaseCaseDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public DiseaseCase queryById(Integer id) {
        return this.diseaseCaseDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<DiseaseCase> queryAllByLimit(int offset, int limit) {
        return this.diseaseCaseDao.queryAllByLimit(offset, limit);
    }
    
    /**
     * 根据实例对象查询数据
     * @param diseaseCase 实例对象
     * @return 对象列表
     */
    @Override
    public List<DiseaseCase> queryAllByDiseaseCase(DiseaseCase diseaseCase){
        return this.diseaseCaseDao.queryAll(diseaseCase); 
    }
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    @Override
    public List<DiseaseCase> queryAll() {
        return this.diseaseCaseDao.queryAll(null);
    }

    /**
     * 新增数据
     *
     * @param diseaseCase 实例对象
     * @return 实例对象
     */
    @Override
    public DiseaseCase insert(DiseaseCase diseaseCase) {
        this.diseaseCaseDao.insert(diseaseCase);
        return diseaseCase;
    }

    /**
     * 修改数据
     *
     * @param diseaseCase 实例对象
     * @return 更新记录数
     */
    @Override
    public int update(DiseaseCase diseaseCase) {
        return this.diseaseCaseDao.update(diseaseCase);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    @Override
    public int deleteById(Integer id) {
        return this.diseaseCaseDao.deleteById(id);
    }
}