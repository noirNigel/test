package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.PointsRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointsRuleRepository extends JpaRepository<PointsRuleEntity, Long> {

    @Query("SELECT p FROM PointsRuleEntity p WHERE p.status = 1 ORDER BY p.id DESC")
    Optional<PointsRuleEntity> findLatestActive();
}
