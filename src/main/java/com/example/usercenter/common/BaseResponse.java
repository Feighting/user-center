package com.example.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: BaseResponse
 * @Description: 通用返回类
 * @Author: ZJL
 * @Date: 2023/2/1
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 6395864705732354844L;

    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data,String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCodeEnum errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
