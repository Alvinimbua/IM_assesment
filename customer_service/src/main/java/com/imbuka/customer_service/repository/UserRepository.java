package com.imbuka.customer_service.repository;

import com.imbuka.customer_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByMsisdn(String msisdn);
}
