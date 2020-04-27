package com.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.entity.*;
import com.entity.criteria.CureCriteria;
import com.entity.criteria.MonitorCriteria;
import com.entity.vo.CureMonitorVO;
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
 * (CureMonitor)表控制层
 *
 * @author makejava
 * @since 2020-04-23 17:36:38
 */
@CrossOrigin(origins = "*")
@Api(description = "CureMonitorController模块")
@RestController
@RequestMapping("cureMonitor")
public class CureMonitorController {
    /**
     * 服务对象
     */
    @Resource
    private CureMonitorService cureMonitorService;
    @Resource
    private CureMonitorExtService cureMonitorExtService;
    @Resource
    private DiseaseCaseService diseaseCaseService;
    @Resource
    private DiseaseCaseExtService diseaseCaseExtService;
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


    @ApiOperation("根据条件查询治疗监测记录列表")
    @PostMapping("selectByCriteria")
    public Result selectByCriteria(@RequestBody MonitorCriteria criteria) {
        criteria.setDefaultValue();
        PageHelper.startPage(criteria.getPage(), criteria.getPageSize());
        List<CureMonitorVO> monitorVOList = cureMonitorExtService.selectByCriteria(criteria);
        PageInfo<CureMonitorVO> monitorInfo = new PageInfo<>(monitorVOList);

        return Result.success(monitorInfo);
    }

    @ApiOperation("获取所有符合提醒时间并且医生为自己的治疗监测记录")
    @PostMapping("selectAllToReport")
    public Result selectAllToReport(@RequestBody MonitorCriteria criteria) {
        if(null == criteria.getDoctorid()){
            return Result.error(new CodeMsg("医生id为空"));
        }
        criteria.setDefaultValue();
        criteria.setBeginremind(DateUtil.date(0));
        criteria.setEndremind(new Date());
        criteria.setState(false);

        PageHelper.startPage(criteria.getPage(), criteria.getPageSize());
        List<CureMonitorVO> monitorVOList = cureMonitorExtService.selectByCriteria(criteria);
        PageInfo<CureMonitorVO> monitorInfo = new PageInfo<>(monitorVOList);

        return Result.success(monitorInfo);
    }

    @ApiOperation("获取所有信息")
    @PostMapping("selectAll")
    public Result selectAll() {
        return Result.success(this.cureMonitorService.queryAll());
    }

    @ApiOperation("根据id获取详情")
    @PostMapping("selectById")
    public Result selectById(@RequestParam("id") Integer id) {
        return Result.success(this.cureMonitorService.queryById(id));
    }

    @ApiOperation("添加记录（带多文件）")
    @PostMapping(value = "add", consumes = "multipart/form-data")
//    @PostMapping("add")
    public Result add(@RequestParam("files") MultipartFile[] files, CureMonitorVO cureVO) throws IOException {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def); // get

        ArrayList<String> strings = new ArrayList<>();

