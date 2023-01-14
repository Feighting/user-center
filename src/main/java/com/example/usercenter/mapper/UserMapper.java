package com.example.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.usercenter.model.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Wonder66
 * @description 针对表【user(用户表)】的数据库操作Mapper
 * @createDate 2023-01-13 13:02:52
 * @Entity generator.domain.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




