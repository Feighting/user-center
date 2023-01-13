package com.example.usercenter.model;

import lombok.Data;

/**
 * @Author zjl
 * @Date 2023-1-13
 * @Desc User
 */
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}