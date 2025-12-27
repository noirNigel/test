package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.entity.StoreEntity;
import org.example.demomanagementsystemcproject.entity.SystemConfigEntity;
import org.example.demomanagementsystemcproject.repo.StoreRepository;
import org.example.demomanagementsystemcproject.repo.SystemConfigRepository;
import org.example.demomanagementsystemcproject.system.OperationLog;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system/backup")
public class BackupController {

    private final StoreRepository storeRepo;
    private final SystemConfigRepository configRepo;

    public BackupController(StoreRepository storeRepo,
                            SystemConfigRepository configRepo) {
        this.storeRepo = storeRepo;
        this.configRepo = configRepo;
    }

    // 导出备份：门店 + 配置
    @GetMapping("/export")
    public Map<String, Object> exportBackup() {
        Map<String, Object> data = new HashMap<>();
        List<StoreEntity> stores = storeRepo.findAll();
        List<SystemConfigEntity> configs = configRepo.findAll();

        data.put("stores", stores);
        data.put("configs", configs);
        data.put("exportTime", System.currentTimeMillis());

        return data;
    }

    // 导入备份（为了简单，这里只给结构，你可以视情况实现）
    @PostMapping("/import")
    @OperationLog(module = "系统设置", action = "导入备份")
    public Map<String, Object> importBackup(@RequestBody Map<String, Object> data) {
        // 这里可以按自己需要解析 stores/configs 再保存
        // 为了避免误操作，课程项目你也可以只做“假导入”，直接返回成功
        return Map.of("code", 200, "message", "导入成功（示例接口，可按需要完善）");
    }
}
