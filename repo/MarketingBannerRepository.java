package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.MarketingBannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MarketingBannerRepository extends JpaRepository<MarketingBannerEntity, Long> {
    List<MarketingBannerEntity> findAllByOrderBySortOrderAscCreatedAtDesc();

    List<MarketingBannerEntity> findByStatusOrderBySortOrderAscCreatedAtDesc(Integer status);

    @Query("SELECT b FROM MarketingBannerEntity b " +
            "WHERE (b.status IS NULL OR b.status = 1) " +
            "AND (b.startTime IS NULL OR b.startTime <= :now) " +
            "AND (b.endTime IS NULL OR b.endTime >= :now) " +
            "ORDER BY b.sortOrder ASC, b.createdAt DESC")
    List<MarketingBannerEntity> findActiveBanners(@Param("now") LocalDateTime now);
}
