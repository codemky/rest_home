package com.controller;

import com.entity.CureMedicine;
import com.service.CureMedicineService;
import org.springframework.web.bind.annotation.*;
import com.common.Result;
import javax.annotation.Resource;
import com.exception.CodeMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * (CureMedicine)表控制层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
@CrossOrigin(origins = "*")
@Api(description = "CureMedicineController模块")
@RestController
@RequestMapping("cureMedicine")
public class CureMedicineController {
    /**
     * 服务对象
     */
    @Resource
    private CureMedicineService cureMedicineService;

    @ApiOperation("获取所有信息")
    @PostMapping("selectAll")
    public Result selectAll() {
        return Result.success(this.cureMedicineService.queryAll());
    }

	@ApiOperation("根据id获取详情")
    @PostMapping("selectById")
    public Result selectById(@RequestParam Integer id) {
        return Result.success(this.cureMedicineService.queryById(id));
    }

	@ApiOperation("添加记录")
    @PostMapping("add")
    public Result add(@RequestBody CureMedicine cureMedicine){
        return null != cureMedicineService.insert(cureMedicine).getId() ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }

	@ApiOperation("更新记录")
    @PostMapping("update")
    public Result update(@RequestBody CureMedicine cureMedicine){
        return cureMedicineService.update(cureMedicine) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }

	@ApiOperation("根据id删除记录")
    @PostMapping("deleteById")
    public Result deleteById(@RequestParam Integer id){
        return cureMedicineService.deleteById(id) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }
}