        //治疗记录部分
        CureMonitor cureMonitor = new CureMonitor();
        cureMonitor.setNum(Long.toString(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
        cureMonitor.setCreatetime(new Date());
        cureMonitor.setState(false);

        Integer patientid = cureVO.getPatientid();
        Integer doctorid = cureVO.getDoctorid();
        Integer caseid = cureVO.getCaseid();
        String curecontent = cureVO.getCurecontent();
        String monitorcontent = cureVO.getMonitorcontent();
        Float sugar = cureVO.getSugar();
        Integer lowpressure = cureVO.getLowpressure();
        Integer highpressure = cureVO.getHighpressure();
        Date remindtime = cureVO.getRemindtime();

        if (Objects.isNull(patientid)) {
            return Result.error(new CodeMsg("医生id为空"));
        }
        if (Objects.isNull(doctorid)) {
            return Result.error(new CodeMsg("患者id为空"));
        }
        if (Objects.isNull(caseid)) {
            return Result.error(new CodeMsg("病例id为空"));
        }
        if (Objects.isNull(curecontent) || curecontent.isEmpty()) {
            return Result.error(new CodeMsg("治疗内容为空"));
        }
        if (Objects.isNull(monitorcontent) || monitorcontent.isEmpty()) {
            return Result.error(new CodeMsg("其他监测内容为空"));
        }
        if (Objects.isNull(remindtime)) {
            return Result.error(new CodeMsg("提醒时间为空"));
        }

        cureMonitor.setPatientid(patientid);
        cureMonitor.setDoctorid(doctorid);
        cureMonitor.setCaseid(caseid);
        cureMonitor.setCurecontent(curecontent);
        cureMonitor.setMonitorcontent(monitorcontent);
        cureMonitor.setSugar(sugar);
        cureMonitor.setLowpressure(lowpressure);
        cureMonitor.setHighpressure(highpressure);
        cureMonitor.setRemindtime(remindtime);

        try {

            DiseaseCase diseaseCase = diseaseCaseService.queryById(caseid);
            if (null == diseaseCase) {
                return Result.error(new CodeMsg("病例ID对应的记录为空"));
            } else if(diseaseCase.getState() != 4 && diseaseCase.getState() == 2) {
                diseaseCase.setState(4);
                if (diseaseCaseService.update(diseaseCase) == 0) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("更新病例状态为已监测的操作失败"));
                }
            }

            Integer cureId = cureMonitorService.insert(cureMonitor).getId();
            if (null == cureId) {
                transactionManager.rollback(status);
                return Result.error(CodeMsg.CREATE_FAILURE);
            }

            List<Integer> medicineList = cureVO.getMedicineList();
            List<Integer> numberList = cureVO.getNumberList();
            List<Integer> medicineIds = medicineService.queryAll().stream().map(Medicine::getId).collect(Collectors.toList());
            //判断是否有该药品
            for (Integer integer : medicineList) {
                if (!medicineIds.contains(integer)) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("id为" + integer + "的药品不存在"));
                }
            }

            //判断药品中是否有重复
            for (int i = 0; i < medicineList.size(); i++) {
                for (int j = 0; j < medicineList.size(); j++) {
                    if (i != j && medicineList.get(i).equals(medicineList.get(j))) {
                        transactionManager.rollback(status);
                        return Result.error(new CodeMsg("第" + (i + 1) + "行的药品和" + (j + 1) + "行的药品重复添加"));
                    }
                }
            }

            if (null != cureVO.getMedicineList() && null != cureVO.getNumberList()) {
                int checkResult = cureVO.checkMedicines();
                if (checkResult == 1) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品id为空"));
                }
                if (checkResult == 2) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品数量为空"));
                }
                if (checkResult == 3) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品数量和药品用量的记录数没有一致"));
                }

                ArrayList<CureMedicine> cureMedicines = new ArrayList<>();
                for (int i = 0; i < medicineList.size(); i++) {
                    CureMedicine cureMedicine = new CureMedicine();
                    cureMedicine.setCureid(cureId);
                    cureMedicine.setMedicineid(medicineList.get(i));
                    cureMedicine.setNumber(numberList.get(i));
                    cureMedicines.add(cureMedicine);
                }

                int importNumber = cureMedicineExtService.insertList(cureMedicines);
                if (importNumber != cureMedicines.size()) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品和用量信息没有全部插入"));
                }
            } else {
                if (null != cureVO.getMedicineList() && null == cureVO.getNumberList()) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品用量为空而id不为空"));
                }
                if (null == cureVO.getMedicineList() && null != cureVO.getNumberList()) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品id为空而用量不为空"));
                }
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
                        myFile.setModule("治疗");
                        myFile.setInfoid(cureId);
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
    public Result update(@RequestParam("files") MultipartFile[] files, CureMonitorVO cureVO) throws IOException {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def); // get

        ArrayList<String> strings = new ArrayList<>();

        //治疗记录部分
        CureMonitor cureMonitor = new CureMonitor();
        cureMonitor.setUpdatetime(new Date());

        Integer patientid = cureVO.getPatientid();
        Integer doctorid = cureVO.getDoctorid();
        Integer caseid = cureVO.getCaseid();
        String curecontent = cureVO.getCurecontent();
        String monitorcontent = cureVO.getMonitorcontent();
        Float sugar = cureVO.getSugar();
        Integer lowpressure = cureVO.getLowpressure();
        Integer highpressure = cureVO.getHighpressure();
        Date remindtime = cureVO.getRemindtime();
        String cureresult = cureVO.getCureresult();
        String monitorresult = cureVO.getMonitorresult();
        String remark = cureVO.getRemark();

        if (Objects.isNull(cureVO.getId())) {
            return Result.error(new CodeMsg("记录id为空"));
        }
        if (Objects.isNull(patientid)) {
            return Result.error(new CodeMsg("医生id为空"));
        }
        if (Objects.isNull(doctorid)) {
            return Result.error(new CodeMsg("患者id为空"));
        }
        if (Objects.isNull(caseid)) {
            return Result.error(new CodeMsg("病例id为空"));
        }
        if (Objects.isNull(remindtime)) {
            return Result.error(new CodeMsg("提醒时间为空"));
        }

        cureMonitor.setId(cureVO.getId());
        cureMonitor.setPatientid(patientid);
        cureMonitor.setDoctorid(doctorid);
        cureMonitor.setCaseid(caseid);
        cureMonitor.setCurecontent(curecontent);
        cureMonitor.setMonitorcontent(monitorcontent);
        cureMonitor.setSugar(sugar);
        cureMonitor.setLowpressure(lowpressure);
        cureMonitor.setHighpressure(highpressure);
        cureMonitor.setRemindtime(remindtime);
        cureMonitor.setMonitorresult(monitorresult);
        cureMonitor.setCureresult(cureresult);
        cureMonitor.setRemark(remark);

        try {
            Integer cureId = cureVO.getId();

            if (cureMonitorService.update(cureMonitor) == 0) {
                transactionManager.rollback(status);
                return Result.error(CodeMsg.UPDATE_FAILURE);
            }

            //删除原本的用药信息
            CureMedicine oldMedicine = new CureMedicine();
            oldMedicine.setCureid(cureId);
            List<Integer> cureMedicineIds = cureMedicineService.queryAllByCureMedicine(oldMedicine)
                    .stream().map(CureMedicine::getId).collect(Collectors.toList());
            if (cureMedicineIds.size() > 0) {
                if (cureMedicineExtService.deleteList(cureMedicineIds) != cureMedicineIds.size()) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("删除原本的用药信息失败"));
                }
            }

            List<Integer> medicineList = cureVO.getMedicineList();
            List<Integer> numberList = cureVO.getNumberList();
            List<Integer> medicineIds = medicineService.queryAll().stream().map(Medicine::getId).collect(Collectors.toList());
            if( null != medicineList ){
                //判断是否有该药品
                for (Integer integer : medicineList) {
                    if (!medicineIds.contains(integer)) {
                        transactionManager.rollback(status);
                        return Result.error(new CodeMsg("id为" + integer + "的药品不存在"));
                    }
                }

                //判断药品中是否有重复
                for (int i = 0; i < medicineList.size(); i++) {
                    for (int j = 0; j < medicineList.size(); j++) {
                        if (i != j && medicineList.get(i).equals(medicineList.get(j))) {
                            transactionManager.rollback(status);
                            return Result.error(new CodeMsg("第" + (i + 1) + "行的药品和" + (j + 1) + "行的药品重复添加"));
                        }
                    }
                }
            }

            if (null != cureVO.getMedicineList() && null != cureVO.getNumberList()) {
                int checkResult = cureVO.checkMedicines();
                if (checkResult == 1) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品id为空"));
                }
                if (checkResult == 2) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品数量为空"));
                }
                if (checkResult == 3) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品数量和药品用量的记录数没有一致"));
                }

                ArrayList<CureMedicine> cureMedicines = new ArrayList<>();
                for (int i = 0; i < medicineList.size(); i++) {
                    CureMedicine cureMedicine = new CureMedicine();
                    cureMedicine.setCureid(cureId);
                    cureMedicine.setMedicineid(medicineList.get(i));
                    cureMedicine.setNumber(numberList.get(i));
                    cureMedicines.add(cureMedicine);
                }

                int importNumber = cureMedicineExtService.insertList(cureMedicines);
                if (importNumber != cureMedicines.size()) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品和用量信息没有全部插入"));
                }
            } else {
                if (null != cureVO.getMedicineList() && null == cureVO.getNumberList()) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品用量为空而id不为空"));
                }
                if (null == cureVO.getMedicineList() && null != cureVO.getNumberList()) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("药品id为空而用量不为空"));
                }
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
                        myFile.setModule("治疗");
                        myFile.setInfoid(cureId);
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

            List<Integer> fileIds = cureVO.getFileIds();
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

    @ApiOperation("治疗监测报告")
    @PostMapping(value = "report", consumes = "multipart/form-data")
    public Result report(@RequestParam("files") MultipartFile[] files, CureMonitorVO cureVO) throws IOException {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def); // get

        ArrayList<String> strings = new ArrayList<>();

        //治疗记录部分
        CureMonitor cureMonitor = new CureMonitor();
        cureMonitor.setUpdatetime(new Date());
        cureMonitor.setState(true);

