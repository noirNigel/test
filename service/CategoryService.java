package org.example.demomanagementsystemcproject.service;

import org.example.demomanagementsystemcproject.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    // 获取分类树
    List<CategoryDTO> getCategoryTree();

    // 获取子分类
    List<CategoryDTO> getChildren(Long parentId);

    // 添加分类
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    // 更新分类
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    // 删除分类
    void deleteCategory(Long id);

    // 更新排序
    void updateSortOrder(Long id, Integer sortOrder);
}