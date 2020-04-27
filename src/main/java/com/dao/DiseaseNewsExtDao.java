package com.dao;

import com.entity.DiseaseNews;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (DiseaseNews)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface DiseaseNewsExtDao {

    public List<String > selectAllType();

}