package com.imbuka.customer_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Accounts",
        description = "Schema to hold Account Information"
)
public class AccountsDto {
    @NotEmpty(message = "Account number can not be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits ")
    @Schema(

            description = "Account Number of IM Account",
            example = "0987654321"
    )
    private Long accountNumber;
    @NotEmpty(message = "Account Type can not be null or empty")
    @Schema(
            description = "Account Type of IM Account", example = "Locked Savings Account"
    )
    private String accountType;
    @NotEmpty(message = "Branch Address can not be null or empty")
    @Schema(
            description = "IM Branch Address",
            example = "300 sky dale"
    )
    private String branchAddress;
}
