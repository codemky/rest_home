package com.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.entity.*;
import com.entity.dto.MedicineDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.service.MedicineExtService;
import com.service.MedicineService;
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

/**
 * (Medicine)表控制层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
@CrossOrigin(origins = "*")
@Api(description = "MedicineController模块")
@RestController
@RequestMapping("medicine")
public class MedicineController {

    @Resource
    private MedicineService medicineService;
    @Resource
    private MedicineExtService medicineExtService;

    @ApiOperation("获取所有药品")
    @PostMapping("selectAll")
    public Result selectAll() {
        return Result.success(medicineService.queryAll());
    }

    @ApiOperation("根据药物名称或单位查询药物列表")
    @PostMapping("selectByMedicine")
    public Result selectByMedicine(@RequestBody Medicine medicine) {
        medicine.setDefaultPage();
        PageHelper.startPage(medicine.getPage(), medicine.getPageSize());
        List<Medicine> newsList = medicineService.queryAllByMedicine(medicine);
        PageInfo<Medicine> medicineInfo = new PageInfo<>(newsList);

        return Result.success(medicineInfo);
    }

	@ApiOperation("根据id获取详情")
    @PostMapping("selectById")
    public Result selectById(@RequestParam Integer id) {
        return Result.success(this.medicineService.queryById(id));
    }

	@ApiOperation("添加记录")
    @PostMapping("add")
    public Result add(@RequestBody Medicine medicine){
        String name = medicine.getName();
        String unit = medicine.getUnit();
        if (Objects.isNull(name) || name.isEmpty()) {
            return Result.error(new CodeMsg("药品名称为空"));
        }
        if (Objects.isNull(unit) || unit.isEmpty()) {
            return Result.error(new CodeMsg("药品单位为空"));
        }
        List<Medicine> medicineList = new ArrayList<>(medicineService.queryAllByMedicine(null));
        ArrayList<String> names = new ArrayList<>();
        for (Medicine medicine1 : medicineList) {
            names.add(medicine1.getName() + medicine1.getUnit());
        }
        if (names.contains(name+unit)) {
            return Result.error(new CodeMsg("该药品和单位已存在"));
        }

        return null != medicineService.insert(medicine) ?
                Result.success(medicine) : Result.error(CodeMsg.ERROR);
    }

	@ApiOperation("更新记录")
    @PostMapping("update")
    public Result update(@RequestBody Medicine medicine){
        if(Objects.isNull(medicine.getId())){
            return Result.error(CodeMsg.UPDATE_ID_NULL);
        }
        String name = medicine.getName();
        String unit = medicine.getUnit();
        if (Objects.isNull(name) || name.isEmpty()) {
            return Result.error(new CodeMsg("药品名称为空"));
        }
        if (Objects.isNull(unit) || unit.isEmpty()) {
            return Result.error(new CodeMsg("药品单位为空"));
        }
        List<Medicine> medicineList = new ArrayList<>(medicineService.queryAllByMedicine(null));
        for (Medicine medicine1 : medicineList) {
            if((name + unit).equals(medicine1.getName() + medicine1.getUnit())
                    && !medicine1.getId().equals(medicine.getId())){
                return Result.error(new CodeMsg("该药品和单位已存在"));
            }
        }
        return medicineService.update(medicine) > 0 ?
                Result .success(null) : Result.error(CodeMsg.ERROR);
    }

    @ApiOperation(value = "将药品信息的Excel文件导入数据库")
    @PostMapping("import")
    public Result importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        if (null == file) {
            return Result.error(new CodeMsg("文件读取错误，无效的文件!"));
        }

        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<Medicine> excelList = reader.read(1, 2, Medicine.class);

        if (null == excelList || excelList.size() == 0) {
            return Result.error(new CodeMsg("文件数据为空!"));
        }

        List<Medicine> medicineList = new ArrayList<>(medicineService.queryAllByMedicine(null));
        ArrayList<String> names = new ArrayList<>();
        for (Medicine medicine : medicineList) {
            names.add(medicine.getName() + medicine.getUnit());
        }

        //判断数据合法性
        int size = excelList.size();
        for (int i = 0; i < size; i++) {
            int j = 0;
            Medicine medicine = excelList.get(i);
            String name = medicine.getName();
            String unit = medicine.getUnit();
            if (Objects.isNull(name) || name.isEmpty()) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 1 + "列出错" + " 出错原因:药品名称为空"));
            }
            if (Objects.isNull(unit) || unit.isEmpty()) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 2 + "列出错" + " 出错原因:药品单位为空"));
            }

            if (names.contains(name+unit)) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行出错" + " 出错原因:该药品和单位已存在"));
            }

            //更新已有的名称和单位列表
            names.add(name+unit);

        }
        //批量插入数据库
        medicineExtService.insertList(excelList);
        return Result.success(CodeMsg.SUCCESS);
    }

    @ApiOperation("下载导入模板")
    @RequestMapping("export")
    public void export(HttpServletResponse response) throws Exception {

        List<Medicine> medicines = medicineService.queryAllByMedicine(null);
        ArrayList<MedicineDTO> rows = new ArrayList<>();
        for (Medicine medicine : medicines) {
            MedicineDTO medicineDTO = new MedicineDTO();
            BeanUtils.copyProperties(medicine, medicineDTO);
            rows.add(medicineDTO);
        }

        String fileName = "药品信息导出" + DateUtil.now() + ".xlsx";
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
        String fileName = "药品导入模板.xlsx";
        String path = MyFileUtil.uploadRoot + "\\药品导入模板.xlsx";
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
        return medicineService.deleteById(id) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }
}