package org.example.demomanagementsystemcproject.service.impl;

import org.example.demomanagementsystemcproject.dto.CategoryDTO;
import org.example.demomanagementsystemcproject.entity.CategoryEntity;
import org.example.demomanagementsystemcproject.repo.CategoryRepository;
import org.example.demomanagementsystemcproject.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> getCategoryTree() {
        List<CategoryEntity> rootCategories = categoryRepository.findByParentIdIsNull();
        return rootCategories.stream()
                .map(this::convertToDTOWithChildren)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> getChildren(Long parentId) {
        if (parentId == null) {
            return new ArrayList<>();
        }
        List<CategoryEntity> children = categoryRepository.findByParentId(parentId);
        return children.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // 验证分类名称
        if (categoryDTO.getName() == null || categoryDTO.getName().trim().isEmpty()) {
            throw new RuntimeException("分类名称不能为空");
        }

        CategoryEntity entity = new CategoryEntity();
        BeanUtils.copyProperties(categoryDTO, entity);

        // 处理父分类ID
        if (categoryDTO.getParentId() == null) {
            entity.setParentId(null);
            entity.setLevel(1);
        } else {
            // 验证父分类是否存在
            CategoryEntity parent = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new RuntimeException("父分类不存在"));
            entity.setLevel(parent.getLevel() + 1);
        }

        // 设置排序值，如果没有提供则设为0
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }

        CategoryEntity saved = categoryRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));

        // 验证分类名称
        if (categoryDTO.getName() == null || categoryDTO.getName().trim().isEmpty()) {
            throw new RuntimeException("分类名称不能为空");
        }

        BeanUtils.copyProperties(categoryDTO, entity, "id", "level");
        CategoryEntity saved = categoryRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        // 检查分类是否存在
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("分类不存在");
        }

        // 检查是否有子分类
        List<CategoryEntity> children = categoryRepository.findByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("该分类下存在子分类，无法删除");
        }

        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateSortOrder(Long id, Integer sortOrder) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        entity.setSortOrder(sortOrder);
        categoryRepository.save(entity);
    }

    private CategoryDTO convertToDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    private CategoryDTO convertToDTOWithChildren(CategoryEntity entity) {
        CategoryDTO dto = convertToDTO(entity);

        // 递归加载子分类
        List<CategoryEntity> children = categoryRepository.findByParentId(entity.getId());
        if (!children.isEmpty()) {
            List<CategoryDTO> childDTOs = children.stream()
                    .map(this::convertToDTOWithChildren)
                    .collect(Collectors.toList());
            dto.setChildren(childDTOs);
        }

        return dto;
    }
}