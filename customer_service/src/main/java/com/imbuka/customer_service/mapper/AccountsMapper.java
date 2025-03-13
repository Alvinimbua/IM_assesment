package com.imbuka.customer_service.mapper;

import com.imbuka.customer_service.dto.AccountsDto;
import com.imbuka.customer_service.entity.Accounts;

public class AccountsMapper {

    /**
     * It is going to map all data from AccountsEntity  to AccountDto
     *
     * @param accounts
     * @param accountsDto
     * @return
     */
    public static AccountsDto mapToAccountsDto(Accounts accounts, AccountsDto accountsDto) {
        accountsDto.setAccountNumber(accounts.getAccountNumber());
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setBranchAddress(accounts.getBranchAddress());
        return accountsDto;
    }

    /**
     * It is going to map all data from AccountsDto to Accounts Entity
     *
     * @param accounts
     * @param accountsDto
     * @return
     */
    public static Accounts mapToAccounts(AccountsDto accountsDto, Accounts accounts) {
        accounts.setAccountNumber(accountsDto.getAccountNumber());
        accounts.setAccountType(accountsDto.getAccountType());
        accounts.setBranchAddress(accountsDto.getBranchAddress());
        return accounts;
    }

}
