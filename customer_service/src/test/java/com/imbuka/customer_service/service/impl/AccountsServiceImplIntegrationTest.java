package com.imbuka.customer_service.service.impl;

import com.imbuka.customer_service.config.JWTService;
import com.imbuka.customer_service.dto.AccountCreationRequest;
import com.imbuka.customer_service.entity.Accounts;
import com.imbuka.customer_service.entity.User;
import com.imbuka.customer_service.enums.Role;
import com.imbuka.customer_service.repository.AccountsRepository;
import com.imbuka.customer_service.repository.TokenRepository;
import com.imbuka.customer_service.repository.UserRepository;
import com.imbuka.customer_service.token.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountsServiceImplIntegrationTest {

    @Autowired
    private AccountsServiceImpl accountsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private TokenRepository tokenRepository;

    private AccountCreationRequest accountCreationRequest;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        userRepository.deleteAll();
        accountsRepository.deleteAll();

        // Create test data
        accountCreationRequest = new AccountCreationRequest();
        accountCreationRequest.setName("Alvin Imbuka");
        accountCreationRequest.setEmail("alvin.imbuka@example.com");
        accountCreationRequest.setMsisdn("1234567890");
        accountCreationRequest.setPassword("password");
        accountCreationRequest.setRole(Role.CUSTOMER);
    }

    @Test
    void createAccount_ShouldCreateAccountInDatabase() {
        // Mock dependencies
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        // Call the method
        accountsService.createAccount(accountCreationRequest);

        // Verify results
        Optional<User> savedUser = userRepository.findByMsisdn("1234567890");
        assertTrue(savedUser.isPresent());
        assertEquals("John Doe", savedUser.get().getName());

        Optional<Accounts> savedAccount = accountsRepository.findByUserId(savedUser.get().getId());
        assertTrue(savedAccount.isPresent());
        assertEquals(0.0, savedAccount.get().getBalance());

        // Verify interactions
        verify(jwtService, times(1)).generateToken(any(User.class));
        verify(tokenRepository, times(1)).save(any(Token.class));
    }
}
