package com.controller;

import cn.hutool.core.lang.UUID;
import com.entity.*;
import com.entity.criteria.CureCriteria;
import com.entity.vo.DiseaseCureVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.service.*;
import com.util.MyFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;
import com.common.Result;
import javax.annotation.Resource;

import com.exception.CodeMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (DiseaseCure)表控制层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
@CrossOrigin(origins = "*")
@Api(description = "DiseaseCureController模块")
@RestController
@RequestMapping("diseaseCure")
public class DiseaseCureController {

    @Resource
    private DiseaseCureService diseaseCureService;
    @Resource
    private DiseaseCureExtService diseaseCureExtService;
    @Resource
    private CureMedicineExtService cureMedicineExtService;
    @Resource
    private CureMedicineService cureMedicineService;
    @Resource
    private MedicineService medicineService;
    @Resource
    private FilesService filesService;
    @Autowired
    private DataSourceTransactionManager transactionManager;


    @ApiOperation("根据条件查询治疗记录列表")
    @PostMapping("selectByCriteria")
    public Result selectByMedicine(@RequestBody CureCriteria cureCriteria) {
        cureCriteria.setDefaultValue();
        PageHelper.startPage(cureCriteria.getPage(), cureCriteria.getPageSize());
        List<DiseaseCureVO> cureVOList = diseaseCureExtService.selectByCriteria(cureCriteria);
        PageInfo<DiseaseCureVO> cureInfo = new PageInfo<>(cureVOList);

        return Result.success(cureInfo);
    }

    @ApiOperation("根据id获取详情")
    @PostMapping("selectById")
    public Result selectById(@RequestParam Integer id) {
        return Result.success(this.diseaseCureExtService.selectById(id));
    }

    @ApiOperation("添加记录（带多文件）")
    @PostMapping(value = "add", consumes = "multipart/form-data")
//    @PostMapping("add")
    public Result add(@RequestParam("files") MultipartFile[] files,DiseaseCureVO cureVO) throws IOException {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def); // get

        ArrayList<String> strings = new ArrayList<>();

