package com.dao;

import com.entity.CureMedicine;
import com.entity.Medicine;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (CureMedicine)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface CureMedicineExtDao {


    /**
     * 批量新增对象
     */
    int insertList(@Param("cureMedicines") List<CureMedicine> cureMedicines);

    int deleteList(@Param("ids") List<Integer> ids);

}