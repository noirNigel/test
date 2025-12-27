package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.entity.OperationLogEntity;
import org.example.demomanagementsystemcproject.service.OperationLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/system/logs")
public class OperationLogController {

    private final OperationLogService svc;

    public OperationLogController(OperationLogService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<OperationLogEntity> list() {
        return svc.list();
    }
}
