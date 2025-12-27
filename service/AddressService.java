package org.example.demomanagementsystemcproject.service;

import org.example.demomanagementsystemcproject.entity.AddressEntity;

import java.util.List;

public interface AddressService {
    List<AddressEntity> listByUser(Long userId);

    AddressEntity get(Long id);

    AddressEntity save(AddressEntity address);

    AddressEntity setDefault(Long userId, Long addressId);

    void delete(Long id, Long userId);
}
