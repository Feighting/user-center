package com.example.usercenter.common;

/**
 * @ClassName: ResultUtils
 * @Description: 通用结果返回工具类，用来处理统一返回结果信息
 * @Author: ZJL
 * @Date: 2023/2/1
 */
public class ResultUtils {

    /**
     * 成功的返回
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok", "请求成功");
    }

    /**
     * 失败的返回
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCodeEnum errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败的返回
     *
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCodeEnum errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(),null, message, description);
    }

    /**
     * 失败的返回
     *
     * @param errorCode
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCodeEnum errorCode, String description) {
        return new BaseResponse<>(errorCode.getCode(),null, errorCode.getMessage(), description);
    }

    /**
     * 失败的返回
     *
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }
}
