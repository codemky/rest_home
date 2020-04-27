package com.dao;

import com.entity.DiseaseType;
import com.entity.Medicine;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (DiseaseType)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface DiseaseTypeExtDao {

    /**
     * 批量新增对象
     */
    int insertList(@Param("diseaseTypes") List<DiseaseType> diseaseTypes);

}