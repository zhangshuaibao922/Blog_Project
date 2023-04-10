package com.ricardo.handler.exception;

import com.ricardo.domain.ResponseResult;
import com.ricardo.enums.AppHttpCodeEnum;
import com.ricardo.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ControllerAdvice
//@ResponseBody
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        //打印异常信息
//        log.info("出现了异常 {}",e);
        //从异常对象中获取提示信息 封装返回
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }
    @ExceptionHandler(Exception.class)
    public ResponseResult ExceptionHandler(Exception e){
        //打印异常信息
        log.info("出现了异常 {}",e);
        //从异常对象中获取提示信息 封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),
                AppHttpCodeEnum.SYSTEM_ERROR.getMsg());

    }
}