package com.example.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercenter.common.BaseResponse;
import com.example.usercenter.common.ErrorCodeEnum;
import com.example.usercenter.constant.UserConstant;
import com.example.usercenter.exception.BusinessException;
import com.example.usercenter.model.domain.User;
import com.example.usercenter.model.domain.request.UserLoginRequest;
import com.example.usercenter.model.domain.request.UserRegisterRequest;
import com.example.usercenter.service.service.UserService;
import com.example.usercenter.common.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        //校验参数是否为空
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 封装的参数
     * @param request          请求对象
     * @return 脱敏的用户信息
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     *
     * @param request 请求对象
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }


    /**
     * 获取用户的登录态（用于前端判断用户是否已经登录过）
     *
     * @param request 请求对象
     * @return 脱敏User
     */
    @GetMapping("/current")
    public BaseResponse<User> current(HttpServletRequest request) {
        Object objUser = request.getSession().getAttribute(UserService.SESSION_KEY);
        User currentUser = (User) objUser;
        if (currentUser == null) {
            throw new BusinessException(ErrorCodeEnum.NO_LOGIN);
        }
        long userId = currentUser.getId();
        //TODO getById()需要校验用户是否合法（用户状态是否正常未被封号）
        User user = userService.getById(userId);
        User safetyUser = userService.safetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 用户查询
     *
     * @param username 用户名
     * @param request  登录态
     * @return User
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String username, HttpServletRequest request) {
        //1、鉴权（仅管理员可操作）
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.NO_AUTH);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            //like->如果username不为空，模糊查询；为空，查询全部
            wrapper.like("username", username);
        }
        List<User> userList = userService.list(wrapper);
        //返回脱敏用户
        List<User> resultList = userList.stream().map(user -> userService.safetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(resultList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody User user, HttpServletRequest request) {
        //1、鉴权（仅管理员可操作）
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        if (user.getId() <= 0) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        boolean result = userService.removeById(user.getId());
        return ResultUtils.success(result);
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
