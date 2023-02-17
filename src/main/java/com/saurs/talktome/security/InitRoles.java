package com.saurs.talktome.security;

import com.saurs.talktome.models.Role;
import com.saurs.talktome.models.enums.Roles;
import com.saurs.talktome.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class InitRoles {

    @Autowired
    RoleRepository roleRepository;

    @PostConstruct
    public void loadRoles() {
        if (getRoles().isEmpty()) {
            Role user_role = new Role();
            Role admin_role = new Role();
            user_role.setName(String.valueOf(Roles.ROLE_USER));
            admin_role.setName(String.valueOf(Roles.ROLE_ADMIN));
            roleRepository.saveAll(Arrays.asList(user_role, admin_role));
        }
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

}
