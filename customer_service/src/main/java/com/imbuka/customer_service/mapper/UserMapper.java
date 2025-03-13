package com.imbuka.customer_service.mapper;

import com.imbuka.customer_service.dto.AccountCreationRequest;
import com.imbuka.customer_service.dto.UserDto;
import com.imbuka.customer_service.entity.User;

public class UserMapper {

    public static UserDto mapToCustomerDto(User user, UserDto userDto) {

        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setMsisdn(user.getMsisdn());
        userDto.setRole(user.getRole());
        return userDto;
    }

    public static User mapToCustomer(UserDto userDto, User user) {
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setMsisdn(userDto.getMsisdn());
        user.setRole(userDto.getRole());
        return user;
    }

    public static User mapToCustomer(AccountCreationRequest userDto, User user) {
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setMsisdn(userDto.getMsisdn());
        user.setRole(userDto.getRole());
        return user;
    }
}
