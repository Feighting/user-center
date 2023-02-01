package com.example.usercenter.exception;

import com.example.usercenter.common.BaseResponse;
import com.example.usercenter.common.ErrorCodeEnum;
import com.example.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.transform.Result;

/**
 * @ClassName: GlobalExceptionHandler
 * @Description: 全局异常处理器（@RestControllerAdvice切面AOP）
 * @Author: ZJL
 * @Date: 2023/2/1
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("BusinessException:{}", e.getMessage());
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException:{}", e.getMessage());
        return ResultUtils.error(ErrorCodeEnum.SYSTEM_ERROR, e.getMessage(), "");
    }
}