//        Integer patientid = cureVO.getPatientid();
//        Integer doctorid = cureVO.getDoctorid();
//        Integer caseid = cureVO.getCaseid();
//        String curecontent = cureVO.getCurecontent();
//        String monitorcontent = cureVO.getMonitorcontent();
        Float sugar = cureVO.getSugar();
        Integer lowpressure = cureVO.getLowpressure();
        Integer highpressure = cureVO.getHighpressure();
//        Date remindtime = cureVO.getRemindtime();
        String cureresult = cureVO.getCureresult();
        String monitorresult = cureVO.getMonitorresult();
        String remark = cureVO.getRemark();

        if (Objects.isNull(cureVO.getId())) {
            return Result.error(new CodeMsg("记录id为空"));
        }
//        if (Objects.isNull(patientid))  {
//            return Result.error(new CodeMsg("医生id为空"));
//        }
//        if (Objects.isNull(doctorid) ) {
//            return Result.error(new CodeMsg("患者id为空"));
//        }
//        if (Objects.isNull(caseid) ) {
//            return Result.error(new CodeMsg("病例id为空"));
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
        if (Objects.isNull(monitorresult) || monitorresult.isEmpty()) {
            return Result.error(new CodeMsg("监测结果为空"));
        }
        if (Objects.isNull(cureresult) || cureresult.isEmpty()) {
            return Result.error(new CodeMsg("治疗结果为空"));
        }
        if (Objects.isNull(remark) || remark.isEmpty()) {
            return Result.error(new CodeMsg("评价或备注为空"));
        }

        cureMonitor.setId(cureVO.getId());
