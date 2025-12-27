package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.MemberLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberLevelRepository extends JpaRepository<MemberLevelEntity, Long>,
        JpaSpecificationExecutor<MemberLevelEntity> {

    // 根据状态查找会员等级
    List<MemberLevelEntity> findByStatus(Integer status);

    // 根据积分查找对应的会员等级 - 修改：不限制状态
    @Query("SELECT ml FROM MemberLevelEntity ml WHERE ml.minPoints <= :points ORDER BY ml.minPoints DESC")
    List<MemberLevelEntity> findLevelByPoints(@Param("points") Integer points);

    // 查找启用的会员等级并按积分要求排序
    List<MemberLevelEntity> findByStatusOrderByMinPointsAsc(Integer status);

    // 新增：查询所有非空状态的会员等级并按积分降序排列
    @Query("SELECT ml FROM MemberLevelEntity ml WHERE ml.status IS NOT NULL ORDER BY ml.minPoints DESC")
    List<MemberLevelEntity> findByStatusIsNotNullOrderByMinPointsDesc();

    @Query("SELECT ml FROM MemberLevelEntity ml WHERE ml.status = 1 ORDER BY ml.sortOrder ASC, ml.minPoints ASC")
    List<MemberLevelEntity> findActiveLevels();
}