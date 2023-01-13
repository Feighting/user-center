package com.example.usercenter;

import com.example.usercenter.mapper.UserMapper;
import com.example.usercenter.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author zjl
 * @Date 2023-1-13
 * @Desc SampleTest
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SampleTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        //断言
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

}