package com.example.tokenauthserver.repository;

import com.example.tokenauthserver.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
