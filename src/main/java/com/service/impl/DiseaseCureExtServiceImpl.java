package com.service.impl;

import com.dao.DiseaseCureExtDao;
import com.entity.criteria.CureCriteria;
import com.entity.vo.DiseaseCureVO;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (DiseaseCure)表服务实现类
 *
 * @author makejava
 * @since 2020-04-20 11:50:01
 */
@Service("diseaseCureExtService")
public class DiseaseCureExtServiceImpl implements DiseaseCureExtService {
    @Resource
    private DiseaseCureExtDao diseaseCureExtDao;

    @Resource
    private DiseaseCureService diseaseCureService;
    @Resource
    private DiseaseCureExtService diseaseCureExtService;
    @Resource
    private CureMedicineExtService cureMedicineExtService;
    @Resource
    private MedicineService medicineService;
    @Resource
    private FilesService filesService;
    @Autowired
    private DataSourceTransactionManager transactionManager;


    @Override
    public List<DiseaseCureVO> selectByCriteria(CureCriteria cureCriteria) {
        return diseaseCureExtDao.selectByCriteria(cureCriteria);
    }

    @Override
    public DiseaseCureVO selectById(Integer id) {
        return diseaseCureExtDao.selectById(id);
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public int insertRecord(MultipartFile[] files, DiseaseCureVO cureVO) {
//
//
//        return 0;
//    }

//    @Override
//    public int templateOp(MultipartFile[] files, DiseaseCureVO cureVO) {
//
//        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        TransactionStatus status = transactionManager.getTransaction(def); // get
//
//        ArrayList<String> strings = new ArrayList<>();
//
//        Integer cureId = cureVO.getId();
//        if( null == cureId ){
//            return 1;
//        }
//
//        //治疗记录部分
//        DiseaseCure diseaseCure = new DiseaseCure();
//        diseaseCure.setUpdatetime(new Date());
//
//        Integer patientid = cureVO.getPatientid();
//        Integer doctorid = cureVO.getDoctorid();
//        String content = cureVO.getContent();
//        String result = cureVO.getResult();
//        String remark = cureVO.getRemark();
//        if (Objects.isNull(patientid))  {
//            return Result.error(new CodeMsg("医生id为空"));
//        }
//        if (Objects.isNull(doctorid) ) {
//            return Result.error(new CodeMsg("患者id为空"));
//        }
//        if (Objects.isNull(content) || content.isEmpty()) {
//            return Result.error(new CodeMsg("治疗内容为空"));
//        }
//        if (Objects.isNull(result) || result.isEmpty()) {
//            return Result.error(new CodeMsg("治疗结果为空"));
//        }
//        if (Objects.isNull(remark) || remark.isEmpty()) {
//            return Result.error(new CodeMsg("结果评价为空"));
//        }
//        diseaseCure.setPatientid(patientid);
//        diseaseCure.setDoctorid(doctorid);
//        diseaseCure.setContent(content);
//        diseaseCure.setResult(result);
//        diseaseCure.setRemark(remark);
//        diseaseCure.setState(true);
//
//        diseaseCure.setCaseid(cureVO.getCaseid());
//
//        try {
//            if (diseaseCureService.update(diseaseCure) == 0) {
//                transactionManager.rollback(status);
//                return Result.error(CodeMsg.UPDATE_FAILURE);
//            }
//
//            List<Integer> medicineList = cureVO.getMedicineList();
//            List<Integer> numberList = cureVO.getNumberList();
//            List<Integer> medicineIds = medicineService.queryAll().stream().map(Medicine::getId).collect(Collectors.toList());
//            //判断是否有该药品
//            for (Integer integer : medicineList) {
//                if(!medicineIds.contains(integer)){
//                    transactionManager.rollback(status);
//                    return Result.error(new CodeMsg("id为"+integer+"的药品不存在"));
//                }
//            }
//
//            //判断药品中是否有重复
//            for(int i=0;i<medicineList.size();i++){
//                for(int j=0;j<medicineList.size();j++){
//                    if(i != j && medicineList.get(i).equals(medicineList.get(j))){
//                        transactionManager.rollback(status);
//                        return Result.error(new CodeMsg("第" + (i+1) + "行的药品和" + (j+1) +"行的药品重复添加"));
//                    }
//                }
//            }
//
//            if(null != cureVO.getMedicineList() && null != cureVO.getNumberList()){
//                int checkResult = cureVO.checkMedicines();
//                if( checkResult == 1){
//                    transactionManager.rollback(status);
//                    return Result.error(new CodeMsg("药品id为空"));
//                }
//                if( checkResult == 2){
//                    transactionManager.rollback(status);
//                    return Result.error(new CodeMsg("药品数量为空"));
//                }
//                if( checkResult == 3){
//                    transactionManager.rollback(status);
//                    return Result.error(new CodeMsg("药品数量和药品用量的记录数没有一致"));
//                }
//
//                ArrayList<CureMedicine> cureMedicines = new ArrayList<>();
//                for(int i=0 ;i<medicineList.size() ;i++ ){
//                    CureMedicine cureMedicine = new CureMedicine();
//                    cureMedicine.setCureid(cureId);
//                    cureMedicine.setMedicineid(medicineList.get(i));
//                    cureMedicine.setNumber(numberList.get(i));
//                    cureMedicines.add(cureMedicine);
//                }
//
//                int importNumber = cureMedicineExtService.insertList(cureMedicines);
//                if(importNumber != cureMedicines.size()){
//                    transactionManager.rollback(status);
//                    return Result.error(new CodeMsg("药品和用量信息没有全部插入"));
//                }
//            }else{
//                if( null != cureVO.getMedicineList() && null == cureVO.getNumberList() ){
//                    transactionManager.rollback(status);
//                    return Result.error(new CodeMsg("药品用量为空而id不为空"));
//                }
//                if( null == cureVO.getMedicineList() && null != cureVO.getNumberList() ){
//                    transactionManager.rollback(status);
//                    return Result.error(new CodeMsg("药品id为空而用量不为空"));
//                }
//            }
//
//
//            if( null != files){
//                ArrayList<String> fileNames = new ArrayList<>();
//                for (MultipartFile file : files) {
//                    String originalFilename = file.getOriginalFilename();
//                    int pointIndex = originalFilename.indexOf(".");
//                    String substring = originalFilename.substring(pointIndex);
//                    String fileUUID = UUID.fastUUID().toString() + substring;
//
//                    File file1 = new File(MyFileUtil.uploadRoot, fileUUID);
//                    if(!file1.exists()){
//                        file1.mkdirs();
//                    }
//                    file.transferTo(file1);
//                    if (!new File(MyFileUtil.uploadRoot+ "\\" +fileUUID).exists()) {
//                        fileNames.add(originalFilename);
//                    }else{
//                        strings.add(fileUUID);
//                    }
//
//                    Files myFile = new Files();
//                    myFile.setModule("治疗");
//                    myFile.setInfoid(cureId);
//                    myFile.setName(originalFilename);
//                    myFile.setPath(fileUUID);
//                    filesService.insert(myFile);
//
//                }
//
//                if(strings.size() != 0){
//                    transactionManager.rollback(status);
//                    return Result.define(-1,"未能成功上传的文件如下",fileNames);
//                }
//            }
//            transactionManager.commit(status);
//        }catch (Exception e){
//            transactionManager.rollback(status);
//            e.printStackTrace();
//        }finally {
//            for (String string : strings) {
//                new File(MyFileUtil.uploadRoot + "\\" + string).delete();
//            }
//        }
//
//        return Result.success(null);
//    }
//
//
//        return 0;
//    }

}