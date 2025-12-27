package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {

    OrderEntity findByOrderNo(String orderNo);

    List<OrderEntity> findByStatus(String status);

    List<OrderEntity> findByStatusAndPayStatus(String status, String payStatus);

    // 统计今日订单
    @Query(value = "SELECT COUNT(*) FROM orders WHERE DATE(created_at) = CURDATE()", nativeQuery = true)
    long countOrdersToday();

    // 统计今日销售额
    @Query(value = "SELECT IFNULL(SUM(total_amount),0) FROM orders WHERE DATE(created_at) = CURDATE()", nativeQuery = true)
    BigDecimal sumSalesToday();

    // 销售趋势查询
    @Query(value = "SELECT DATE(created_at) AS day, COUNT(*) AS orders_cnt, IFNULL(SUM(total_amount),0) AS sales_sum " +
            "FROM orders " +
            "WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL :days-1 DAY) " +
            "GROUP BY DATE(created_at) " +
            "ORDER BY DATE(created_at)", nativeQuery = true)
    List<Object[]> ordersAndSalesByDay(@Param("days") int days);

    // 批量更新订单状态
    @Modifying
    @Transactional
    @Query("UPDATE OrderEntity o SET o.status = :status WHERE o.id IN :ids")
    int updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") String status);

    // 查找需要语音提醒的新订单（过去5分钟内创建的新订单）
    @Query("SELECT o FROM OrderEntity o WHERE o.status = 'NEW' AND o.createdAt >= :sinceTime")
    List<OrderEntity> findNewOrdersSince(@Param("sinceTime") java.time.LocalDateTime sinceTime);

    // 添加缺失的 topProducts 方法
    @Query(value = "SELECT oi.product_id, p.name, SUM(oi.quantity) as sold_quantity, SUM(oi.subtotal) as revenue " +
            "FROM order_item oi " +
            "LEFT JOIN product p ON oi.product_id = p.id " +
            "LEFT JOIN orders o ON oi.order_id = o.id " +
            "WHERE o.created_at >= DATE_SUB(CURDATE(), INTERVAL :days DAY) " +
            "AND o.status IN ('COMPLETED', 'CONFIRMED') " +
            "GROUP BY oi.product_id, p.name " +
            "ORDER BY revenue DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> topProducts(@Param("days") int days, @Param("limit") int limit);
}