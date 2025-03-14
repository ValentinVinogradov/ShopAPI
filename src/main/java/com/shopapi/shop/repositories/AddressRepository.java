package com.shopapi.shop.repositories;

import com.shopapi.shop.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.isActive = true")
    Optional<Address> findActiveAddressByUserId(@Param("userId") UUID userId);

    Optional<Address> findByUser_IdAndId(UUID id, UUID activeAddressId);
}
