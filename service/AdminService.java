package org.example.demomanagementsystemcproject.service;

import org.example.demomanagementsystemcproject.entity.Admin;

public interface AdminService {

    // 登录
    Admin login(String username, String password);

    // 注册
    Admin register(String username, String password, String confirmPassword, String email);

    // 检查用户名是否存在
    boolean checkUsernameExists(String username);

    // 根据用户名查找 Admin（给 JwtFilter 用）
    Admin findByUsername(String username);
}
