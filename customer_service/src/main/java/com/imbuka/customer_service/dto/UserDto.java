package com.imbuka.customer_service.dto;

import com.imbuka.customer_service.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Schema to hold Customer and Account Information"
)
public class UserDto {
    @Schema(
            description = "Name of the customer", example = "John Doe"
    )
    @NotEmpty(message = "Name cannot be null")
    @Size(min = 5, max = 30, message = "The length of the customer name should be between 5 and 30")
    private String name;

    @Schema(
            description = "Email of the customer", example = "doe@spring.com"
    )
    @Email(message = "Email address should be a valid value")
    @NotEmpty(message = "Email address can not be null or empty")
    private String email;

    @Schema(
            description = "Mobile Number of the customer", example = "0712345678"
    )
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String msisdn;

    @Schema(
            description = "Role of the customer"
    )
    private Role role;
    @Schema(
            description = "Account details of the customer"
    )
    private AccountsDto accountsDto;
}
