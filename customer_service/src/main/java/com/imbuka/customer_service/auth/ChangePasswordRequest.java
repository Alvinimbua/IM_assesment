package com.imbuka.customer_service.auth;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChangePasswordRequest {

    private String currentPassword;
    private String confirmPassword;
    private String newPassword;
}
