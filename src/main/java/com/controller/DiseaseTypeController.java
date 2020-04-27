package com.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.entity.DiseaseType;
import com.entity.dto.DiseaseTypeDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.service.DiseaseTypeExtService;
import com.service.DiseaseTypeService;
import com.util.MyFileUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import com.common.Result;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.exception.CodeMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (DiseaseType)表控制层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
@CrossOrigin(origins = "*")
@Api(description = "DiseaseTypeController模块")
@RestController
@RequestMapping("diseaseType")
public class DiseaseTypeController {
    /**
     * 服务对象
     */
    @Resource
    private DiseaseTypeService diseaseTypeService;
    @Resource
    private DiseaseTypeExtService diseaseTypeExtService;

    @ApiOperation("根据病种名称查询病种")
    @PostMapping("selectByName")
    public Result selectByName(@RequestBody DiseaseType diseaseType) {
        diseaseType.setDefaultPage();
        PageHelper.startPage(diseaseType.getPage(), diseaseType.getPageSize());
        List<DiseaseType> typeList = diseaseTypeService.queryAllByDiseaseType(diseaseType);
        PageInfo<DiseaseType> typePageInfo= new PageInfo<>(typeList);

        return Result.success(typePageInfo);
    }

	@ApiOperation("根据id获取详情")
    @PostMapping("selectById")
    public Result selectById(@RequestParam Integer id) {
        return Result.success(this.diseaseTypeService.queryById(id));
    }

    @ApiOperation("获取所有病种类型")
    @PostMapping("selectAll")
    public Result selectAll() {
        return Result.success(this.diseaseTypeService.queryAll());
    }

	@ApiOperation("添加记录")
    @PostMapping("add")
    public Result add(@RequestBody DiseaseType diseaseType){
        if(Objects.isNull(diseaseType.getName()) || diseaseType.getName().isEmpty()){
            return Result.error(new CodeMsg("慢性病病种名称为空"));
        }
        List<String> diseaseNameList = new ArrayList<>(diseaseTypeService.queryAllByDiseaseType(null))
                .stream().map(DiseaseType::getName).collect(Collectors.toList());
        if (diseaseNameList.contains(diseaseType.getName())) {
            return Result.error(new CodeMsg("该慢性病病种名称已存在"));
        }
        return null != diseaseTypeService.insert(diseaseType) ?
                Result.success(diseaseType) : Result.error(CodeMsg.ERROR);
    }

	@ApiOperation("更新记录")
    @PostMapping("update")
    public Result update(@RequestBody DiseaseType diseaseType){
        if(Objects.isNull(diseaseType.getId())){
            return Result.error(CodeMsg.UPDATE_ID_NULL);
        }
        if(Objects.isNull(diseaseType.getName()) || diseaseType.getName().isEmpty()){
            return Result.error(new CodeMsg("慢性病病种名称为空"));
        }
        List<DiseaseType> diseaseTypeList = diseaseTypeService.queryAllByDiseaseType(null);
        for (DiseaseType type : diseaseTypeList) {
            if(type.getName().equals(diseaseType.getName())
                    && !type.getId().equals(diseaseType.getId())){
                return Result.error(new CodeMsg("该慢性病病种名称已存在"));
            }
        }
        return diseaseTypeService.update(diseaseType) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }

    @ApiOperation(value = "将病种信息的Excel文件导入数据库")
    @PostMapping("import")
    public Result importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        if (null == file) {
            return Result.error(new CodeMsg("文件读取错误，无效的文件!"));
        }

        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<DiseaseType> excelList = reader.read(1, 2, DiseaseType.class);

        if (null == excelList || excelList.size() == 0) {
            return Result.error(new CodeMsg("文件数据为空!"));
        }

        List<String> diseaseNameList = new ArrayList<>(diseaseTypeService.queryAllByDiseaseType(null))
                .stream().map(DiseaseType::getName).collect(Collectors.toList());

        //判断数据合法性
        int size = excelList.size();
        for (int i = 0; i < size; i++) {
            int j = 0;
            DiseaseType diseaseType = excelList.get(i);
            String name = diseaseType.getName();
            String symptom = diseaseType.getSymptom();
            if (Objects.isNull(name) || name.isEmpty()) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 1 + "列出错" + " 出错原因:病种名称为空"));
            }
            if (Objects.isNull(symptom) || symptom.isEmpty()) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 2 + "列出错" + " 出错原因:常见症状为空"));
            }

            if (diseaseNameList.contains(name)) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 1 + "列出错" + " 出错原因:病种名称已存在"));
            }

            //更新已有的名称和单位列表
            diseaseNameList.add(name);

        }
        //批量插入数据库
        diseaseTypeExtService.insertList(excelList);
        return Result.success(CodeMsg.SUCCESS);
    }

    @ApiOperation("下载导入模板")
    @RequestMapping("export")
    public void export(HttpServletResponse response) throws Exception {

        List<DiseaseType> diseaseTypes = diseaseTypeService.queryAllByDiseaseType(null);
        ArrayList<DiseaseTypeDTO> rows = new ArrayList<>();
        for (DiseaseType diseaseType : diseaseTypes) {
            DiseaseTypeDTO diseaseTypeDTO = new DiseaseTypeDTO();
            BeanUtils.copyProperties(diseaseType, diseaseTypeDTO);
            rows.add(diseaseTypeDTO);
        }

        String fileName = "病种信息导出" + DateUtil.now() + ".xlsx";
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        ExcelWriter writer = ExcelUtil.getWriter(fileName);
        writer.write(rows, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=\""+fileName+"\"");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);

    }

    @ApiOperation("下载导入模板")
    @RequestMapping("download")
    public void download(HttpServletResponse response) throws Exception {
//        fileName = MyFileUtil.uploadRoot + "/" + fileName;
        String fileName = "病种导入模板.xlsx";
        String path = MyFileUtil.uploadRoot + "\\病种导入模板.xlsx";
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));

        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("multipart/form-data");

        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        try {
            int len = 0;
            while ((len = bis.read()) != -1) {
                out.write(len);
                out.flush();
            }
        } finally {
            out.close();
            bis.close();
        }
    }

	@ApiOperation("根据id删除记录")
    @PostMapping("deleteById")
    public Result deleteById(@RequestParam Integer id){
        return diseaseTypeService.deleteById(id) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }
}