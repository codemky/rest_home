package com.service.impl;

import com.entity.CureMedicine;
import com.dao.CureMedicineDao;
import com.service.CureMedicineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (CureMedicine)表服务实现类
 *
 * @author makejava
 * @since 2020-04-19 19:33:27
 */
@Service("cureMedicineService")
public class CureMedicineServiceImpl implements CureMedicineService {
    @Resource
    private CureMedicineDao cureMedicineDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public CureMedicine queryById(Integer id) {
        return this.cureMedicineDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<CureMedicine> queryAllByLimit(int offset, int limit) {
        return this.cureMedicineDao.queryAllByLimit(offset, limit);
    }
    
    /**
     * 根据实例对象查询数据
     * @param cureMedicine 实例对象
     * @return 对象列表
     */
    @Override
    public List<CureMedicine> queryAllByCureMedicine(CureMedicine cureMedicine){
        return this.cureMedicineDao.queryAll(cureMedicine); 
    }
    
    /**
     * 查询所有数据
     *
     * @return 对象列表
     */
    @Override
    public List<CureMedicine> queryAll() {
        return this.cureMedicineDao.queryAll(null);
    }

    /**
     * 新增数据
     *
     * @param cureMedicine 实例对象
     * @return 实例对象
     */
    @Override
    public CureMedicine insert(CureMedicine cureMedicine) {
        this.cureMedicineDao.insert(cureMedicine);
        return cureMedicine;
    }

    /**
     * 修改数据
     *
     * @param cureMedicine 实例对象
     * @return 更新记录数
     */
    @Override
    public int update(CureMedicine cureMedicine) {
        return this.cureMedicineDao.update(cureMedicine);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 更新记录数
     */
    @Override
    public int deleteById(Integer id) {
        return this.cureMedicineDao.deleteById(id);
    }
}