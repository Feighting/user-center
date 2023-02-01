package com.example.usercenter.constant;

/**
 * @ClassName: UserConstant
 * @Description: 用户常量
 * @Author: ZJL
 * @Date: 2023/1/15
 */
public class UserConstant {
    /**
     * 盐值
     */
    public static final String SALT = "yupi";

    /**
     * 正则表达式（校验特殊字符）
     */
    public static final String REGEX = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

    /**
     * 用户角色 0-普通用户
     */
    public static final int COMMON_USER = 0;
    /**
     * 用户角色 1-管理员
     */
    public static final int ADMIN_USER = 1;
}
