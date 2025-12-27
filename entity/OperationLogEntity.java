package org.example.demomanagementsystemcproject.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_operation_log")
public class OperationLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 操作用户名 */
    @Column(name = "username")
    private String username;

    /** 所属模块，比如：系统管理 / 商品管理 / 订单管理 */
    @Column(name = "module")
    private String module;

    /** 操作名称，比如：新增员工 / 修改配置 */
    @Column(name = "action")
    private String action;

    /** 调用的方法全名 */
    @Column(name = "method")
    private String method;

    /** 请求 URI */
    @Column(name = "request_uri")
    private String requestUri;

    /** 请求方式 GET / POST ... */
    @Column(name = "request_method")
    private String requestMethod;

    /** 请求参数 JSON */
    @Column(name = "request_params", columnDefinition = "TEXT")
    private String requestParams;

    /** 耗时（毫秒） */
    @Column(name = "cost_time")
    private Long costTime;

    /** 请求 IP */
    @Column(name = "ip")
    private String ip;

    /** 创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /* ------------ getter / setter ------------ */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
