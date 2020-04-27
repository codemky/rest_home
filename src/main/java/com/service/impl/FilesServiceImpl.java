package com.service.impl;

import com.entity.Files;
import com.dao.FilesDao;
import com.service.FilesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Files)表服务实现类
 *
 * @author makejava
 * @since 2020-04-19 19:33:27
 */
@Service("filesService")
public class FilesServiceImpl implements FilesService {
    @Resource
    private FilesDao filesDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Files queryById(Integer id) {
        return this.filesDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Files> queryAllByLimit(int offset, int limit) {
        return this.filesDao.queryAllByLimit(offset, limit);
    }
    
    /**
     * 根据实例对象查询数据
     * @param files 实例对象
     * @return 对象列表
     */
    @Override
    public List<Files> queryAllByFiles(Files files){
        return this.filesDao.queryAll(files); 
    }
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    @Override
    public List<Files> queryAll() {
        return this.filesDao.queryAll(null);
    }

    /**
     * 新增数据
     *
     * @param files 实例对象
     * @return 实例对象
     */
    @Override
    public Files insert(Files files) {
        this.filesDao.insert(files);
        return files;
    }

    /**
     * 修改数据
     *
     * @param files 实例对象
     * @return 更新记录数
     */
    @Override
    public int update(Files files) {
        return this.filesDao.update(files);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    @Override
    public int deleteById(Integer id) {
        return this.filesDao.deleteById(id);
    }
}