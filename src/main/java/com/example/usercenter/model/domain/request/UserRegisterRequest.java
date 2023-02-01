package com.example.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: UserRegisterRequest
 * @Description: 用户注册时接收的前端参数
 * @Author: ZJL
 * @Date: 2023/1/14
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -543286974259314538L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;
}
