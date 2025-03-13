package com.imbuka.customer_service.service.impl;

import com.imbuka.customer_service.config.JWTService;
import com.imbuka.customer_service.constants.AccountsConstants;
import com.imbuka.customer_service.dto.AccountCreationRequest;
import com.imbuka.customer_service.dto.AccountsDto;
import com.imbuka.customer_service.dto.UserDto;
import com.imbuka.customer_service.entity.Accounts;
import com.imbuka.customer_service.entity.User;
import com.imbuka.customer_service.exception.CustomerAlreadyExistsException;
import com.imbuka.customer_service.exception.ResourceNotFoundException;
import com.imbuka.customer_service.mapper.AccountsMapper;
import com.imbuka.customer_service.mapper.UserMapper;
import com.imbuka.customer_service.repository.AccountsRepository;
import com.imbuka.customer_service.repository.TokenRepository;
import com.imbuka.customer_service.repository.UserRepository;
import com.imbuka.customer_service.service.IAccountsService;
import com.imbuka.customer_service.token.Token;
import com.imbuka.customer_service.token.TokenType;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JWTService jwtService;
    private TokenRepository tokenRepository;

    @Override
    public void createAccount(AccountCreationRequest accountCreationRequest) {
        User user = UserMapper.mapToCustomer(accountCreationRequest, new User());
        Optional<User> optionalUser = userRepository.findByMsisdn(user.getMsisdn());
        if (optionalUser.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobile Number " + accountCreationRequest.getMsisdn());
        }

        var userSaved = User.builder()
                .name(accountCreationRequest.getName())
                .email(accountCreationRequest.getEmail())
                .msisdn(accountCreationRequest.getMsisdn())
                .password(passwordEncoder.encode(accountCreationRequest.getPassword()))
                .role(accountCreationRequest.getRole())
                .build();
        var savedUser = userRepository.save(userSaved);
        var jwtToken = jwtService.generateToken(userSaved);
        saveUserToken(userSaved, jwtToken);
        accountsRepository.save(createNewAccount(savedUser));

    }

    /**
     * @param user
     * @return
     */
    private Accounts createNewAccount(User user) {
        Accounts newAccount = new Accounts();
        newAccount.setUserId(user.getId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        double balance = 0.0;

        newAccount.setBalance(balance);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;

    }

    /**
     * @param msisdn - Input mobile Number
     * @return
     */
    @Override
    public UserDto fetchAccount(String msisdn) {
        User user = userRepository.findByMsisdn(msisdn).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "msisdn", msisdn)
        );

        Accounts accounts = accountsRepository.findByUserId(user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", user.getId().toString())
        );

        UserDto userDto = UserMapper.mapToCustomerDto(user, new UserDto());
        userDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return userDto;
    }


    /**
     * @param userDto - UserDtoObject
     * @return
     */
    @Override
    public boolean updateAccount(UserDto userDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = userDto.getAccountsDto();
        if (accountsDto != null) {
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );

            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getUserId();
            User customer = userRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerId", customerId.toString())
            );

            UserMapper.mapToCustomer(userDto, customer);
            userRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    /**
     * @param msisdn - Input mobile NUmber
     * @return
     */
    @Override
    public boolean deleteAccount(String msisdn) {
        User customer = userRepository.findByMsisdn(msisdn).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "msisdn", msisdn)
        );
        accountsRepository.deleteByUserId(customer.getId());
        userRepository.deleteById(customer.getId());
        return true;
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

}
