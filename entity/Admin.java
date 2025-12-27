package org.example.demomanagementsystemcproject.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 登录账号 */
    @Column(name = "username", nullable = false, unique = true, length = 255)
    private String username;

    /** 登录密码（已加密或明文） */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /** 邮箱（注册可选，默认值 xxx@xxx.com） */
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    /** 角色：ADMIN / MANAGER / STAFF 等 */
    @Column(name = "role", length = 50)
    private String role;

    /** 账号状态：1 启用，0 禁用 */
    @Column(name = "status")
    private Integer status = 1;

    /** 所属门店 ID（多门店支持） */
    @Column(name = "store_id")
    private Long storeId;

    /** 地址（最多 30 个汉字） */
    @Column(name = "address", length = 90)
    private String address;

    /** 标签（与 address 一一对应，建议使用 JSON 数组存储） */
    @Column(name = "address_tags", columnDefinition = "TEXT")
    private String addressTags;

    /** 联系方式 */
    @Column(name = "contact_info", length = 255)
    private String contactInfo;

    /** 收货人 */
    @Column(name = "receiver_name", length = 100)
    private String receiverName;

    /** 积分（可用于积分商城兑换） */
    @Column(name = "points")
    private Integer points = 0;

    /** 成长积分(用于等级，累计不减) */
    @Column(name = "level_points")
    private Integer levelPoints = 0;

    /** 可用积分余额（抵扣用，可减少） */
    @Column(name = "available_points")
    private Integer availablePoints = 0;

    /** 创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** 更新时间 */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* ------------ 生命周期回调：自动填充时间 ------------ */

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = 1;
        }
        if (this.points == null) {
            this.points = 0;
        }
        if (this.levelPoints == null) {
            this.levelPoints = 0;
        }
        if (this.availablePoints == null) {
            this.availablePoints = 0;
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressTags() {
        return addressTags;
    }

    public void setAddressTags(String addressTags) {
        this.addressTags = addressTags;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getLevelPoints() {
        return levelPoints;
    }

    public void setLevelPoints(Integer levelPoints) {
        this.levelPoints = levelPoints;
    }

    public Integer getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(Integer availablePoints) {
        this.availablePoints = availablePoints;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
