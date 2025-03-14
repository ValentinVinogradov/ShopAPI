package com.shopapi.shop.impl;

import com.shopapi.shop.models.Role;
import com.shopapi.shop.repositories.RoleRepository;
import com.shopapi.shop.services.RoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleRepository.findRoleByName("ROLE_" + roleName)
                .orElseThrow(() -> new EntityNotFoundException("Роль не найдена"));
    }
}
