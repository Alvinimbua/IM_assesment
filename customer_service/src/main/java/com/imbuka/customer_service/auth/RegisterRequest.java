package com.imbuka.customer_service.auth;

import com.imbuka.customer_service.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
        name = "Register Request",
        description = "Schema to hold Register request Information"
)
public class RegisterRequest {
    @Schema(
            description = "Name of a User",
            example = "Alvin Imbuka"
    )
    private String name;

    @Schema(
            description = "Email of a User",
            example = "twain@gmail.com"
    )
    private String email;

    @Schema(
            description = "Password of a User",
            example = "123456qwty"
    )
    private String password;

    @Schema(
            description = "Phone Number of a User",
            example = "123456qwty"
    )
    private String msisdn;
    @Schema(
            description = "Role of the User",
            example = "ADMIN or NORMAL_USER"
    )
    private Role role;
}
