package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.OperationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface OperationLogRepository extends JpaRepository<OperationLogEntity, Long> {

    default List<OperationLogEntity> findAllOrderByCreatedAtDesc() {
        return findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
