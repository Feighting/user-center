package com.example.usercenter.service.service.impl;

import com.example.usercenter.model.domain.User;
import com.example.usercenter.service.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertTrue;

/**
 * @Author: ZJL
 * @Date: 2023/1/13
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Resource
    private UserService userService;

    @Test
    public void addUserTest() {
        User user = new User();
        user.setUsername("李思思");
        user.setUserAccount("lisisi");
        user.setUserPassword("123456");
        user.setGender(0);
        user.setPhoneNumber("123456");
        user.setEmail("123456@qq.com");
        user.setAvatarUrl("https://www.csdn.net/?spm=1001.2101.3001.4476");
        boolean result = userService.save(user);
        assertTrue(result);
        System.out.println(user.getId());
    }

    /**
     * 测试用户注册
     */
    @Test
    public void userRegister() {
        long result = userService.userRegister("yupi", "1234", "1234","1");
        Assert.assertEquals(3, result);
        System.out.println(result);
    }
}