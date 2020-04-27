package com.controller;

import cn.hutool.core.lang.UUID;
import com.entity.*;
import com.entity.criteria.CaseCriteria;
import com.entity.criteria.MonitorCriteria;
import com.entity.vo.DiseaseCaseVO;
import com.entity.vo.DiseaseCaseVO;
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

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (DiseaseCase)表控制层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
@CrossOrigin(origins = "*")
@Api(description = "DiseaseCaseController模块")
@RestController
@RequestMapping("diseaseCase")
public class DiseaseCaseController {

    @Resource
    private DiseaseCaseService diseaseCaseService;
    @Resource
    private DiseaseCaseExtService diseaseCaseExtService;
    @Resource
    private CureMonitorService cureMonitorService;
    @Resource
    private CureMonitorExtService cureMonitorExtService;
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


    @ApiOperation("根据条件查询监测记录列表")
    @PostMapping("selectByCriteria")
    public Result selectByCriteria(@RequestBody CaseCriteria criteria) {
        criteria.setDefaultValue();
        PageHelper.startPage(criteria.getPage(), criteria.getPageSize());
        List<DiseaseCaseVO> caseVOList = diseaseCaseExtService.selectByCriteria(criteria);
        PageInfo<DiseaseCaseVO> caseInfo = new PageInfo<>(caseVOList);

        return Result.success(caseInfo);
    }

    @ApiOperation("获取所有未进行过治疗监测并且申报人为自己的病例记录")
    @PostMapping("selectAllWithoutCure")
    public Result selectAllWithoutCure(@RequestBody CaseCriteria criteria) {
        if( null == criteria.getApplyid()){
            return Result.error(new CodeMsg("申报人id为空"));
        }
        criteria.setDefaultValue();
        criteria.setState(1);
        PageHelper.startPage(criteria.getPage(), criteria.getPageSize());
        List<DiseaseCaseVO> caseVOList = diseaseCaseExtService.selectByCriteria(criteria);
        PageInfo<DiseaseCaseVO> caseInfo = new PageInfo<>(caseVOList);

        return Result.success(caseInfo);
    }

    @ApiOperation("获取所有状态为未审批的病例记录")
    @PostMapping("selectAllWithoutCheck")
    public Result selectAllWithoutCheck(@RequestBody CaseCriteria criteria) {
        criteria.setDefaultValue();
        criteria.setState(0);
        PageHelper.startPage(criteria.getPage(), criteria.getPageSize());
        List<DiseaseCaseVO> caseVOList = diseaseCaseExtService.selectByCriteria(criteria);
        PageInfo<DiseaseCaseVO> caseInfo = new PageInfo<>(caseVOList);

        return Result.success(caseInfo);
    }


    @ApiOperation("获取患者用户的所有患病病种")
    @PostMapping("selectAllUserCaseByUserId")
    public Result selectAllUserCaseByUserId(@RequestParam Integer id) {
        return Result.success(this.diseaseCaseExtService.selectUserDiseaseByUserId(id));
    }

    @ApiOperation("获取所有信息")
    @PostMapping("selectAll")
    public Result selectAll() {
        return Result.success(this.diseaseCaseService.queryAll());
    }

    @ApiOperation("根据id获取详情")
    @PostMapping("selectById")
    public Result selectById(@RequestParam Integer id) {
        return Result.success(this.diseaseCaseService.queryById(id));
    }

    @ApiOperation("添加记录（带多文件）")
    @PostMapping(value = "add", consumes = "multipart/form-data")
    public Result add(@RequestParam("files") MultipartFile[] files, DiseaseCaseVO caseVO) throws IOException {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def); // get

        ArrayList<String> strings = new ArrayList<>();

