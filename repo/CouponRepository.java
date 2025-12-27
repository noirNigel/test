package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.CouponEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long>, JpaSpecificationExecutor<CouponEntity> {

    // 查找可领取的优惠券
    @Query("SELECT c FROM CouponEntity c WHERE c.status = 1 AND c.startTime <= :now AND c.endTime >= :now AND c.remainingCount > 0")
    List<CouponEntity> findAvailableCoupons(@Param("now") LocalDateTime now);

    // 根据状态查找优惠券
    List<CouponEntity> findByStatus(Integer status);

    // 查找即将过期的优惠券
    @Query("SELECT c FROM CouponEntity c WHERE c.endTime BETWEEN :start AND :end AND c.status = 1")
    List<CouponEntity> findExpiringCoupons(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}