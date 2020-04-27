package com.service;

import com.entity.criteria.CaseCriteria;
import com.entity.vo.DiseaseCaseVO;
import com.entity.vo.UserCaseTypeVO;

import java.util.List;

/**
 * (DiseaseCase)表服务接口
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface DiseaseCaseExtService {
    
    public List<UserCaseTypeVO> selectUserDiseaseByUserId(Integer uid);

    public List<DiseaseCaseVO> selectByCriteria(CaseCriteria caseCriteria);

}