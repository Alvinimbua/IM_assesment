package com.imbuka.customer_service.controller;

import com.imbuka.customer_service.auth.AuthenticationRequest;
import com.imbuka.customer_service.auth.AuthenticationResponse;
import com.imbuka.customer_service.auth.RegisterRequest;
import com.imbuka.customer_service.entity.User;
import com.imbuka.customer_service.service.AuthenticationService;
import com.imbuka.customer_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request, //object where we can get or read the authorization header which will hold the refreshToken
            HttpServletResponse response //object that will help us re-inject the response back to the user
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @GetMapping("/findAllUsers")
    public List<User> findAllUsers() {
        return authenticationService.findAll();
    }

    @PutMapping("/updateProfile/{id}")
    public ResponseEntity<String> updateUserProfile(@PathVariable Long id, @RequestBody RegisterRequest request) {
        userService.updateUserProfile(id, request);
        return ResponseEntity.ok("User profile updated Successfully");
    }
}
