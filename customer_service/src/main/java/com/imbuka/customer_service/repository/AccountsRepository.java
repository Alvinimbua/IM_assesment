package com.imbuka.customer_service.repository;

import com.imbuka.customer_service.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    Optional<Accounts> findByUserId(Long userId);

    @Transactional
    @Modifying
    void deleteByUserId(Long userId);

}
