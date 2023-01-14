package com.example.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: UserLoginRequest
 * @Description: 用户登录时接收的前端参数
 * @Author: ZJL
 * @Date: 2023/1/14
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -4120714029430306843L;

    private String userAccount;

    private String userPassword;
}
