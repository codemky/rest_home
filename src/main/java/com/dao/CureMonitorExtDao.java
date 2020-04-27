package com.dao;

import com.entity.CureMonitor;
import com.entity.criteria.CureCriteria;
import com.entity.criteria.MonitorCriteria;
import com.entity.vo.CureMonitorVO;
import com.entity.vo.DiseaseCureVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (CureMonitor)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-23 17:36:38
 */
public interface CureMonitorExtDao {

    public List<CureMonitorVO> selectByCriteria(@Param("MonitorCriteria") MonitorCriteria monitorCriteria);

}