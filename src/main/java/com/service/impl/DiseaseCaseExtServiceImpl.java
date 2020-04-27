package com.service.impl;

import com.entity.DiseaseCase;
import com.dao.DiseaseCaseExtDao;
import com.entity.criteria.CaseCriteria;
import com.entity.vo.DiseaseCaseVO;
import com.entity.vo.UserCaseTypeVO;
import com.service.DiseaseCaseExtService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (DiseaseCase)表服务实现类
 *
 * @author makejava
 * @since 2020-04-20 11:50:01
 */
@Service("diseaseCaseExtService")
public class DiseaseCaseExtServiceImpl implements DiseaseCaseExtService {
    @Resource
    private DiseaseCaseExtDao diseaseCaseExtDao;

    @Override
    public List<UserCaseTypeVO> selectUserDiseaseByUserId(Integer uid) {
        return diseaseCaseExtDao.selectUserDiseaseByUserId(uid);
    }

    @Override
    public List<DiseaseCaseVO> selectByCriteria(CaseCriteria caseCriteria) {
        return diseaseCaseExtDao.selectByCriteria(caseCriteria);
    }
}