package com.service.impl;

import com.entity.DiseaseNews;
import com.dao.DiseaseNewsExtDao;
import com.service.DiseaseNewsExtService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (DiseaseNews)表服务实现类
 *
 * @author makejava
 * @since 2020-04-20 11:50:01
 */
@Service("diseaseNewsExtService")
public class DiseaseNewsExtServiceImpl implements DiseaseNewsExtService {
    @Resource
    private DiseaseNewsExtDao diseaseNewsExtDao;

    @Override
    public List<String> selectAllType() {
        return diseaseNewsExtDao.selectAllType();
    }
}