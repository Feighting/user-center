package com.example.usercenter.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenter.common.ErrorCodeEnum;
import com.example.usercenter.constant.UserConstant;
import com.example.usercenter.exception.BusinessException;
import com.example.usercenter.mapper.UserMapper;
import com.example.usercenter.model.domain.User;
import com.example.usercenter.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Wonder66
 * @Desc 针对表【user(用户表)】的数据库操作Service实现
 * @Date 2023-01-13 13:02:52
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {


    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        //校验用户的账户、密码、校验密码，是否符合要求
        //1. 非空（引入依赖）
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "数据为空");
        }
        //2. 账户长度 **不小于** 4 位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "账户长度过短");
        }
        //3. 密码 **不小于** 8 位
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "密码长度过短");
        }
        //星球编号长度小于5
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "星球编号长度过长");
        }
        //4. 账户不包含特殊字符（正则表达式）
        Matcher matcher = Pattern.compile(UserConstant.REGEX).matcher(userAccount);
        if (matcher.find()) {
            //匹配到特殊字符返回-1
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "账号包含特殊字符");
        }
        //5. 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "密码不一致");
        }
        //6. 账户不能重复（需要操作数据库的放在最后校验）
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        Long userAccountCount = userMapper.selectCount(wrapper);
        if (userAccountCount > 0) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "账号已存在");
        }
        //星球编号不能重复（需要操作数据库的放在最后校验）
        wrapper = new QueryWrapper<User>();
        wrapper.eq("planetCode", planetCode);
        Long planetCodeCount = userMapper.selectCount(wrapper);
        if (planetCodeCount > 0) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "星球编号已存在");
        }
        //7. 对密码进行加密（密码千万不要直接以明文存储到数据库中）
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        //8. 向数据库插入用户数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean resultSave = this.save(user);
        if (!resultSave) {
            throw new BusinessException(ErrorCodeEnum.INSERT_ERROR);
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1. 校验用户账户和密码是否合法
        //非空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        //账户长度 **不小于** 4 位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        //密码就 **不小于** 8 位吧
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        //账户不包含特殊字符
        Matcher matcher = Pattern.compile(UserConstant.REGEX).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        //校验密码是否输入正确，要和数据库中的密文密码去对比
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        wrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            log.info("Login failed, username and password do not match");
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        //3. 用户信息脱敏，隐藏敏感信息，防止数据库中的字段泄露
        User safetyUser = safetyUser(user);
        //4. 我们要记录用户的登录态（session），将其存到服务器上（用后端 SpringBoot 框架封装的服务器 tomcat 去记录）
        request.getSession().setAttribute(SESSION_KEY, safetyUser);
        //5. 返回脱敏后的用户信息
        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param user
     * @return
     */
    @Override
    public User safetyUser(User user) {
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhoneNumber(user.getPhoneNumber());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setPlanetCode(user.getPlanetCode());
        safetyUser.setCreateTime(user.getCreateTime());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态信息
        request.getSession().removeAttribute(SESSION_KEY);
        return 1;
    }
}




