package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.PointsExchangeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointsExchangeRepository extends JpaRepository<PointsExchangeEntity, Long>, JpaSpecificationExecutor<PointsExchangeEntity> {

    // 查找用户的兑换记录
    List<PointsExchangeEntity> findByUserIdOrderByExchangeTimeDesc(Long userId);

    // 根据状态查找兑换记录
    List<PointsExchangeEntity> findByStatusOrderByExchangeTimeDesc(String status);

    // 分页查询用户兑换记录
    Page<PointsExchangeEntity> findByUserId(Long userId, Pageable pageable);

    // 统计用户今日兑换次数
    @Query("SELECT COUNT(pe) FROM PointsExchangeEntity pe WHERE pe.userId = :userId AND DATE(pe.exchangeTime) = CURRENT_DATE")
    Long countTodayExchangesByUserId(@Param("userId") Long userId);
}