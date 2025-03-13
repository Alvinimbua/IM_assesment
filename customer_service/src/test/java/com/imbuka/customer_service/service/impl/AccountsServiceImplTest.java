package com.imbuka.customer_service.service.impl;

import com.imbuka.customer_service.config.JWTService;
import com.imbuka.customer_service.dto.AccountCreationRequest;
import com.imbuka.customer_service.dto.UserDto;
import com.imbuka.customer_service.entity.Accounts;
import com.imbuka.customer_service.entity.User;
import com.imbuka.customer_service.exception.CustomerAlreadyExistsException;
import com.imbuka.customer_service.exception.ResourceNotFoundException;
import com.imbuka.customer_service.repository.AccountsRepository;
import com.imbuka.customer_service.repository.TokenRepository;
import com.imbuka.customer_service.repository.UserRepository;
import com.imbuka.customer_service.token.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountsServiceImplUnitTest {

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AccountsServiceImpl accountsService;

    private AccountCreationRequest accountCreationRequest;
    private User user;
    private Accounts accounts;

    @BeforeEach
    void setUp() {
        accountCreationRequest = new AccountCreationRequest();
        accountCreationRequest.setName("John Doe");
        accountCreationRequest.setEmail("john.doe@example.com");
        accountCreationRequest.setMsisdn("1234567890");
        accountCreationRequest.setPassword("password");
        accountCreationRequest.setRole(com.imbuka.customer_service.enums.Role.CUSTOMER);

        user = new User();
        user.setId(1L);
        user.setMsisdn("1234567890");

        accounts = new Accounts();
        accounts.setAccountNumber(1000000001L);
        accounts.setUserId(1L);
        accounts.setBalance(0.0);
    }

    @Test
    void createAccount_ShouldCreateAccountSuccessfully() {
        // Mock dependencies
        when(userRepository.findByMsisdn(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(accountsRepository.save(any(Accounts.class))).thenReturn(accounts);

        // Call the method
        accountsService.createAccount(accountCreationRequest);

        // Verify interactions
        verify(userRepository, times(1)).findByMsisdn(accountCreationRequest.getMsisdn());
        verify(userRepository, times(1)).save(any(User.class));
        verify(accountsRepository, times(1)).save(any(Accounts.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void createAccount_ShouldThrowException_WhenCustomerAlreadyExists() {
        // Mock dependencies
        when(userRepository.findByMsisdn(anyString())).thenReturn(Optional.of(user));

        // Call the method and assert exception
        assertThrows(CustomerAlreadyExistsException.class, () -> {
            accountsService.createAccount(accountCreationRequest);
        });

        // Verify interactions
        verify(userRepository, times(1)).findByMsisdn(accountCreationRequest.getMsisdn());
        verify(userRepository, never()).save(any(User.class));
        verify(accountsRepository, never()).save(any(Accounts.class));
    }

    @Test
    void fetchAccount_ShouldReturnUserDto() {
        // Mock dependencies
        when(userRepository.findByMsisdn(anyString())).thenReturn(Optional.of(user));
        when(accountsRepository.findByUserId(anyLong())).thenReturn(Optional.of(accounts));

        // Call the method
        UserDto userDto = accountsService.fetchAccount("1234567890");

        // Verify results
        assertNotNull(userDto);
        assertEquals(user.getMsisdn(), userDto.getMsisdn());
        assertEquals(accounts.getAccountNumber(), userDto.getAccountsDto().getAccountNumber());

        // Verify interactions
        verify(userRepository, times(1)).findByMsisdn("1234567890");
        verify(accountsRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    void fetchAccount_ShouldThrowException_WhenUserNotFound() {
        // Mock dependencies
        when(userRepository.findByMsisdn(anyString())).thenReturn(Optional.empty());

        // Call the method and assert exception
        assertThrows(ResourceNotFoundException.class, () -> {
            accountsService.fetchAccount("1234567890");
        });

        // Verify interactions
        verify(userRepository, times(1)).findByMsisdn("1234567890");
        verify(accountsRepository, never()).findByUserId(anyLong());
    }

    @Test
    void deleteAccount_ShouldDeleteAccountSuccessfully() {
        // Mock dependencies
        when(userRepository.findByMsisdn(anyString())).thenReturn(Optional.of(user));

        // Call the method
        boolean result = accountsService.deleteAccount("1234567890");

        // Verify results
        assertTrue(result);

        // Verify interactions
        verify(userRepository, times(1)).findByMsisdn("1234567890");
        verify(accountsRepository, times(1)).deleteByUserId(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void deleteAccount_ShouldThrowException_WhenUserNotFound() {
        // Mock dependencies
        when(userRepository.findByMsisdn(anyString())).thenReturn(Optional.empty());

        // Call the method and assert exception
        assertThrows(ResourceNotFoundException.class, () -> {
            accountsService.deleteAccount("1234567890");
        });

        // Verify interactions
        verify(userRepository, times(1)).findByMsisdn("1234567890");
        verify(accountsRepository, never()).deleteByUserId(anyLong());
        verify(userRepository, never()).deleteById(anyLong());
    }
}