//        cureMonitor.setPatientid(patientid);
//        cureMonitor.setDoctorid(doctorid);
//        cureMonitor.setCaseid(caseid);
//        cureMonitor.setCurecontent(curecontent);
//        cureMonitor.setMonitorcontent(monitorcontent);
        cureMonitor.setSugar(sugar);
        cureMonitor.setLowpressure(lowpressure);
        cureMonitor.setHighpressure(highpressure);
//        cureMonitor.setRemindtime(remindtime);
        cureMonitor.setMonitorresult(monitorresult);
        cureMonitor.setCureresult(cureresult);
        cureMonitor.setRemark(remark);


        try {
            Integer cureId = cureVO.getId();
            if (cureMonitorService.update(cureMonitor) == 0) {
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
                        myFile.setModule("治疗");
                        myFile.setInfoid(cureId);
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

            List<Integer> fileIds = cureVO.getFileIds();
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

    @ApiOperation("根据id删除记录")
    @PostMapping("deleteById")
    public Result deleteById(@RequestParam("id") Integer id) {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def); // get


        CureMonitor cureMonitor = cureMonitorService.queryById(id);
        if (null == cureMonitor) {
            return Result.error(CodeMsg.SELECT_RECORD_NULL);
        } else {
            try {
                if (null != cureMonitor.getCaseid()) {
                    DiseaseCase diseaseCase = diseaseCaseService.queryById(cureMonitor.getCaseid());
                    if (null != diseaseCase) {
                        CureMonitor allmonitor = new CureMonitor();
                        allmonitor.setCaseid(cureMonitor.getCaseid());
                        if (cureMonitorService.queryAllByCureMonitor(allmonitor).size() == 1) {
                            diseaseCase.setState(2);  //设置回已通过（未监测）状态
                            if (diseaseCaseService.update(diseaseCase) == 0) {
                                transactionManager.rollback(status);
                                return Result.error(new CodeMsg("更新病例状态为已通过的操作失败"));
                            }
                        }
                    }
                }

                CureMedicine cureMedicine = new CureMedicine();
                cureMedicine.setCureid(id);
                List<CureMedicine> cureMedicines = cureMedicineService.queryAllByCureMedicine(cureMedicine);
                for (CureMedicine medicine : cureMedicines) {
                    if (cureMedicineService.deleteById(medicine.getId()) == 0) {
                        transactionManager.rollback(status);
                        return Result.error(new CodeMsg("删除治疗记录对应的药品用量信息失败"));
                    }
                }

                Files files = new Files();
                files.setModule("治疗");
                files.setInfoid(id);
                List<Files> files1 = filesService.queryAllByFiles(files);
                List<String> collect = files1.stream().map(Files::getPath).collect(Collectors.toList());
                for (Files files2 : files1) {
                    if (filesService.deleteById(files2.getId()) == 0) {
                        transactionManager.rollback(status);
                        return Result.error(new CodeMsg("删除治疗记录对应的文件信息失败"));
                    }
                }

                if (cureMonitorService.deleteById(id) == 0) {
                    transactionManager.rollback(status);
                    return Result.error(new CodeMsg("删除治疗监测信息失败"));
                }

                for (String s : collect) {
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