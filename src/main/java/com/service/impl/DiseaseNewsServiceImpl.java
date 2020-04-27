package com.service.impl;

import com.entity.DiseaseNews;
import com.dao.DiseaseNewsDao;
import com.service.DiseaseNewsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (DiseaseNews)表服务实现类
 *
 * @author makejava
 * @since 2020-04-19 19:33:27
 */
@Service("diseaseNewsService")
public class DiseaseNewsServiceImpl implements DiseaseNewsService {
    @Resource
    private DiseaseNewsDao diseaseNewsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public DiseaseNews queryById(Integer id) {
        return this.diseaseNewsDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<DiseaseNews> queryAllByLimit(int offset, int limit) {
        return this.diseaseNewsDao.queryAllByLimit(offset, limit);
    }
    
    /**
     * 根据实例对象查询数据
     * @param diseaseNews 实例对象
     * @return 对象列表
     */
    @Override
    public List<DiseaseNews> queryAllByDiseaseNews(DiseaseNews diseaseNews){
        return this.diseaseNewsDao.queryAll(diseaseNews); 
    }
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    @Override
    public List<DiseaseNews> queryAll() {
        return this.diseaseNewsDao.queryAll(null);
    }

    /**
     * 新增数据
     *
     * @param diseaseNews 实例对象
     * @return 实例对象
     */
    @Override
    public DiseaseNews insert(DiseaseNews diseaseNews) {
        this.diseaseNewsDao.insert(diseaseNews);
        return diseaseNews;
    }

    /**
     * 修改数据
     *
     * @param diseaseNews 实例对象
     * @return 更新记录数
     */
    @Override
    public int update(DiseaseNews diseaseNews) {
        return this.diseaseNewsDao.update(diseaseNews);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    @Override
    public int deleteById(Integer id) {
        return this.diseaseNewsDao.deleteById(id);
    }
}