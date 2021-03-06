package com.service;

import com.entity.Files;
import java.util.List;

/**
 * (Files)表服务接口
 *
 * @author makejava
 * @since 2020-04-19 19:30:57
 */
public interface FilesService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Files queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Files> queryAllByLimit(int offset, int limit);
    
    /**
     * 根据实例对象查询数据
     * @param files 实例对象
     * @return 对象列表
     */
    List<Files> queryAllByFiles(Files files);
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    public List<Files> queryAll();

    /**
     * 新增数据
     *
     * @param files 实例对象
     * @return 实例对象
     */
    Files insert(Files files);

    /**
     * 修改数据
     *
     * @param files 实例对象
     * @return 更新记录数
     */
    int update(Files files);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    int deleteById(Integer id);

}