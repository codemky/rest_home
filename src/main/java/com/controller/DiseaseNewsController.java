package com.controller;

import com.entity.DiseaseNews;
import com.entity.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.service.DiseaseNewsExtService;
import com.service.DiseaseNewsService;
import org.springframework.web.bind.annotation.*;
import com.common.Result;
import javax.annotation.Resource;
import com.exception.CodeMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (DiseaseNews)表控制层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
@CrossOrigin(origins = "*")
@Api(description = "DiseaseNewsController模块")
@RestController
@RequestMapping("diseaseNews")
public class DiseaseNewsController {

    @Resource
    private DiseaseNewsService diseaseNewsService;
    @Resource
    private DiseaseNewsExtService diseaseNewsExtService;

    @ApiOperation("获取现有文章的所有类型")
    @PostMapping("selectAllType")
    public Result selectAllType() {
        return Result.success(diseaseNewsExtService.selectAllType().stream().distinct().collect(Collectors.toList()));
    }

    @ApiOperation("根据id获取详情")
    @PostMapping("selectById")
    public Result selectById(@RequestParam Integer id) {
        return Result.success(this.diseaseNewsService.queryById(id));
    }

    @ApiOperation("根据条件搜索科普文章")
    @PostMapping("selectByNews")
    public Result selectByNews(@RequestBody DiseaseNews diseaseNews) {
        diseaseNews.setDefaultPage();
        PageHelper.startPage(diseaseNews.getPage(), diseaseNews.getPageSize());
        List<DiseaseNews> newsList = diseaseNewsService.queryAllByDiseaseNews(diseaseNews);
        PageInfo<DiseaseNews> newsInfo = new PageInfo<>(newsList);

        return Result.success(newsInfo);
    }

	@ApiOperation("添加记录")
    @PostMapping("add")
    public Result add(@RequestBody DiseaseNews diseaseNews){
        if( null == diseaseNews.getTitle()){
            return Result.error(new CodeMsg("文章标题为空"));
        }
        if( null == diseaseNews.getTitle()){
            return Result.error(new CodeMsg("文章类型为空"));
        }
        if( null == diseaseNews.getTitle()){
            return Result.error(new CodeMsg("文章内容为空"));
        }
        diseaseNews.setCreatetime(new Date());

        return null != diseaseNewsService.insert(diseaseNews) ?
                Result.success(diseaseNews) : Result.error(CodeMsg.ERROR);
    }

	@ApiOperation("更新记录")
    @PostMapping("update")
    public Result update(@RequestBody DiseaseNews diseaseNews){
	    if(Objects.isNull(diseaseNews.getId())){
	        return Result.error(CodeMsg.UPDATE_ID_NULL);
        }
        if( null == diseaseNews.getTitle()){
            return Result.error(new CodeMsg("文章标题为空"));
        }
        if( null == diseaseNews.getTitle()){
            return Result.error(new CodeMsg("文章类型为空"));
        }
        if( null == diseaseNews.getTitle()){
            return Result.error(new CodeMsg("文章内容为空"));
        }
        return diseaseNewsService.update(diseaseNews) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }

	@ApiOperation("根据id删除记录")
    @PostMapping("deleteById")
    public Result deleteById(@RequestParam Integer id){
        return diseaseNewsService.deleteById(id) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }
}