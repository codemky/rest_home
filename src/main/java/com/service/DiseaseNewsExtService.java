package com.service;

import com.entity.DiseaseNews;
import java.util.List;

/**
 * (DiseaseNews)表服务接口
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface DiseaseNewsExtService {
    
    public List<String> selectAllType();
}