        //治疗记录部分
        DiseaseCure diseaseCure = new DiseaseCure();
        diseaseCure.setNum(Long.toString(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
        diseaseCure.setCreatetime(new Date());
        diseaseCure.setState(false);

        Integer patientid = cureVO.getPatientid();
        Integer doctorid = cureVO.getDoctorid();
        String content = cureVO.getContent();
        if (Objects.isNull(patientid))  {
            return Result.error(new CodeMsg("医生id为空"));
        }
        if (Objects.isNull(doctorid) ) {
            return Result.error(new CodeMsg("患者id为空"));
        }
        if (Objects.isNull(content) || content.isEmpty()) {
            return Result.error(new CodeMsg("治疗内容为空"));
        }
        diseaseCure.setPatientid(patientid);
        diseaseCure.setDoctorid(doctorid);
        diseaseCure.setContent(content);

        diseaseCure.setCaseid(cureVO.getCaseid());

        try {
            Integer cureId = diseaseCureService.insert(diseaseCure).getId();
            if( null == cureId ){
                transactionManager.rollback(status);
                return Result.error(CodeMsg.CREATE_FAILURE);
            }



            List<Integer> medicineList = cureVO.getMedicineList();
            List<Integer> numberList = cureVO.getNumberList();
            List<Integer> medicineIds = medicineService.queryAll().stream().map(Medicine::getId).collect(Collectors.toList());
            //判断是否有该药品
            for (Integer integer : medicineList) {
                if(!medicineIds.contains(integer)){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("id为"+integer+"的药品不存在"));
                }
            }

            //判断药品中是否有重复
            for(int i=0;i<medicineList.size();i++){
                for(int j=0;j<medicineList.size();j++){
                    if(i != j && medicineList.get(i).equals(medicineList.get(j))){
                        transactionManager.rollback(status);
                        return Result.error(new CodeMsg("第" + (i+1) + "行的药品和" + (j+1) +"行的药品重复添加"));
                    }
                }
            }

            if(null != cureVO.getMedicineList() && null != cureVO.getNumberList()){
                int checkResult = cureVO.checkMedicines();
                if( checkResult == 1){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品id为空"));
                }
                if( checkResult == 2){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品数量为空"));
                }
                if( checkResult == 3){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品数量和药品用量的记录数没有一致"));
                }

                ArrayList<CureMedicine> cureMedicines = new ArrayList<>();
                for(int i=0 ;i<medicineList.size() ;i++ ){
                    CureMedicine cureMedicine = new CureMedicine();
                    cureMedicine.setCureid(cureId);
                    cureMedicine.setMedicineid(medicineList.get(i));
                    cureMedicine.setNumber(numberList.get(i));
                    cureMedicines.add(cureMedicine);
                }

                int importNumber = cureMedicineExtService.insertList(cureMedicines);
                if(importNumber != cureMedicines.size()){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品和用量信息没有全部插入"));
                }
            }else{
                if( null != cureVO.getMedicineList() && null == cureVO.getNumberList() ){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品用量为空而id不为空"));
                }
                if( null == cureVO.getMedicineList() && null != cureVO.getNumberList() ){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品id为空而用量不为空"));
                }
            }

            if( null != files){
                ArrayList<String> fileNames = new ArrayList<>();
                for (MultipartFile file : files) {
                    String originalFilename = file.getOriginalFilename();
                    int pointIndex = originalFilename.indexOf(".");
                    String substring = originalFilename.substring(pointIndex);
                    String fileUUID = UUID.fastUUID().toString() + substring;

                    File file1 = new File(MyFileUtil.uploadRoot, fileUUID);
                    if(!file1.exists()){
                        file1.mkdirs();
                    }
                    file.transferTo(file1);
                    if (!new File(MyFileUtil.uploadRoot+ "\\" +fileUUID).exists()) {
                        fileNames.add(originalFilename);
                    }else{
                        strings.add(MyFileUtil.uploadRoot+ "\\" +fileUUID);
                    }

                    Files myFile = new Files();
                    myFile.setModule("治疗");
                    myFile.setInfoid(cureId);
                    myFile.setName(originalFilename);
                    myFile.setPath(fileUUID);
                    filesService.insert(myFile);

                }

                if(fileNames.size() != 0){
                    for (String string : strings) {
                        new File(string).delete();
                    }
                    transactionManager.rollback(status);
                    return Result.define(-1,"未能成功上传的文件如下",fileNames);
                }
            }

            transactionManager.commit(status);
        }catch (Exception e){
            transactionManager.rollback(status);
            e.printStackTrace();
        }finally {
            for (String string : strings) {
                new File(MyFileUtil.uploadRoot + "\\" + string).delete();
            }
        }

        return Result.success(null);
    }

    @ApiOperation("更新记录")
    @PostMapping(value = "update", consumes = "multipart/form-data")
    public Result update(@RequestParam("files") MultipartFile[] files,DiseaseCureVO cureVO) throws IOException {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def); // get

        ArrayList<String> strings = new ArrayList<>();

        Integer cureId = cureVO.getId();
        if( null == cureId ){
            return Result.error(CodeMsg.UPDATE_ID_NULL);
        }

        //治疗记录部分
        DiseaseCure diseaseCure = new DiseaseCure();
        diseaseCure.setUpdatetime(new Date());

        Integer patientid = cureVO.getPatientid();
        Integer doctorid = cureVO.getDoctorid();
        String content = cureVO.getContent();
        if (Objects.isNull(patientid))  {
            return Result.error(new CodeMsg("医生id为空"));
        }
        if (Objects.isNull(doctorid) ) {
            return Result.error(new CodeMsg("患者id为空"));
        }
        if (Objects.isNull(content) || content.isEmpty()) {
            return Result.error(new CodeMsg("治疗内容为空"));
        }
        diseaseCure.setId(cureId);
        diseaseCure.setPatientid(patientid);
        diseaseCure.setDoctorid(doctorid);
        diseaseCure.setContent(content);

