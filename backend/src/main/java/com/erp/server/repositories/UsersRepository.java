package com.erp.server.repositories;

import com.erp.server.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<UserDetails> findByUsername(String username);
}
