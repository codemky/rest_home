package com.dao;

import com.entity.Medicine;
import com.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (Medicine)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface MedicineExtDao {

    /**
     * 批量新增对象
     */
    int insertList(@Param("medicines") List<Medicine> medicines);


}