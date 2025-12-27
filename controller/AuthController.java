package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.dto.LoginRequest;
import org.example.demomanagementsystemcproject.dto.RegisterRequest;
import org.example.demomanagementsystemcproject.entity.Admin;
import org.example.demomanagementsystemcproject.service.AdminService;
import org.example.demomanagementsystemcproject.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;

    public AuthController(AdminService adminService, JwtUtil jwtUtil) {
        this.adminService = adminService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Object login(@RequestBody LoginRequest request) {
        Admin admin = adminService.login(request.getUsername(), request.getPassword());
        if (admin == null) {
            return new HashMap<>() {{
                put("code", 400);
                put("message", "用户名或密码错误");
            }};
        }

        String token = jwtUtil.generateToken(admin.getUsername());
        return new HashMap<>() {{
            put("code", 200);
            put("message", "登录成功");
            put("token", token);
            put("username", admin.getUsername());
            put("role", admin.getRole());
            put("userId", admin.getId());
        }};
    }

    // 添加注册接口
    @PostMapping("/register")
    public Object register(@RequestBody RegisterRequest request) {
        try {
            Admin admin = adminService.register(request.getUsername(), request.getPassword(), request.getConfirmPassword(), request.getEmail());
            String token = jwtUtil.generateToken(admin.getUsername());
            return new HashMap<>() {{
                put("code", 200);
                put("message", "注册成功");
                put("token", token);
                put("username", admin.getUsername());
                put("role", admin.getRole());
                put("userId", admin.getId());
            }};
        } catch (RuntimeException e) {
            return new HashMap<>() {{
                put("code", 400);
                put("message", e.getMessage());
            }};
        }
    }

    // 添加用户名检查接口
    @GetMapping("/check-username")
    public Object checkUsername(@RequestParam String username) {
        boolean exists = adminService.checkUsernameExists(username);
        return new HashMap<>() {{
            put("code", 200);
            put("exists", exists);
            put("message", exists ? "用户名已存在" : "用户名可用");
        }};
    }
}