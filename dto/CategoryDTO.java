package org.example.demomanagementsystemcproject.dto;

import java.util.List;

public class CategoryDTO {
    private Long id;
    private Long parentId;
    private String name;
    private Integer sortOrder;
    private Integer level;
    private List<CategoryDTO> children;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public List<CategoryDTO> getChildren() { return children; }
    public void setChildren(List<CategoryDTO> children) { this.children = children; }
}