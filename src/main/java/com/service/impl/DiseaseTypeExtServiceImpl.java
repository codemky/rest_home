package com.service.impl;

import com.entity.DiseaseType;
import com.dao.DiseaseTypeExtDao;
import com.entity.Medicine;
import com.service.DiseaseTypeExtService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (DiseaseType)表服务实现类
 *
 * @author makejava
 * @since 2020-04-20 11:50:01
 */
@Service("diseaseTypeExtService")
public class DiseaseTypeExtServiceImpl implements DiseaseTypeExtService {
    @Resource
    private DiseaseTypeExtDao diseaseTypeExtDao;

    @Override
    public int insertList(List<DiseaseType> diseaseTypes) {
        return diseaseTypeExtDao.insertList(diseaseTypes);
    }

}