package org.example.demomanagementsystemcproject.repo;

import org.example.demomanagementsystemcproject.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    List<OrderItemEntity> findByOrderId(Long orderId);

    void deleteByOrderId(Long orderId);
}