package com.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.check.CheckUser;
import com.dao.UserDao;
import com.entity.User;
import com.entity.dto.UserDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.service.UserExtService;
import com.service.UserService;
import com.util.IDCardValidator;
import com.util.MyFileUtil;
import com.util.MyMD5;
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

import static com.util.MyMD5.MD5;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2020-04-19 00:14:32
 */
@CrossOrigin(origins = "*")
@Api(description = "UserController模块")
@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserExtService userExtService;
    @Resource
    private UserDao userDao;

    @ApiOperation("用户登录")
    @PostMapping("login")
    public Result login(@RequestBody User user) {
        //校验用户名、密码和角色分类
        int checkResult = CheckUser.checkLogin(user);
        if (checkResult == 1) {
            return Result.error(CodeMsg.LOGIN_ACCOUNT_NULL);
        }
        if (checkResult == 2) {
            return Result.error(CodeMsg.LOGIN_PASSWORD_NULL);
        }
        if (checkResult == 3) {
            return Result.error(CodeMsg.LOGIN_ROLE_NULL);
        }
        if (checkResult == 4) {
            return Result.error(CodeMsg.LOGIN_ROLE_ERROR);
        }

        User criteriaUser = new User();
        criteriaUser.setUsername(user.getUsername());
        criteriaUser.setRole(user.getRole());
        //校验用户名是否存在
        List<User> users = userService.queryAllByUser(criteriaUser);
        if (users.size() == 0) {
            return Result.error(CodeMsg.LOGIN_ACCOUNT_ERROR);
        }

        //输入的密码明文通过加盐Hash计算后 是否与原数据库中的Hash值相同
        String password = user.getPassword();
        User dUser = users.get(0);
        String hashWithSalt = dUser.getPassword();
        String saltHash = MyMD5.getHashFromSaltHash(hashWithSalt);
        String salt = MyMD5.getSaltFromHash(hashWithSalt);
        if (!MD5(password + salt).equals(saltHash)) {
            return Result.error(CodeMsg.LOGIN_PASSWORD_ERROR);
        }

        return Result.success(dUser);
    }

    @ApiOperation("根据用户信息搜索用户信息（分页）")
    @PostMapping("selectByUser")
    public Result selectByUser(@RequestBody(required = false) User user) {
        user.setDefaultPage();
        PageHelper.startPage(user.getPage(), user.getPageSize());
        List<User> users = userService.queryAllByUser(user);
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return Result.success(userPageInfo);
    }

    @ApiOperation("获取所有患者用户")
    @PostMapping("selectAllPatients")
    public Result selectAllPatients() {
        User user = new User();
        user.setRole(1);
        return Result.success(this.userService.queryAllByUser(user));
    }

    @ApiOperation("获取所有门诊医生")
    @PostMapping("selectAllDoorDoc")
    public Result selectAllDoorDoc() {
        User user = new User();
        user.setRole(2);
        return Result.success(this.userService.queryAllByUser(user));
    }

    @ApiOperation("获取所有慢病科医生")
    @PostMapping("selectAllDiseaDoc")
    public Result selectAllDiseaDoc() {
        User user = new User();
        user.setRole(3);
        return Result.success(this.userService.queryAllByUser(user));
    }

    @ApiOperation("根据id获取详情")
    @PostMapping("selectById")
    public Result selectById(@RequestParam Integer id) {
        return Result.success(this.userService.queryById(id));
    }

    @ApiOperation("判断用户名是否重复")
    @PostMapping("checkUsername")
    public Result checkUsername(@RequestParam String username) {

        User user = new User();
        user.setTempname(username);
        List<User> users = userDao.checkUsername(user);
        if(users.size() > 0){
            return Result.success(null);
        }

        return Result.error(null);
    }


    @ApiOperation("添加记录")
    @PostMapping("add")
    public Result add(@RequestBody User user) {
        //校验用户名、密码和角色分类
        int checkResult = CheckUser.checkLogin(user);
        if (checkResult == 1) {
            return Result.error(CodeMsg.LOGIN_ACCOUNT_NULL);
        }
//        if (checkResult == 2) {
//            return Result.error(CodeMsg.LOGIN_PASSWORD_NULL);
//        }
        if (checkResult == 3) {
            return Result.error(CodeMsg.LOGIN_ROLE_NULL);
        }
        if (checkResult == 4) {
            return Result.error(CodeMsg.LOGIN_ROLE_ERROR);
        }
        user.setPassword("123123");

        User criteriaUser = new User();
        criteriaUser.setUsername(user.getUsername());
        //校验用户名是否已存在
        List<User> users = userDao.checkUsername(criteriaUser);
        if (users.size() == 0) {
            return Result.error(CodeMsg.ACCOUNT_REPEAT);
        }

        //把随机生成的带盐密文进行存储
        user.setPassword(MyMD5.MD5WithSalt(user.getPassword()));

        return null != userService.insert(user) ?
                Result.success(null) : Result.error(CodeMsg.CREATE_FAILURE);
    }

    @ApiOperation("更新记录")
    @PostMapping("update")
    public Result update(@RequestBody User user) {
        if (Objects.isNull(user.getId())) {
            return Result.error(CodeMsg.UPDATE_ID_NULL);
        }
        //校验用户名、密码和角色分类
        int checkResult = CheckUser.checkLogin(user);
        if (checkResult == 1) {
            return Result.error(CodeMsg.LOGIN_ACCOUNT_NULL);
        }
//        if (checkResult == 2) {
//            return Result.error(CodeMsg.LOGIN_PASSWORD_NULL);
//        }
        if (checkResult == 3) {
            return Result.error(CodeMsg.LOGIN_ROLE_NULL);
        }
        if (checkResult == 4) {
            return Result.error(CodeMsg.LOGIN_ROLE_ERROR);
        }

        User criteriaUser = new User();
        criteriaUser.setUsername(user.getUsername());
        //校验用户名是否已存在
        List<User> users = userDao.checkUsername(criteriaUser);
        if (users.size() == 0) {
            return Result.error(CodeMsg.ACCOUNT_REPEAT);
        }

//        //把随机生成的带盐密文进行存储
//        user.setPassword(MyMD5.MD5WithSalt(user.getPassword()));

        return userService.update(user) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }

    @ApiOperation("验证原密码更新密码")
    @PostMapping("updatePwd")
    public Result updatePwd(@RequestBody User user) {

        if (Objects.isNull(user.getId())) {
            return Result.error(CodeMsg.UPDATE_ID_NULL);
        }
        //校验用户名、密码和角色分类
        int checkResult = CheckUser.checkLogin(user);
        if (checkResult == 1) {
            return Result.error(CodeMsg.LOGIN_ACCOUNT_NULL);
        }
        if( null == user.getNewPwd()){
            return Result.error(new CodeMsg("新密码为空"));
        }

        User user1 = userService.queryById(user.getId());
        if( null == user1){
            return Result.error(CodeMsg.SELECT_RECORD_NULL);
        }

        String password = user1.getPassword();
        String salt = MyMD5.getHashFromSaltHash(password);
        String saltHash = MyMD5.getSaltFromHash(password);

        if(  !MyMD5.MD5(user.getPassword()+salt ).equals(salt) ){
            return Result.error(new CodeMsg("原密码不正确"));
        }else{
            user1.setPassword(MyMD5.MD5WithSalt(user.getNewPwd()));
        }

        return userService.update(user1) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }

    @ApiOperation("重置密码为123123")
    @PostMapping("resetPwd")
    public Result resetPwd(@RequestParam Integer id) {


        User user = userService.queryById(id);

        user.setPassword("123123");

        //把随机生成的带盐密文进行存储
        user.setPassword(MyMD5.MD5WithSalt(user.getPassword()));

        return userService.update(user) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }



    @ApiOperation(value = "将用户信息的Excel文件导入数据库")
    @PostMapping("import")
    public Result importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        if (null == file) {
            return Result.error(new CodeMsg("文件读取错误，无效的文件!"));
        }

        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<User> excelList = reader.read(1, 2, User.class);

        if (null == excelList || excelList.size() == 0) {
            return Result.error(new CodeMsg("文件数据为空!"));
        }

        List<String> usernameList = userService.queryAllByUser(null)
                .stream().map(User::getUsername).collect(Collectors.toList());

        //判断数据合法性
        int size = excelList.size();
        for (int i = 0; i < size; i++) {
            int j = 0;
            User user = excelList.get(i);
            String username = user.getUsername();
            if (Objects.isNull(username) || username.isEmpty()) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 1 + "列出错" + " 出错原因:账号为空"));
            } else if (usernameList.contains(username)) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 1 + "列出错" + " 出错原因:账号已存在"));
            }
            //更新已有的账号列表
            usernameList.add(username);

            String password = user.getPassword();
            if (Objects.isNull(password) || password.isEmpty()) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 2 + "列出错" + " 出错原因:密码为空"));
            }
            user.setPassword(MyMD5.MD5WithSalt(user.getPassword()));
            excelList.set(i, user);

            String name = user.getName();
            if (Objects.isNull(name) || name.isEmpty()) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 3 + "列出错" + " 出错原因:姓名为空"));
            }

            Integer age = user.getAge();
            if (Objects.isNull(age)) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 4 + "列出错" + " 出错原因:年龄为空"));
            }

            String sexy = user.getSexy();
            if (Objects.isNull(sexy) || sexy.isEmpty()) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 5 + "列出错" + " 出错原因:性别为空"));
            } else if (!"男".equals(sexy) && !"女".equals(sexy)) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 5 + "列出错" + " 出错原因:性别不合法"));
            }

            //验证身份证号
            String idcard = user.getIdcard();
            if (Objects.isNull(idcard) || !IDCardValidator.isValidatedIdcard(idcard)) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 6 + "列出错" + " 出错原因:身份证为空或不合法"));
            }

            //验证手机号
            String phone = user.getPhone();
            if (Objects.isNull(phone)) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 7 + "列出错" + " 出错原因:手机号码为空"));
            }
            String phoneRegex = "^1[3|4|5|8][0-9]\\d{8}$";
            boolean phoneIsMatche = phone.matches(phoneRegex);
            if (!phoneIsMatche) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 7 + "列出错" + " 出错原因:手机号码不合法"));
            }

            String addr = user.getAddr();
            if (Objects.isNull(addr) || addr.isEmpty()) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 8 + "列出错" + " 出错原因:地址为空"));
            } else if (addr.length() > 50) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 8 + "列出错" + " 出错原因:地址最大程度为50字符"));
            }

            Integer role = user.getRole();
            if (null == role) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 9 + "列出错" + " 出错原因:角色分类为空"));
            } else if (role != 0 && role != 1 && role != 2 && role != 3) {
                return Result.error(new CodeMsg("第" + (i + 3) + "行 第" + j + 9 + "列出错" + " 出错原因:角色分类不合法"));
            }
        }
        //批量插入数据库
        userExtService.insertList(excelList);
        return Result.success(CodeMsg.SUCCESS);
    }

    @ApiOperation("下载导入模板")
    @RequestMapping("export")
    public void export(HttpServletResponse response) throws Exception {

        List<User> users = userService.queryAllByUser(null);
        ArrayList<UserDTO> rows = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user,userDTO);
            rows.add(userDTO);
        }

        String fileName = "用户信息导出" + DateUtil.now() + ".xlsx";
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
        String fileName = "用户导入模板.xlsx";
        String path = MyFileUtil.uploadRoot + "\\用户导入模板.xlsx";
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
    public Result deleteById(@RequestParam Integer id) {
        return userService.deleteById(id) > 0 ?
                Result.success(null) : Result.error(CodeMsg.ERROR);
    }

}