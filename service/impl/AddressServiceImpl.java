package org.example.demomanagementsystemcproject.service.impl;

import org.example.demomanagementsystemcproject.entity.AddressEntity;
import org.example.demomanagementsystemcproject.repo.AddressRepository;
import org.example.demomanagementsystemcproject.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<AddressEntity> listByUser(Long userId) {
        return addressRepository.findByUserIdOrderByIsDefaultDescIdDesc(userId);
    }

    @Override
    public AddressEntity get(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("地址不存在"));
    }

    @Override
    @Transactional
    public AddressEntity save(AddressEntity address) {
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            List<AddressEntity> list = addressRepository.findByUserIdOrderByIsDefaultDescIdDesc(address.getUserId());
            for (AddressEntity item : list) {
                if (item.getIsDefault() != null && item.getIsDefault() == 1
                        && !Objects.equals(item.getId(), address.getId())) {
                    item.setIsDefault(0);
                    addressRepository.save(item);
                }
            }
        }
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public AddressEntity setDefault(Long userId, Long addressId) {
        AddressEntity address = get(addressId);
        if (!address.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改");
        }
        address.setIsDefault(1);
        return save(address);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        AddressEntity address = get(id);
        if (!address.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除");
        }
        addressRepository.deleteById(id);
    }
}
