package com.exception;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 自定义全局异常类
 */
@Data
@ApiModel(value = "自定义全局异常类")
public class IKEException extends RuntimeException {

    @ApiModelProperty(value = "状态码")
    private Integer code;

    /**
     * 接受状态码和消息
     */
    public IKEException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型
     */
    public IKEException(CodeMsg codeMsg) {
        super(codeMsg.getMsg());
        this.code = codeMsg.getCode();
    }

    /**
     * 重写toString()方法
     */
    @Override
    public String toString() {
        return "IKEException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}