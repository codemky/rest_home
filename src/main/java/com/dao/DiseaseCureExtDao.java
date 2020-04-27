package com.dao;

import com.entity.criteria.CureCriteria;
import com.entity.vo.DiseaseCureVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (DiseaseCure)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface DiseaseCureExtDao {

    public List<DiseaseCureVO> selectByCriteria(@Param("CureCriteria")CureCriteria cureCriteria);

    public DiseaseCureVO selectById(Integer id);
}