package com.example.usercenter.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wonder66
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-01-13 13:02:52
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param checkPassword    校验码
     * @return 用户ID
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request      request请求（用户获取/存放session 即登录态）
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);
}
