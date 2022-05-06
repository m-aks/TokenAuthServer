package com.example.tokenauthserver.repository;

import com.example.tokenauthserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
    User findByEmail(String email);
}
