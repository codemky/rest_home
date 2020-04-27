package com.controller;

import com.entity.DiseaseMonitor;
import com.service.DiseaseMonitorService;
import org.springframework.web.bind.annotation.*;
import com.common.Result;
import javax.annotation.Resource;
import com.exception.CodeMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * (DiseaseMonitor)表控制层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
@CrossOrigin(origins = "*")
@Api(description = "DiseaseMonitorController模块")
@RestController
@RequestMapping("diseaseMonitor")
public class DiseaseMonitorController {
    /**
     * 服务对象
     */
    @Resource
    private DiseaseMonitorService diseaseMonitorService;

    @ApiOperation("获取所有信息")
    @PostMapping("selectAll")
    public Result selectAll() {
        return Result.success(this.diseaseMonitorService.queryAll());
    }

	@ApiOperation("根据id获取详情")
    @PostMapping("selectById")
    public Result selectById(@RequestParam Integer id) {
        return Result.success(this.diseaseMonitorService.queryById(id));
    }

	@ApiOperation("添加记录")
    @PostMapping("add")
    public Result add(@RequestBody DiseaseMonitor diseaseMonitor){
        return null != diseaseMonitorService.insert(diseaseMonitor).getId() ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }

	@ApiOperation("更新记录")
    @PostMapping("update")
    public Result update(@RequestBody DiseaseMonitor diseaseMonitor){
        return diseaseMonitorService.update(diseaseMonitor) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }

	@ApiOperation("根据id删除记录")
    @PostMapping("deleteById")
    public Result deleteById(@RequestParam Integer id){
        return diseaseMonitorService.deleteById(id) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }
}