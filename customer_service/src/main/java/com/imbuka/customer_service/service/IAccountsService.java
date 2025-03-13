package com.imbuka.customer_service.service;

import com.imbuka.customer_service.dto.AccountCreationRequest;
import com.imbuka.customer_service.dto.UserDto;

public interface IAccountsService {
    /**
     * @param accountCreationRequest - UserDtoObject
     */
    void createAccount(AccountCreationRequest accountCreationRequest);

    /**
     * @param msisdn - Input mobile Number
     * @return AccountDetails based on a given mobile Number
     */
    UserDto fetchAccount(String msisdn);

    /**
     * @param userDto - UserDtoObject
     * @return boolean indicating if the update of Account details is Successful or not.
     */
    boolean updateAccount(UserDto userDto);

    /**
     * @param msisdn - Input mobile NUmber
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(String msisdn);
}
