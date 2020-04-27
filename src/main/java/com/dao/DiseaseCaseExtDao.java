package com.dao;

import com.entity.criteria.CaseCriteria;
import com.entity.criteria.StatictisCriteria;
import com.entity.vo.CaseStatictisVO;
import com.entity.vo.DiseaseCaseVO;
import com.entity.vo.UserCaseTypeVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (DiseaseCase)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface DiseaseCaseExtDao {

    public List<UserCaseTypeVO> selectUserDiseaseByUserId(Integer uid);

    public List<DiseaseCaseVO> selectByCriteria(@Param("CaseCriteria") CaseCriteria caseCriteria);

    public List<CaseStatictisVO> statisticsType(@Param("StatictisCriteria") StatictisCriteria criteria);
}