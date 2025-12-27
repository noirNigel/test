package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findByUserIdOrderByIsDefaultDescIdDesc(Long userId);

    Optional<AddressEntity> findFirstByUserIdAndIsDefault(Long userId, Integer isDefault);
}
