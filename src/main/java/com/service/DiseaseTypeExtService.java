package com.service;

import com.entity.DiseaseType;
import java.util.List;

/**
 * (DiseaseType)表服务接口
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface DiseaseTypeExtService {

    public int insertList(List<DiseaseType> diseaseTypes);

}