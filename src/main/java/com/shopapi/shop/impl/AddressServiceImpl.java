package com.shopapi.shop.impl;

import com.shopapi.shop.dto.AddressRequestDTO;
import com.shopapi.shop.models.Address;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repositories.AddressRepository;
import com.shopapi.shop.services.AddressService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRespository;
    private final UserServiceImpl userService;


    public AddressServiceImpl(AddressRepository addressRespository, UserServiceImpl userService) {
        this.addressRespository = addressRespository;
        this.userService = userService;
    }


    public void addAddress(UUID userId, AddressRequestDTO addressRequestDTO) {
        User user = userService.getById(userId);

        Address activeAddress = addressRespository.findActiveAddressByUserId(userId).orElse(null);
        if (activeAddress != null) {
            activeAddress.setActive(false);
            addressRespository.save(activeAddress);
        }

        Address newAddress = new Address();
        newAddress.setUser(user);
        newAddress.setRegion(addressRequestDTO.region());
        newAddress.setCity(addressRequestDTO.city());
        newAddress.setHouseNumber(addressRequestDTO.houseNumber());
        newAddress.setStreet(addressRequestDTO.street());
        newAddress.setActive(true);
        addressRespository.save(newAddress);
    }

    public void changeActiveAddress(UUID userId, UUID activeAddressId) {
        Address newActiveAddress = addressRespository.findByUser_IdAndId(userId, activeAddressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        addressRespository.findActiveAddressByUserId(userId).ifPresent(currentActiveAddress -> {
            if (currentActiveAddress.getId().equals(activeAddressId)) {
                throw new IllegalStateException("The address is already active.");
            }
            currentActiveAddress.setActive(false);
            addressRespository.save(currentActiveAddress);
        });

        newActiveAddress.setActive(true);
        addressRespository.save(newActiveAddress);
    }


    public void updateAddress(UUID userId, UUID addressId, AddressRequestDTO addressRequestDTO) {
        //todo
    }

    public String getActiveAddedUserAddress(UUID userId) {
//        Address address = addressRespository.findActiveAddressByUserId(userId)
//                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        Address address = addressRespository.findActiveAddressByUserId(userId).orElse(null);
        if (address == null) {
            return null;
        }
        return String.join(", ",
                address.getRegion(),
                address.getCity(),
                address.getStreet(),
                address.getHouseNumber().toString());
    }

    public void deleteAddressById(UUID userId, UUID addressId) {
        Address address = addressRespository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        if (address.getUser().getId().equals(userId)) {
            addressRespository.deleteById(addressId);
        } else {
            throw new RuntimeException("Failed to delete address");
        }
    }
}
