package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    // 根据SKU查找商品
    ProductEntity findBySku(String sku);

    // 根据状态查找商品
    List<ProductEntity> findByStatus(Integer status);

    // 批量更新商品状态
    @Modifying
    @Transactional
    @Query("UPDATE ProductEntity p SET p.status = :status WHERE p.id IN :ids")
    int updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") Integer status);

    // 批量更新库存
    @Modifying
    @Transactional
    @Query("UPDATE ProductEntity p SET p.stock = p.stock + :quantity WHERE p.id = :id")
    int updateStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    // 查找低库存商品 - 确保方法名正确
    @Query("SELECT p FROM ProductEntity p WHERE p.stock <= p.warningThreshold")
    List<ProductEntity> findLowStockProducts();

    // 根据阈值查找低库存商品
    @Query("SELECT p.id, p.name, p.stock FROM ProductEntity p WHERE p.stock <= :threshold ORDER BY p.stock ASC")
    List<Object[]> findProductsLowStock(@Param("threshold") int threshold);

    // 根据分类查找商品
    Page<ProductEntity> findByCategoryId(Long categoryId, Pageable pageable);
}