package com.exception;


import lombok.Getter;

@Getter
public class CodeMsg {

    private int code;
    private String msg;

    //通用
    public static CodeMsg SUCCESS = new CodeMsg(233, "success");
    public static CodeMsg ERROR = new CodeMsg(1, "error");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500101, "非法请求");
    public static CodeMsg JSON_PARSE_ERROR = new CodeMsg(500102, "JSON 解析异常");
    public static CodeMsg REQUEST_FAILURE = new CodeMsg(500103,"服务器处理请求失败");
    public static CodeMsg UPDATE_FAILURE = new CodeMsg(500104,"更新记录失败");
    public static CodeMsg CREATE_FAILURE = new CodeMsg(500105,"新增记录失败");
    public static CodeMsg DELETE_FAILURE = new CodeMsg(500106,"删除记录失败");
    public static CodeMsg QUERY_FAILURE = new CodeMsg(500107,"查询记录失败");
    public static CodeMsg PARAM_ERROR = new CodeMsg(500108,"请求参数错误");
    public static CodeMsg UPDATE_ID_NULL = new CodeMsg(500108,"需更新的记录ID为空");
    public static CodeMsg SELECT_RECORD_NULL = new CodeMsg(500108,"根据ID查询的记录为空");


    //用户模块 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "session不存在或session失效");
    public static CodeMsg LOGIN_ACCOUNT_ERROR = new CodeMsg(500211, "此类角色的用户名不存在");
    public static CodeMsg LOGIN_PASSWORD_ERROR = new CodeMsg(500212, "密码错误");
    public static CodeMsg LOGIN_ACCOUNT_NULL = new CodeMsg(500213,"输入的用户名为空");
    public static CodeMsg LOGIN_PASSWORD_NULL = new CodeMsg(500214,"输入的密码为空");
    public static CodeMsg LOGIN_ROLE_ERROR = new CodeMsg(500215,"输入的角色分类不合法");
    public static CodeMsg LOGIN_ROLE_NULL = new CodeMsg(500216,"输入的角色分类为空");
    public static CodeMsg ACCOUNT_REPEAT = new CodeMsg(500217,"用户名已存在");
//    public static CodeMsg LOGIN_ACCOUNT_LOCK = new CodeMsg(500213, "账号已被冻结");
//    public static CodeMsg EMAIL_NO_SEND = new CodeMsg(500214, "邮件发送失败");
//    public static CodeMsg AUTHCODE_NOT_EQUAL = new CodeMsg(500703, "验证码错误");
//    public static CodeMsg EMAIL_ERROR = new CodeMsg(500704, "邮箱错误");

    //客户模块 5003XX
    //客户模块中的客户等级
    public static CodeMsg CUSTOMER_LEVEL_FAILURE = new CodeMsg(500301, "操作客户等级模块失败");
    public static CodeMsg CUSTOMER_ORIGIN_FAILURE = new CodeMsg(500310, "操作客户来源模块失败");
    public static CodeMsg CUSTOMER_STATE_FAILURE = new CodeMsg(500311, "操作客户状态模块失败");
    public static CodeMsg CUSTOMER_STAGE_FAILURE = new CodeMsg(500312, "操作客户阶段模块失败");
    public static CodeMsg CUSTOMER_FAILURE = new CodeMsg(500313, "操作客户模块失败");
    public static CodeMsg CUSTOMER_FOLLOW_FAILURE = new CodeMsg(500314, "操作客户跟进模块失败");
    public static CodeMsg CUSTOMER_UPDATE_FAILURE = new CodeMsg(500315, "更新客户操作失败");
    public static CodeMsg CUSTOMER_STAGE_LOG_FAILURE = new CodeMsg(500316, "更新客户阶段修改日志模块失败");
    public static CodeMsg CUSTOMER_RELATION_FAILURE = new CodeMsg(500317, "操作客户联系人模块失败");
    public static CodeMsg COMPLAINT_HANDLE = new CodeMsg(500317, "处理投诉失败");
    public static CodeMsg CUSTOMER_FOLLOW_PLAN_FAILURE = new CodeMsg(500318, "操作客户跟进计划模块失败");
    public static CodeMsg CUSTOMER_FOLLOW_ARRANGE_FAILURE = new CodeMsg(500319, "操作客户跟进任务模块失败");
    public static CodeMsg COMPLAINT_HANDLE_DELETE_ERROR = new CodeMsg(500320, "批量删除客户投诉&处理记录失败");
    public static CodeMsg CUSTOMER_RETURN_LESS_TOTAL = new CodeMsg(500318, "回款的总金额已经超出订单的总金额");


    //产品模块 5004XX
    public static CodeMsg PROD_NOT_EXIST = new CodeMsg(500400, "产品不存在");
    public static CodeMsg PROD_FAILURE = new CodeMsg(500401,"产品模块操作失败");
    public static CodeMsg PROD_UPDATE_FAILURE = new CodeMsg(500402,"更新产品操作失败");
    public static CodeMsg PROD_FILE_FAILURE = new CodeMsg(500403,"产品文件操作失败");

    //个人中心模块 5005XX
    public static CodeMsg PASSWORDS_DIFFER = new CodeMsg(500500, "两次输入的密码不一致");
    public static CodeMsg MESSAGE_GET_ERROR = new CodeMsg(500501, "内部消息获取失败");

    //文件模块 5006XX
    public static CodeMsg FILE_UPLOAD_ERROR = new CodeMsg(500600, "文件上传错误");
    public static CodeMsg UNKNOWN_REASON = new CodeMsg(500600, "文件上传未知错误");
    public static CodeMsg INVALID_FILE_TYPE = new CodeMsg(500603,"文件读取错误, 无效的文件类型!");
    public static CodeMsg FILE_DATE_EMPTY = new CodeMsg(500604,"文件数据为空");

    //权限模块
    public static CodeMsg NO_PERMISSION = new CodeMsg(500701, "用户权限不足");
    public static CodeMsg NO_LOGIN = new CodeMsg(500702, "用户未登录");

    //用户模块 5008XX
    public static CodeMsg USERNAME_BEEN_USED = new CodeMsg(500801, "用户名已被占用");
    public static CodeMsg EMAIL_BEEN_USED = new CodeMsg(500802, "邮箱已被注册");

    //回款计划模块 5009xx
    public static CodeMsg PLAN_DELETE_ERROR = new CodeMsg(500901,"该回款计划已被订单引用");
    public static CodeMsg PLANDETAIL_DELETE_ERROR = new CodeMsg(500902, "该回款明细已被订单及其对应回款计划引用");


    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 填充错误信息
     *
     * @param args
     * @return
     */
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
