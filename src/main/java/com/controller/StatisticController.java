package com.controller;

import cn.hutool.core.date.DateUtil;
import com.common.Result;
import com.dao.DiseaseCaseExtDao;
import com.entity.criteria.MonitorCriteria;
import com.entity.criteria.StatictisCriteria;
import com.entity.vo.CaseStatictisVO;
import com.entity.vo.CureMonitorVO;
import com.exception.CodeMsg;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.service.DiseaseCaseExtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * ClassName StatisticController
 * Description TODO
 *
 * @author mokuanyuan
 * @version 1.0
 * @date 2020/4/25 8:22
 **/
@CrossOrigin(origins = "*")
@Api(description = "DiseaseCaseController模块")
@RestController
@RequestMapping("statistic")
public class StatisticController {

    @Resource
    private DiseaseCaseExtService diseaseCaseExtService;
    @Resource
    private DiseaseCaseExtDao diseaseCaseExtDao;


    @ApiOperation("根据条件统计病种的病例个数")
    @PostMapping("statisticBycriteria")
    public Result statisticBycriteria(@RequestBody StatictisCriteria criteria) {

        criteria.setDefaultValue();

        List<CaseStatictisVO> caseStatictisVOS = diseaseCaseExtDao.statisticsType(criteria);

        return Result.success(caseStatictisVOS);
    }




















}
