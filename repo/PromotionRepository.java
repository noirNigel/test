package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity, Long>,
        JpaSpecificationExecutor<PromotionEntity> {

    // 查找进行中的促销活动 - 修改：包含状态为1或null的活动
    @Query("SELECT p FROM PromotionEntity p WHERE (p.status = 1 OR p.status IS NULL) AND p.startTime <= :now AND p.endTime >= :now")
    List<PromotionEntity> findActiveAndNullStatusPromotions(@Param("now") LocalDateTime now);

    // 根据类型查找促销活动 - 修改：不限制状态
    List<PromotionEntity> findByType(String type);

    // 查找即将开始的活动 - 修改：包含状态为1或null的活动
    @Query("SELECT p FROM PromotionEntity p WHERE (p.status = 1 OR p.status IS NULL) AND p.startTime > :now ORDER BY p.startTime ASC")
    List<PromotionEntity> findUpcomingPromotions(@Param("now") LocalDateTime now);
}