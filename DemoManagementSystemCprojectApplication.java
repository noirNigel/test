package org.example.demomanagementsystemcproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;   // ← 必须加这个！

@SpringBootApplication
public class DemoManagementSystemCprojectApplication {

    public static void main(String[] args) {

        // 设置默认时区（修复 ZoneId.setDefault 不存在的问题）
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));

        SpringApplication.run(DemoManagementSystemCprojectApplication.class, args);
    }
}
