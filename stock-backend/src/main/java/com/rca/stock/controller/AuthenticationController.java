package com.rca.stock.controller;

import com.rca.stock.dto.auth.AuthenticationRequest;
import com.rca.stock.dto.auth.AuthenticationResponse;
import com.rca.stock.dto.auth.RegisterRequest;
import com.rca.stock.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name= "Authentication")
public class AuthenticationController {
    private final AuthenticationService service;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegisterRequest request
    ) throws MessagingException {
        service.register(request);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Registration successful");

        return ResponseEntity.accepted().body(responseBody);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }


    @GetMapping("activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) throws MessagingException {
        String response = service.forgotPassword(email);
        return ResponseEntity.ok(response);

    }



    @PutMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String password){
        return service.resetPassword(token,password);
    }

        @PutMapping("/update-password")
        public ResponseEntity<String> updatePassword(
                @RequestParam("email") String email,
                @RequestParam("currentPassword") String currentPassword,
                @RequestParam("newPassword") String newPassword
        ) {
            String response = service.updatePassword(email, currentPassword, newPassword);
            return ResponseEntity.ok(response);
        }

    @GetMapping("/me")
    public ResponseEntity<AuthenticationResponse> getAuthenticatedUser() {
        AuthenticationResponse response = service.getAuthenticatedUserDetail();
        return ResponseEntity.ok(response);
    }


}
