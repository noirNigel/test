package org.example.demomanagementsystemcproject.repository;

import org.example.demomanagementsystemcproject.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByUsername(String username);

    boolean existsByUsername(String username);
}
