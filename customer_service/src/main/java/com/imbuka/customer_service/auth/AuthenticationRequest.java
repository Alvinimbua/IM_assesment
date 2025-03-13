package com.imbuka.customer_service.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Authentication Request",
        description = "Schema to hold Authentication request Information"
)
public class AuthenticationRequest {

    @Schema(
            description = "Email of the registered User",
            example = "twain@gmail.com"
    )
    private String email;

    @Schema(
            description = "Password of the registered User",
            example = "123456qwty"
    )
    private String password;
}
