package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.entity.AddressEntity;
import org.example.demomanagementsystemcproject.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    private Long requireUserId(String xUserId) {
        if (xUserId == null || xUserId.isBlank()) {
            throw new RuntimeException("缺少用户信息");
        }
        return Long.valueOf(xUserId);
    }

    @GetMapping
    public List<AddressEntity> list(@RequestHeader(value = "X-User-Id", required = false) String xUserId) {
        Long userId = requireUserId(xUserId);
        return addressService.listByUser(userId);
    }

    @PostMapping
    public AddressEntity create(@RequestHeader(value = "X-User-Id", required = false) String xUserId,
                                @RequestBody AddressEntity dto) {
        Long userId = requireUserId(xUserId);
        dto.setUserId(userId);
        return addressService.save(dto);
    }

    @PutMapping("/{id}")
    public AddressEntity update(@RequestHeader(value = "X-User-Id", required = false) String xUserId,
                                @PathVariable Long id,
                                @RequestBody AddressEntity dto) {
        Long userId = requireUserId(xUserId);
        AddressEntity existing = addressService.get(id);
        if (!existing.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改");
        }
        existing.setRecipient(dto.getRecipient());
        existing.setPhone(dto.getPhone());
        existing.setProvince(dto.getProvince());
        existing.setCity(dto.getCity());
        existing.setDistrict(dto.getDistrict());
        existing.setDetail(dto.getDetail());
        existing.setTag(dto.getTag());
        if (dto.getIsDefault() != null) {
            existing.setIsDefault(dto.getIsDefault());
        }
        return addressService.save(existing);
    }

    @PutMapping("/{id}/default")
    public AddressEntity setDefault(@RequestHeader(value = "X-User-Id", required = false) String xUserId,
                                    @PathVariable Long id) {
        Long userId = requireUserId(xUserId);
        return addressService.setDefault(userId, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader(value = "X-User-Id", required = false) String xUserId,
                       @PathVariable Long id) {
        Long userId = requireUserId(xUserId);
        addressService.delete(id, userId);
    }
}
