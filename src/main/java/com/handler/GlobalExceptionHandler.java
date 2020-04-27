package com.handler;


import com.common.Result;
import com.exception.CodeMsg;
import com.exception.IKEException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Result error(HttpMessageNotReadableException e){
        e.printStackTrace();
        return Result.error(CodeMsg.JSON_PARSE_ERROR);
    }

    /**
     * 自定义异常处理
     */
    @ExceptionHandler(IKEException.class)
    @ResponseBody
    public Result error(IKEException e){
        e.printStackTrace();
        return Result.error(e.getCode(), e.getMessage());
    }
}
