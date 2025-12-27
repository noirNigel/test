package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}
