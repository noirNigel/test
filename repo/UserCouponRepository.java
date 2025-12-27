package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.UserCouponEntity;
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
public interface UserCouponRepository extends JpaRepository<UserCouponEntity, Long>, JpaSpecificationExecutor<UserCouponEntity> {

    // 查找用户的优惠券
    List<UserCouponEntity> findByUserIdAndStatus(Long userId, String status);

    // 查找用户可用的优惠券
    @Query("SELECT uc FROM UserCouponEntity uc WHERE uc.userId = :userId AND uc.status = 'UNUSED' AND uc.expiredTime > :now")
    List<UserCouponEntity> findAvailableUserCoupons(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    // 统计用户对某个优惠券的领取数量
    @Query("SELECT COUNT(uc) FROM UserCouponEntity uc WHERE uc.userId = :userId AND uc.couponId = :couponId")
    Long countByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);

    // 查找过期的用户优惠券
    @Query("SELECT uc FROM UserCouponEntity uc WHERE uc.status = 'UNUSED' AND uc.expiredTime < :now")
    List<UserCouponEntity> findExpiredUserCoupons(@Param("now") LocalDateTime now);

    // 分页查询用户优惠券
    Page<UserCouponEntity> findByUserId(Long userId, Pageable pageable);
}