        //治疗记录部分
        DiseaseCase diseaseCase = new DiseaseCase();
        diseaseCase.setNum(Long.toString(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
        diseaseCase.setApplytime(new Date());

        Integer patientid = caseVO.getPatientid();
        Integer applyid = caseVO.getApplyid();
//        Integer checkid = caseVO.getCheckid();
        Integer typeid = caseVO.getTypeid();
        String symptom = caseVO.getSymptom();
        Float sugar = caseVO.getSugar();
        Integer lowpressure = caseVO.getLowpressure();
        Integer highpressure = caseVO.getHighpressure();
        Date attacktime = caseVO.getAttacktime();
//        String remark = caseVO.getRemark();

//        if (Objects.isNull(caseVO.getId()))  {
//            return Result.error(new CodeMsg("记录id为空"));
//        }
        if (Objects.isNull(symptom) || symptom.isEmpty()) {
            return Result.error(new CodeMsg("症状表现为空"));
        }
        if (Objects.isNull(typeid)) {
            return Result.error(new CodeMsg("病种id为空"));
        }
        if (Objects.isNull(patientid)) {
            return Result.error(new CodeMsg("患者id为空"));
        }
        if (Objects.isNull(applyid)) {
            return Result.error(new CodeMsg("申报人id为空"));
        }
//        if (Objects.isNull(checkid) ) {
//            return Result.error(new CodeMsg("审批人id为空"));
//        }
        if (Objects.isNull(sugar)) {
            return Result.error(new CodeMsg("血糖值为空"));
        }
        if (Objects.isNull(lowpressure)) {
            return Result.error(new CodeMsg("舒张压为空"));
        }
        if (Objects.isNull(highpressure)) {
            return Result.error(new CodeMsg("收缩压为空"));
        }
        if (Objects.isNull(attacktime)) {
            return Result.error(new CodeMsg("发病时间为空"));
        }
//        if (Objects.isNull(remark) ) {
//            return Result.error(new CodeMsg("审批意见为空"));
//        }

//        diseaseCase.setId(caseVO.getId());
        diseaseCase.setPatientid(patientid);
        diseaseCase.setApplyid(applyid);
//        diseaseCase.setCheckid(checkid);
        diseaseCase.setTypeid(typeid);
        diseaseCase.setSymptom(symptom);
        diseaseCase.setSugar(sugar);
        diseaseCase.setLowpressure(lowpressure);
        diseaseCase.setHighpressure(highpressure);
//        diseaseCase.setRemark(remark);
        diseaseCase.setState(0);
        diseaseCase.setAttacktime(attacktime);

        DiseaseCase allCase = new DiseaseCase();
        allCase.setPatientid(patientid);
        allCase.setTypeid(typeid);
        if (diseaseCaseService.queryAllByDiseaseCase(allCase).size() != 0) {
            return Result.error(new CodeMsg("患者已确诊过该病例"));
        }

        try {
            Integer caseId = diseaseCaseService.insert(diseaseCase).getId();
            if (null == caseId) {
                return Result.error(CodeMsg.CREATE_FAILURE);
            }

            if (null != files) {
                ArrayList<String> fileNames = new ArrayList<>();
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String originalFilename = file.getOriginalFilename();
                        int pointIndex = originalFilename.indexOf(".");
                        String substring = originalFilename.substring(pointIndex);
                        String fileUUID = UUID.fastUUID().toString() + substring;

                        File file1 = new File(MyFileUtil.uploadRoot, fileUUID);
                        if (!file1.exists()) {
                            file1.mkdirs();
                        }
                        file.transferTo(file1);
                        if (!new File(MyFileUtil.uploadRoot + "\\" + fileUUID).exists()) {
                            fileNames.add(originalFilename);
                        } else {
                            strings.add(MyFileUtil.uploadRoot + "\\" + fileUUID);
                        }

                        Files myFile = new Files();
                        myFile.setModule("病例");
                        myFile.setInfoid(caseId);
                        myFile.setName(originalFilename);
                        myFile.setPath(fileUUID);
                        filesService.insert(myFile);
                    }

                }

                if (fileNames.size() != 0) {
                    for (String string : strings) {
                        new File(string).delete();
                    }
                    transactionManager.rollback(status);
                    return Result.define(-1, "未能成功上传的文件如下", fileNames);
                }
            }

            List<Integer> fileIds = caseVO.getFileIds();
            if (null != fileIds) {
                for (Integer fileId : fileIds) {
                    Files dBFile = filesService.queryById(fileId);
                    if (null != dBFile) {
                        if (null != dBFile.getPath()) {
                            File file = new File(MyFileUtil.uploadRoot + "\\" + dBFile.getPath());
                            if (file.exists()) {
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
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
        } finally {
            for (String string : strings) {
                new File(MyFileUtil.uploadRoot + "\\" + string).delete();
            }
        }

        return Result.success(null);
    }

    @ApiOperation("更新记录")
    @PostMapping("update")
    public Result update(@RequestParam("files") MultipartFile[] files, DiseaseCaseVO caseVO) throws IOException {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def); // get

        ArrayList<String> strings = new ArrayList<>();

        //治疗记录部分
        DiseaseCase diseaseCase = new DiseaseCase();

        Integer patientid = caseVO.getPatientid();
        Integer applyid = caseVO.getApplyid();
        Integer checkid = caseVO.getCheckid();
        Integer typeid = caseVO.getTypeid();
        String symptom = caseVO.getSymptom();
        Float sugar = caseVO.getSugar();
        Integer lowpressure = caseVO.getLowpressure();
        Integer highpressure = caseVO.getHighpressure();
        Date attacktime = caseVO.getAttacktime();
        String remark = caseVO.getRemark();
        Integer state = caseVO.getState();

        if (Objects.isNull(caseVO.getId())) {
            return Result.error(new CodeMsg("记录id为空"));
        }
        if (Objects.isNull(typeid)) {
            return Result.error(new CodeMsg("病种id为空"));
        }
        if (Objects.isNull(symptom) || symptom.isEmpty()) {
            return Result.error(new CodeMsg("症状表现为空"));
        }
//        if (Objects.isNull(remark) || remark.isEmpty() )  {
//            return Result.error(new CodeMsg("审批意见为空"));
//        }
        if (Objects.isNull(patientid)) {
            return Result.error(new CodeMsg("患者id为空"));
        }
//        if (Objects.isNull(applyid) ) {
//            return Result.error(new CodeMsg("申报人id为空"));
//        }
//        if (Objects.isNull(checkid) ) {
//            return Result.error(new CodeMsg("审批人id为空"));
//        }
        if (Objects.isNull(sugar)) {
            return Result.error(new CodeMsg("血糖值为空"));
        }
        if (Objects.isNull(lowpressure)) {
            return Result.error(new CodeMsg("舒张压为空"));
        }
        if (Objects.isNull(highpressure)) {
            return Result.error(new CodeMsg("收缩压为空"));
        }
        if (Objects.isNull(attacktime)) {
            return Result.error(new CodeMsg("发病时间为空"));
        }
        if (null != state) {
            if (state != 0 && state != 1 && state != 2 && state != 3) {
                return Result.error(new CodeMsg("记录状态state字段不合法不在（0、1、2、3）范围内"));
            }
        }

        diseaseCase.setId(caseVO.getId());
        diseaseCase.setPatientid(patientid);
        diseaseCase.setApplyid(applyid);
        diseaseCase.setCheckid(checkid);
        diseaseCase.setTypeid(typeid);
        diseaseCase.setSymptom(symptom);
        diseaseCase.setSugar(sugar);
        diseaseCase.setLowpressure(lowpressure);
        diseaseCase.setHighpressure(highpressure);
        diseaseCase.setRemark(remark);
        diseaseCase.setState(state);
        diseaseCase.setAttacktime(attacktime);

        DiseaseCase allCase = new DiseaseCase();
        allCase.setPatientid(patientid);
        allCase.setTypeid(typeid);
        List<DiseaseCase> diseaseCases = diseaseCaseService.queryAllByDiseaseCase(allCase);
        for (DiseaseCase aCase : diseaseCases) {
            if (!aCase.getId().equals(caseVO.getId())) {
                return Result.error(new CodeMsg("患者已确诊过该病例"));
            }
        }

        try {
            Integer caseId = caseVO.getId();
            if (diseaseCaseService.update(diseaseCase) == 0) {
                transactionManager.rollback(status);
                return Result.error(CodeMsg.UPDATE_FAILURE);
            }

            if (null != files) {
                ArrayList<String> fileNames = new ArrayList<>();
                for (MultipartFile file : files) {
                    String originalFilename = file.getOriginalFilename();
                    int pointIndex = originalFilename.indexOf(".");
                    String substring = originalFilename.substring(pointIndex);
                    String fileUUID = UUID.fastUUID().toString() + substring;

                    File file1 = new File(MyFileUtil.uploadRoot, fileUUID);
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    file.transferTo(file1);
                    if (!new File(MyFileUtil.uploadRoot + "\\" + fileUUID).exists()) {
                        fileNames.add(originalFilename);
                    } else {
                        strings.add(MyFileUtil.uploadRoot + "\\" + fileUUID);
                    }

                    Files myFile = new Files();
                    myFile.setModule("病例");
                    myFile.setInfoid(caseId);
                    myFile.setName(originalFilename);
                    myFile.setPath(fileUUID);
                    filesService.insert(myFile);

                }

                if (fileNames.size() != 0) {
                    for (String string : strings) {
                        new File(string).delete();
                    }
                    transactionManager.rollback(status);
                    return Result.define(-1, "未能成功上传的文件如下", fileNames);
                }
            }

            List<Integer> fileIds = caseVO.getFileIds();
            if (null != fileIds) {
                for (Integer fileId : fileIds) {
                    Files dBFile = filesService.queryById(fileId);
                    if (null != dBFile) {
                        if (null != dBFile.getPath()) {
                            File file = new File(MyFileUtil.uploadRoot + "\\" + dBFile.getPath());
                            if (file.exists()) {
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
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
        } finally {
            for (String string : strings) {
                new File(MyFileUtil.uploadRoot + "\\" + string).delete();
            }
        }

        return Result.success(null);
    }


    @ApiOperation("审批")
    @PostMapping("check")
    public Result check(@RequestParam("files") MultipartFile[] files, DiseaseCaseVO caseVO) throws IOException {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def); // get

        ArrayList<String> strings = new ArrayList<>();

        //治疗记录部分
        DiseaseCase diseaseCase = new DiseaseCase();
        diseaseCase.setChecktime(new Date());

//        Integer patientid = caseVO.getPatientid();
//        Integer applyid = caseVO.getApplyid();
        Integer checkid = caseVO.getCheckid();
//        Integer typeid = caseVO.getTypeid();
//        String symptom = caseVO.getSymptom();
//        Float sugar = caseVO.getSugar();
//        Integer lowpressure = caseVO.getLowpressure();
//        Integer highpressure = caseVO.getHighpressure();
//        Date attacktime = caseVO.getAttacktime();
        String remark = caseVO.getRemark();
        Boolean result = caseVO.getResult();

        if (Objects.isNull(caseVO.getId())) {
            return Result.error(new CodeMsg("审批结果为空"));
        }
        if (Objects.isNull(caseVO.getId())) {
            return Result.error(new CodeMsg("记录id为空"));
        }
//        if (Objects.isNull(symptom) || symptom.isEmpty())  {
//            return Result.error(new CodeMsg("症状表现为空"));
//        }
        if (Objects.isNull(remark) || remark.isEmpty()) {
            return Result.error(new CodeMsg("审批意见为空"));
        }
//        if (Objects.isNull(patientid))  {
//            return Result.error(new CodeMsg("患者id为空"));
//        }
//        if (Objects.isNull(applyid) ) {
//            return Result.error(new CodeMsg("申报人id为空"));
//        }
        if (Objects.isNull(checkid)) {
            return Result.error(new CodeMsg("审批人id为空"));
        }
//        if (Objects.isNull(sugar) ) {
//            return Result.error(new CodeMsg("血糖值为空"));
//        }
//        if (Objects.isNull(lowpressure) ) {
//            return Result.error(new CodeMsg("舒张压为空"));
//        }
//        if (Objects.isNull(highpressure) ) {
//            return Result.error(new CodeMsg("收缩压为空"));
//        }
//        if (Objects.isNull(attacktime) ) {
//            return Result.error(new CodeMsg("发病时间为空"));
//        }

        diseaseCase.setId(caseVO.getId());
//        diseaseCase.setPatientid(patientid);
//        diseaseCase.setApplyid(applyid);
        diseaseCase.setCheckid(checkid);
//        diseaseCase.setTypeid(typeid);
//        diseaseCase.setSymptom(symptom);
//        diseaseCase.setSugar(sugar);
//        diseaseCase.setLowpressure(lowpressure);
//        diseaseCase.setHighpressure(highpressure);
        diseaseCase.setRemark(remark);
        if (result) {
            diseaseCase.setState(1);
            diseaseCase.setResult(result);
        } else {
            diseaseCase.setState(2);
        }

//        DiseaseCase allCase = new DiseaseCase();
//        allCase.setPatientid(patientid);
//        allCase.setTypeid(typeid);
//        List<DiseaseCase> diseaseCases = diseaseCaseService.queryAllByDiseaseCase(allCase);
//        for (DiseaseCase aCase : diseaseCases) {
//            if(!aCase.getId().equals(caseVO.getId())){
//                return Result.error(new CodeMsg("患者已确诊过该病例"));
//            }
//        }

        try {
            Integer caseId = caseVO.getId();
            if (diseaseCaseService.update(diseaseCase) == 0) {
                transactionManager.rollback(status);
                return Result.error(CodeMsg.UPDATE_FAILURE);
            }

            if (null != files) {
                ArrayList<String> fileNames = new ArrayList<>();
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String originalFilename = file.getOriginalFilename();
                        int pointIndex = originalFilename.indexOf(".");
                        String substring = originalFilename.substring(pointIndex);
                        String fileUUID = UUID.fastUUID().toString() + substring;

                        File file1 = new File(MyFileUtil.uploadRoot, fileUUID);
                        if (!file1.exists()) {
                            file1.mkdirs();
                        }
                        file.transferTo(file1);
                        if (!new File(MyFileUtil.uploadRoot + "\\" + fileUUID).exists()) {
                            fileNames.add(originalFilename);
                        } else {
                            strings.add(MyFileUtil.uploadRoot + "\\" + fileUUID);
                        }

                        Files myFile = new Files();
                        myFile.setModule("病例");
                        myFile.setInfoid(caseId);
                        myFile.setName(originalFilename);
                        myFile.setPath(fileUUID);
                        filesService.insert(myFile);
                    }

                }

                if (fileNames.size() != 0) {
                    for (String string : strings) {
                        new File(string).delete();
                    }
                    transactionManager.rollback(status);
                    return Result.define(-1, "未能成功上传的文件如下", fileNames);
                }
            }

            List<Integer> fileIds = caseVO.getFileIds();
            if (null != fileIds) {
                for (Integer fileId : fileIds) {
                    Files dBFile = filesService.queryById(fileId);
                    if (null != dBFile) {
                        if (null != dBFile.getPath()) {
                            File file = new File(MyFileUtil.uploadRoot + "\\" + dBFile.getPath());
                            if (file.exists()) {
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
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
        } finally {
            for (String string : strings) {
                new File(MyFileUtil.uploadRoot + "\\" + string).delete();
            }
        }

        return Result.success(null);
    }


    @ApiOperation("门诊医生修改后再提交")
    @PostMapping("edit")
    public Result edit(@RequestParam("files") MultipartFile[] files, DiseaseCaseVO caseVO) throws IOException {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def); // get

        ArrayList<String> strings = new ArrayList<>();

        //治疗记录部分
        DiseaseCase diseaseCase = new DiseaseCase();
        diseaseCase.setApplytime(new Date());

        Integer patientid = caseVO.getPatientid();
        Integer applyid = caseVO.getApplyid();
//        Integer checkid = caseVO.getCheckid();
        Integer typeid = caseVO.getTypeid();
        String symptom = caseVO.getSymptom();
        Float sugar = caseVO.getSugar();
        Integer lowpressure = caseVO.getLowpressure();
        Integer highpressure = caseVO.getHighpressure();
        Date attacktime = caseVO.getAttacktime();
//        String remark = caseVO.getRemark();

        if (Objects.isNull(caseVO.getId())) {
            return Result.error(new CodeMsg("记录id为空"));
        }
        if (Objects.isNull(symptom) || symptom.isEmpty()) {
            return Result.error(new CodeMsg("症状表现为空"));
        }
//        if (Objects.isNull(remark) || remark.isEmpty() )  {
//            return Result.error(new CodeMsg("审批意见为空"));
//        }
        if (Objects.isNull(typeid)) {
            return Result.error(new CodeMsg("病种id为空"));
        }
        if (Objects.isNull(patientid)) {
            return Result.error(new CodeMsg("患者id为空"));
        }
        if (Objects.isNull(applyid)) {
            return Result.error(new CodeMsg("申报人id为空"));
        }
//        if (Objects.isNull(checkid) ) {
//            return Result.error(new CodeMsg("审批人id为空"));
//        }
        if (Objects.isNull(sugar)) {
            return Result.error(new CodeMsg("血糖值为空"));
        }
        if (Objects.isNull(lowpressure)) {
            return Result.error(new CodeMsg("舒张压为空"));
        }
        if (Objects.isNull(highpressure)) {
            return Result.error(new CodeMsg("收缩压为空"));
        }
        if (Objects.isNull(attacktime)) {
            return Result.error(new CodeMsg("发病时间为空"));
        }

        diseaseCase.setId(caseVO.getId());
        diseaseCase.setPatientid(patientid);
        diseaseCase.setApplyid(applyid);
//        diseaseCase.setCheckid(checkid);
        diseaseCase.setTypeid(typeid);
        diseaseCase.setSymptom(symptom);
        diseaseCase.setSugar(sugar);
        diseaseCase.setLowpressure(lowpressure);
        diseaseCase.setHighpressure(highpressure);
//        diseaseCase.setRemark(remark);
        diseaseCase.setState(0);
        diseaseCase.setAttacktime(attacktime);

        DiseaseCase allCase = new DiseaseCase();
        allCase.setPatientid(patientid);
        allCase.setTypeid(typeid);
        List<DiseaseCase> diseaseCases = diseaseCaseService.queryAllByDiseaseCase(allCase);
        for (DiseaseCase aCase : diseaseCases) {
            if (!aCase.getId().equals(caseVO.getId())) {
                return Result.error(new CodeMsg("患者已确诊过该病例"));
            }
        }

        try {
            Integer caseId = caseVO.getId();
            if (diseaseCaseService.update(diseaseCase) == 0) {
                transactionManager.rollback(status);
                return Result.error(CodeMsg.UPDATE_FAILURE);
            }

            if (null != files) {
                ArrayList<String> fileNames = new ArrayList<>();
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String originalFilename = file.getOriginalFilename();
                        int pointIndex = originalFilename.indexOf(".");
                        String substring = originalFilename.substring(pointIndex);
                        String fileUUID = UUID.fastUUID().toString() + substring;

                        File file1 = new File(MyFileUtil.uploadRoot, fileUUID);
                        if (!file1.exists()) {
                            file1.mkdirs();
                        }
                        file.transferTo(file1);
                        if (!new File(MyFileUtil.uploadRoot + "\\" + fileUUID).exists()) {
                            fileNames.add(originalFilename);
                        } else {
                            strings.add(MyFileUtil.uploadRoot + "\\" + fileUUID);
                        }

                        Files myFile = new Files();
                        myFile.setModule("病例");
                        myFile.setInfoid(caseId);
                        myFile.setName(originalFilename);
                        myFile.setPath(fileUUID);
                        filesService.insert(myFile);
                    }
                }

                if (fileNames.size() != 0) {
                    for (String string : strings) {
                        new File(string).delete();
                    }
                    transactionManager.rollback(status);
                    return Result.define(-1, "未能成功上传的文件如下", fileNames);
                }
            }

            List<Integer> fileIds = caseVO.getFileIds();
            if (null != fileIds) {
                for (Integer fileId : fileIds) {
                    Files dBFile = filesService.queryById(fileId);
                    if (null != dBFile) {
                        if (null != dBFile.getPath()) {
                            File file = new File(MyFileUtil.uploadRoot + "\\" + dBFile.getPath());
                            if (file.exists()) {
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
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
        } finally {
            for (String string : strings) {
                new File(MyFileUtil.uploadRoot + "\\" + string).delete();
            }
        }

        return Result.success(null);
    }

    @ApiOperation("医生作废病例记录")
    @PostMapping("cancel")
    public Result cancel(@RequestParam Integer id) throws IOException {

        DiseaseCase diseaseCase = new DiseaseCase();
        diseaseCase.setId(id);
        diseaseCase.setState(3);

        return diseaseCaseService.update(diseaseCase) > 0 ? Result.success(null) : Result.error(new CodeMsg("医生作废记录失败"));

    }


    @ApiOperation("根据id删除记录")
    @PostMapping("deleteById")
    public Result deleteById(@RequestParam Integer id) {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def); // get

        DiseaseCase diseaseCase = diseaseCaseService.queryById(id);
        if (null == diseaseCase) {
            return Result.error(CodeMsg.SELECT_RECORD_NULL);
        } else {

            try {
                ArrayList<String> filePaths = new ArrayList<>();
                CureMonitor cureMonitor = new CureMonitor();
                cureMonitor.setCaseid(id);
                List<CureMonitor> cureMonitors = cureMonitorService.queryAllByCureMonitor(cureMonitor);

                for (CureMonitor monitor : cureMonitors) {

                    CureMedicine cureMedicine = new CureMedicine();
                    cureMedicine.setCureid(monitor.getId());
                    List<CureMedicine> cureMedicines = cureMedicineService.queryAllByCureMedicine(cureMedicine);
                    for (CureMedicine medicine : cureMedicines) {
                        if (cureMedicineService.deleteById(medicine.getId()) == 0) {
                            transactionManager.rollback(status);
                            return Result.error(new CodeMsg("删除治疗记录对应的药品用量信息失败"));
                        }
                    }

                    Files files = new Files();
                    files.setModule("治疗");
                    files.setInfoid(monitor.getId());
                    List<Files> files1 = filesService.queryAllByFiles(files);
                    List<String> collect = files1.stream().map(Files::getPath).collect(Collectors.toList());
                    for (Files files2 : files1) {
                        filePaths.add(MyFileUtil.uploadRoot + "\\" + files2.getPath());
                        if (filesService.deleteById(files2.getId()) == 0) {
                            transactionManager.rollback(status);
                            return Result.error(new CodeMsg("删除治疗记录对应的文件信息失败"));
                        }
                    }

                    if (cureMonitorService.deleteById(monitor.getId()) == 0) {
                        transactionManager.rollback(status);
                        return Result.error(new CodeMsg("删除治疗监测信息失败"));
                    }

                }

                Files files = new Files();
                files.setModule("病例");
                files.setInfoid(id);
                List<Files> files1 = filesService.queryAllByFiles(files);
                List<String> collect = files1.stream().map(Files::getPath).collect(Collectors.toList());
                for (Files files2 : files1) {
                    if (filesService.deleteById(files2.getId()) == 0) {
                        transactionManager.rollback(status);
                        return Result.error(new CodeMsg("删除治疗记录对应的文件信息失败"));
                    }
                }

                if (diseaseCaseService.deleteById(id) == 0) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("删除病例信息失败"));
                }

                //删除病例记录文件
                for (String s : collect) {
                    if (!s.isEmpty()) {
                        File file = new File(MyFileUtil.uploadRoot + "\\" + s);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }

                //删除监测记录文件
                for (String s : filePaths) {
                    if (!s.isEmpty()) {
                        File file = new File(MyFileUtil.uploadRoot + "\\" + s);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }

                transactionManager.commit(status);
            } catch (Exception e) {
                transactionManager.rollback(status);
                e.printStackTrace();
            }

            return Result.success(null);
        }
    }
}