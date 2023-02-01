package com.example.usercenter.exception;

import com.example.usercenter.common.ErrorCodeEnum;

/**
 * @ClassName: BusinessException
 * @Description: 自定义异常
 * @Author: ZJL
 * @Date: 2023/2/1
 */
public class BusinessException extends RuntimeException {

    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCodeEnum errorCode,String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }


    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
