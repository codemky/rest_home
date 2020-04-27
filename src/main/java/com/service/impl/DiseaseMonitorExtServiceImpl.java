package com.service.impl;

import com.entity.DiseaseMonitor;
import com.dao.DiseaseMonitorExtDao;
import com.service.DiseaseMonitorExtService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (DiseaseMonitor)表服务实现类
 *
 * @author makejava
 * @since 2020-04-20 11:50:01
 */
@Service("diseaseMonitorExtService")
public class DiseaseMonitorExtServiceImpl implements DiseaseMonitorExtService {
    @Resource
    private DiseaseMonitorExtDao diseaseMonitorExtDao;

    
}