        diseaseCure.setResult(cureVO.getResult());
        diseaseCure.setRemark(cureVO.getRemark());

        diseaseCure.setCaseid(cureVO.getCaseid());

        try {
            if (diseaseCureService.update(diseaseCure) == 0) {
                transactionManager.rollback(status);
                return Result.error(CodeMsg.UPDATE_FAILURE);
            }

            //删除原本的用药信息
            CureMedicine oldMedicine = new CureMedicine();
            oldMedicine.setCureid(cureId);
            List<Integer> cureMedicineIds = cureMedicineService.queryAllByCureMedicine(oldMedicine)
                    .stream().map(CureMedicine::getId).collect(Collectors.toList());
            if(cureMedicineIds.size() > 0){
                if (cureMedicineExtService.deleteList(cureMedicineIds) != cureMedicineIds.size()) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("删除原本的用药信息失败"));
                }
            }

            List<Integer> medicineList = cureVO.getMedicineList();
            List<Integer> numberList = cureVO.getNumberList();
            List<Integer> medicineIds = medicineService.queryAll().stream().map(Medicine::getId).collect(Collectors.toList());
            //判断是否有该药品
            for (Integer integer : medicineList) {
                if(!medicineIds.contains(integer)){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("id为" + integer + "的药品不存在"));
                }
            }

            //判断药品中是否有重复
            for(int i=0;i<medicineList.size();i++){
                for(int j=0;j<medicineList.size();j++){
                    if(i != j && medicineList.get(i).equals(medicineList.get(j))){
                        transactionManager.rollback(status);
                        return Result.error(new CodeMsg("第" + (i+1) + "行的药品和" + (j+1) +"行的药品重复添加"));
                    }
                }
            }

            if( null != cureVO.getMedicineList() && null != cureVO.getNumberList() ){
                int checkResult = cureVO.checkMedicines();
                if( checkResult == 1){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品id为空"));
                }
                if( checkResult == 2){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品数量为空"));
                }
                if( checkResult == 3){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品数量和药品用量的记录数没有一致"));
                }

                ArrayList<CureMedicine> cureMedicines = new ArrayList<>();
                for(int i=0 ;i<medicineList.size() ;i++ ){
                    CureMedicine cureMedicine = new CureMedicine();
                    cureMedicine.setCureid(cureId);
                    cureMedicine.setMedicineid(medicineList.get(i));
                    cureMedicine.setNumber(numberList.get(i));
                    cureMedicines.add(cureMedicine);
                }

                int importNumber = cureMedicineExtService.insertList(cureMedicines);
                if(importNumber != cureMedicines.size()){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品和用量信息没有全部插入"));
                }
            }else{
                if( null != cureVO.getMedicineList() && null == cureVO.getNumberList() ){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品用量为空而id不为空"));
                }
                if( null == cureVO.getMedicineList() && null != cureVO.getNumberList() ){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品id为空而用量不为空"));
                }
            }


            if( null != files){
                ArrayList<String> fileNames = new ArrayList<>();
                for (MultipartFile file : files) {
                    String originalFilename = file.getOriginalFilename();
                    int pointIndex = originalFilename.indexOf(".");
                    String substring = originalFilename.substring(pointIndex);
                    String fileUUID = UUID.fastUUID().toString() + substring;

                    File file1 = new File(MyFileUtil.uploadRoot, fileUUID);
                    if(!file1.exists()){
                        file1.mkdirs();
                    }
                    file.transferTo(file1);
                    if (!new File(MyFileUtil.uploadRoot+ "\\" +fileUUID).exists()) {
                        fileNames.add(originalFilename);
                    }else{
                        strings.add(MyFileUtil.uploadRoot+ "\\" +fileUUID);
                    }

                    Files myFile = new Files();
                    myFile.setModule("治疗");
                    myFile.setInfoid(cureId);
                    myFile.setName(originalFilename);
                    myFile.setPath(fileUUID);
                    filesService.insert(myFile);

                }

                if(fileNames.size() != 0){
                    for (String string : strings) {
                        new File(string).delete();
                    }
                    transactionManager.rollback(status);
                    return Result.define(-1,"未能成功上传的文件如下",fileNames);
                }
            }

            List<Integer> fileIds = cureVO.getFileIds();
            if( null != fileIds ){
                for (Integer fileId : fileIds) {
                    Files dBFile = filesService.queryById(fileId);
                    if( null != dBFile ){
                        if(null != dBFile.getPath()){
                            File file = new File(MyFileUtil.uploadRoot + "\\" + dBFile.getPath());
                            if( file.exists() ){
                                file.delete();
                            }
                        }
                    }
                }
                for (Integer fileId : fileIds) {
                    filesService.deleteById(fileId);
                }
            }

            transactionManager.commit(status);
        }catch (Exception e){
            transactionManager.rollback(status);
            e.printStackTrace();
        }finally {
            for (String string : strings) {
                new File(MyFileUtil.uploadRoot + "\\" + string).delete();
            }
        }

        return Result.success(null);
    }

    @ApiOperation("治疗记录报告")
    @PostMapping(value = "report", consumes = "multipart/form-data")
    public Result report(@RequestParam("files") MultipartFile[] files,DiseaseCureVO cureVO) throws IOException {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def); // get

        ArrayList<String> strings = new ArrayList<>();

        Integer cureId = cureVO.getId();
        if( null == cureId ){
            return Result.error(CodeMsg.UPDATE_ID_NULL);
        }

        //治疗记录部分
        DiseaseCure diseaseCure = new DiseaseCure();
        diseaseCure.setUpdatetime(new Date());

        Integer patientid = cureVO.getPatientid();
        Integer doctorid = cureVO.getDoctorid();
        String content = cureVO.getContent();
        String result = cureVO.getResult();
        String remark = cureVO.getRemark();
        if (Objects.isNull(patientid))  {
            return Result.error(new CodeMsg("医生id为空"));
        }
        if (Objects.isNull(doctorid) ) {
            return Result.error(new CodeMsg("患者id为空"));
        }
        if (Objects.isNull(content) || content.isEmpty()) {
            return Result.error(new CodeMsg("治疗内容为空"));
        }
        if (Objects.isNull(result) || result.isEmpty()) {
            return Result.error(new CodeMsg("治疗结果为空"));
        }
        if (Objects.isNull(remark) || remark.isEmpty()) {
            return Result.error(new CodeMsg("结果评价为空"));
        }

        diseaseCure.setId(cureId);
        diseaseCure.setPatientid(patientid);
        diseaseCure.setDoctorid(doctorid);
        diseaseCure.setContent(content);
        diseaseCure.setResult(result);
        diseaseCure.setRemark(remark);
        diseaseCure.setState(true);

        diseaseCure.setCaseid(cureVO.getCaseid());

        try {
            if (diseaseCureService.update(diseaseCure) == 0) {
                transactionManager.rollback(status);
                return Result.error(CodeMsg.UPDATE_FAILURE);
            }


            //删除原本的用药信息
            CureMedicine oldMedicine = new CureMedicine();
            oldMedicine.setCureid(cureId);
            List<Integer> cureMedicineIds = cureMedicineService.queryAllByCureMedicine(oldMedicine)
                    .stream().map(CureMedicine::getId).collect(Collectors.toList());
            if(cureMedicineIds.size() > 0){
                if (cureMedicineExtService.deleteList(cureMedicineIds) != cureMedicineIds.size()) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("删除原本的用药信息失败"));
                }
            }

            List<Integer> medicineList = cureVO.getMedicineList();
            List<Integer> numberList = cureVO.getNumberList();
            List<Integer> medicineIds = medicineService.queryAll().stream().map(Medicine::getId).collect(Collectors.toList());

            //判断是否有该药品
            for (Integer integer : medicineList) {
                if(!medicineIds.contains(integer)){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("id为"+integer+"的药品不存在"));
                }
            }

            //判断药品中是否有重复
            for(int i=0;i<medicineList.size();i++){
                for(int j=0;j<medicineList.size();j++){
                    if(i != j && medicineList.get(i).equals(medicineList.get(j))){
                        transactionManager.rollback(status);
                        return Result.error(new CodeMsg("第" + (i+1) + "行的药品和" + (j+1) +"行的药品重复添加"));
                    }
                }
            }


            if(null != cureVO.getMedicineList() && null != cureVO.getNumberList()){
                int checkResult = cureVO.checkMedicines();
                if( checkResult == 1){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品id为空"));
                }
                if( checkResult == 2){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品数量为空"));
                }
                if( checkResult == 3){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品数量和药品用量的记录数没有一致"));
                }

                ArrayList<CureMedicine> cureMedicines = new ArrayList<>();
                for(int i=0 ;i<medicineList.size() ;i++ ){
                    CureMedicine cureMedicine = new CureMedicine();
                    cureMedicine.setCureid(cureId);
                    cureMedicine.setMedicineid(medicineList.get(i));
                    cureMedicine.setNumber(numberList.get(i));
                    cureMedicines.add(cureMedicine);
                }

                int importNumber = cureMedicineExtService.insertList(cureMedicines);
                if(importNumber != cureMedicines.size()){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品和用量信息没有全部插入"));
                }
            }else{
                if( null != cureVO.getMedicineList() && null == cureVO.getNumberList() ){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品用量为空而id不为空"));
                }
                if( null == cureVO.getMedicineList() && null != cureVO.getNumberList() ){
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品id为空而用量不为空"));
                }
            }

            if( null != files){
                ArrayList<String> fileNames = new ArrayList<>();
                for (MultipartFile file : files) {
                    String originalFilename = file.getOriginalFilename();
                    int pointIndex = originalFilename.indexOf(".");
                    String substring = originalFilename.substring(pointIndex);
                    String fileUUID = UUID.fastUUID().toString() + substring;

                    File file1 = new File(MyFileUtil.uploadRoot, fileUUID);
                    if(!file1.exists()){
                        file1.mkdirs();
                    }
                    file.transferTo(file1);
                    if (!new File(MyFileUtil.uploadRoot+ "\\" +fileUUID).exists()) {
                        fileNames.add(originalFilename);
                    }else{
                        strings.add(MyFileUtil.uploadRoot+ "\\" +fileUUID);
                    }

                    Files myFile = new Files();
                    myFile.setModule("治疗");
                    myFile.setInfoid(cureId);
                    myFile.setName(originalFilename);
                    myFile.setPath(fileUUID);
                    filesService.insert(myFile);

                }

                if(fileNames.size() != 0){
                    for (String string : strings) {
                        new File(string).delete();
                    }
                    transactionManager.rollback(status);
                    return Result.define(-1,"未能成功上传的文件如下",fileNames);
                }
            }

            List<Integer> fileIds = cureVO.getFileIds();
            if( null != fileIds ){
                for (Integer fileId : fileIds) {
                    Files dBFile = filesService.queryById(fileId);
                    if( null != dBFile ){
                        if(null != dBFile.getPath()){
                            File file = new File(MyFileUtil.uploadRoot + "\\" + dBFile.getPath());
                            if( file.exists() ){
                                file.delete();
                            }
                        }
                    }
                }
                for (Integer fileId : fileIds) {
                    filesService.deleteById(fileId);
                }
            }

            transactionManager.commit(status);
        }catch (Exception e){
            transactionManager.rollback(status);
            e.printStackTrace();
        }finally {
            for (String string : strings) {
                new File(MyFileUtil.uploadRoot + "\\" + string).delete();
            }
        }

        return Result.success(null);
    }


    @ApiOperation("根据id删除记录")
    @PostMapping("deleteById")
    public Result deleteById(@RequestParam Integer id){
        return medicineService.deleteById(id) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }

}