package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.SystemConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemConfigRepository extends JpaRepository<SystemConfigEntity, Long> {

    SystemConfigEntity findByConfigKey(String configKey);
}
