package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    // 根据父分类ID查找子分类
    List<CategoryEntity> findByParentId(Long parentId);

    // 查找所有顶级分类（parent_id为null）
    List<CategoryEntity> findByParentIdIsNull();

    // 更新排序
    @Modifying
    @Transactional
    @Query("UPDATE CategoryEntity c SET c.sortOrder = :sortOrder WHERE c.id = :id")
    int updateSortOrder(@Param("id") Long id, @Param("sortOrder") Integer sortOrder);

    // 根据层级查找分类
    List<CategoryEntity> findByLevel(Integer level);
}