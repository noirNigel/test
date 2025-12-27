package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.PointsGoodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointsGoodsRepository extends JpaRepository<PointsGoodsEntity, Long>, JpaSpecificationExecutor<PointsGoodsEntity> {

    // 查找可兑换的积分商品
    @Query("SELECT pg FROM PointsGoodsEntity pg WHERE pg.status = 1 AND pg.stock > 0 ORDER BY pg.sortOrder ASC, pg.id DESC")
    List<PointsGoodsEntity> findAvailableGoods();

    // 根据状态查找积分商品
    List<PointsGoodsEntity> findByStatusOrderBySortOrderAsc(Integer status);

    // 分页查询积分商品
    Page<PointsGoodsEntity> findByStatus(Integer status, Pageable pageable);
}