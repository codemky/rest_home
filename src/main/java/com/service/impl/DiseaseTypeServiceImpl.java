package com.service.impl;

import com.entity.DiseaseType;
import com.dao.DiseaseTypeDao;
import com.service.DiseaseTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (DiseaseType)表服务实现类
 *
 * @author makejava
 * @since 2020-04-19 19:33:27
 */
@Service("diseaseTypeService")
public class DiseaseTypeServiceImpl implements DiseaseTypeService {
    @Resource
    private DiseaseTypeDao diseaseTypeDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public DiseaseType queryById(Integer id) {
        return this.diseaseTypeDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<DiseaseType> queryAllByLimit(int offset, int limit) {
        return this.diseaseTypeDao.queryAllByLimit(offset, limit);
    }
    
    /**
     * 根据实例对象查询数据
     * @param diseaseType 实例对象
     * @return 对象列表
     */
    @Override
    public List<DiseaseType> queryAllByDiseaseType(DiseaseType diseaseType){
        return this.diseaseTypeDao.queryAll(diseaseType); 
    }
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    @Override
    public List<DiseaseType> queryAll() {
        return this.diseaseTypeDao.queryAll(null);
    }

    /**
     * 新增数据
     *
     * @param diseaseType 实例对象
     * @return 实例对象
     */
    @Override
    public DiseaseType insert(DiseaseType diseaseType) {
        this.diseaseTypeDao.insert(diseaseType);
        return diseaseType;
    }

    /**
     * 修改数据
     *
     * @param diseaseType 实例对象
     * @return 更新记录数
     */
    @Override
    public int update(DiseaseType diseaseType) {
        return this.diseaseTypeDao.update(diseaseType);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    @Override
    public int deleteById(Integer id) {
        return this.diseaseTypeDao.deleteById(id);
    }
}