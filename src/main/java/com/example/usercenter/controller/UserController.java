package com.example.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercenter.constant.UserConstant;
import com.example.usercenter.model.domain.User;
import com.example.usercenter.model.domain.request.UserLoginRequest;
import com.example.usercenter.model.domain.request.UserRegisterRequest;
import com.example.usercenter.service.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: UserController
 * @Description:
 * @Author: ZJL
 * @Date: 2023/1/14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 封装的参数
     * @return 用户ID
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        //校验参数是否为空
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 封装的参数
     * @param request          请求对象
     * @return 脱敏的用户信息
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    /**
     * 用户查询
     *
     * @param username 用户名
     * @param request  登录态
     * @return User
     */
    @GetMapping("/search")
    public List<User> searchUser(String username, HttpServletRequest request) {
        //1、鉴权（仅管理员可操作）
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            //like->如果username不为空，模糊查询；为空，查询全部
            wrapper.like("username", username);
        }
        List<User> userList = userService.list(wrapper);
        //返回脱敏用户
        return userList.stream().map(user -> userService.safetyUser(user)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody User user, HttpServletRequest request) {
        //1、鉴权（仅管理员可操作）
        if (!isAdmin(request)) {
            return false;
        }
        if (user.getId() <= 0) {
            return false;
        }
        return userService.removeById(user.getId());
    }

    /**
     * 是否为管理员
     *
     * @param request 登录态
     * @return boolean
     */
    public boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(UserService.SESSION_KEY);
        return user != null && user.getUserRole() == UserConstant.ADMIN_USER;
    }
}
