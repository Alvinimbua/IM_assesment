package com.imbuka.transaction_service.repository;

import com.imbuka.transaction_service.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Accounts, Long> {

    Optional<Accounts> findByUserId(Long userId);
}
