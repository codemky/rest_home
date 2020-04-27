package com.service.impl;

import com.entity.CureMonitor;
import com.dao.CureMonitorExtDao;
import com.entity.criteria.CureCriteria;
import com.entity.criteria.MonitorCriteria;
import com.entity.vo.CureMonitorVO;
import com.entity.vo.DiseaseCureVO;
import com.service.CureMonitorExtService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (CureMonitor)表服务实现类
 *
 * @author makejava
 * @since 2020-04-23 17:36:38
 */
@Service("cureMonitorExtService")
public class CureMonitorExtServiceImpl implements CureMonitorExtService {
    @Resource
    private CureMonitorExtDao cureMonitorExtDao;

    @Override
    public List<CureMonitorVO> selectByCriteria(MonitorCriteria criteria) {
        return cureMonitorExtDao.selectByCriteria(criteria);
    }
}