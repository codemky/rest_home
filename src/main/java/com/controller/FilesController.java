package com.controller;

import cn.hutool.core.lang.UUID;
import com.entity.vo.DiseaseCureVO;
import com.entity.Files;
import com.service.FilesService;
import com.util.MyFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;
import com.common.Result;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.exception.CodeMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * (Files)表控制层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
@CrossOrigin(origins = "*")
@Api(description = "FilesController模块")
@RestController
@RequestMapping("files")
public class FilesController {
    /**
     * 服务对象
     */
    @Resource
    private FilesService filesService;
    @Autowired
    private DataSourceTransactionManager transactionManager;

//    @Autowired
//    HttpServletRequest request;


    @ApiOperation("批量上传文件")
    @PostMapping(value = "uploadFiles", consumes = "multipart/form-data")
    public Result uploadFiles(@RequestParam("files") MultipartFile[] files, DiseaseCureVO user) throws IOException, ServletException {


        ArrayList<String> strings = new ArrayList<>();
        ArrayList<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            int pointIndex = originalFilename.indexOf(".");
            String substring = originalFilename.substring(pointIndex);
            String filePath = UUID.fastUUID().toString() + substring;

            File file1 = new File(MyFileUtil.uploadRoot, filePath);
            if(!file1.exists()){
                file1.mkdirs();
            }
            file.transferTo(file1);
            if ( !new File(MyFileUtil.uploadRoot+ "\\" +filePath).exists() ) {
                fileNames.add(originalFilename);
            }else{
                strings.add(filePath);
            }

        }

        if(fileNames.size() == 0){
            return Result.success(null);
        }else{
            for (String string : strings) {
                new File(MyFileUtil.uploadRoot + "\\" + string).delete();
            }
            return Result.define(-1,"未能成功上传的文件如下",fileNames);
        }

    }

    @ApiOperation("下载文件")
    @RequestMapping("download")
    public Result download(@RequestParam("path") String path,HttpServletResponse response) throws Exception{
//        fileName = MyFileUtil.uploadRoot + "/" + fileName;
        Files files = new Files();
        files.setPath(path);
        List<Files> files1 = filesService.queryAllByFiles(files);
        if(files1.size() == 0){
            return Result.error(new CodeMsg("该文件不存在"));
        }

        path = MyFileUtil.uploadRoot + "\\" + path;

        InputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));

        String fileName = URLEncoder.encode(files1.get(0).getName(),"UTF-8");
        if( null == fileName || fileName.isEmpty()){
            URLEncoder.encode("未知文件"+path.substring(path.indexOf(".")),"UTF-8");
        }
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("multipart/form-data");

        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        try {
            int len = 0;
            while((len = bis.read()) != -1){
                out.write(len);
                out.flush();
            }
        }finally {
            out.close();
            bis.close();
        }
        return Result.success(null);
    }


    @ApiOperation("获取所有信息")
    @PostMapping("selectAll")
    public Result selectAll() {
        return Result.success(this.filesService.queryAll());
    }

	@ApiOperation("根据id获取详情")
    @PostMapping("selectById")
    public Result selectById(@RequestParam Integer id) {
        return Result.success(this.filesService.queryById(id));
    }

	@ApiOperation("添加记录")
    @PostMapping("add")
    public Result add(@RequestBody Files files){
        return null != filesService.insert(files).getId() ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }

	@ApiOperation("更新记录")
    @PostMapping("update")
    public Result update(@RequestBody Files files){
        return filesService.update(files) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }

	@ApiOperation("根据id删除记录")
    @PostMapping("deleteById")
    public Result deleteById(@RequestParam Integer id){

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def); // get

        Files DbFile = filesService.queryById(id);
        if( null == DbFile){
            transactionManager.rollback(status);
            return Result.error(CodeMsg.CREATE_FAILURE);
        }

        if( filesService.deleteById(DbFile.getId()) == 0){
            transactionManager.rollback(status);
            return Result.error(CodeMsg.DELETE_FAILURE);
        }

        File file = new File(MyFileUtil.uploadRoot + "\\" + DbFile.getPath());
        if( file.exists() ){
            if( !file.delete() ){
                transactionManager.rollback(status);
                return Result.error(new CodeMsg("文件删除失败"));
            }
        }

        transactionManager.commit(status);

        return Result.success(null) ;
    }
}