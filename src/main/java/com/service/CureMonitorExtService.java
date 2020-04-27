package com.service;

import com.entity.CureMonitor;
import com.entity.criteria.CureCriteria;
import com.entity.criteria.MonitorCriteria;
import com.entity.vo.CureMonitorVO;
import com.entity.vo.DiseaseCureVO;

import java.util.List;

/**
 * (CureMonitor)表服务接口
 *
 * @author makejava
 * @since 2020-04-23 17:36:38
 */
public interface CureMonitorExtService {

    public List<CureMonitorVO> selectByCriteria(MonitorCriteria criteria);

}