package com.rca.stock.services;

import com.rca.stock.dto.auth.AuthenticationRequest;
import com.rca.stock.dto.auth.AuthenticationResponse;
import com.rca.stock.dto.auth.RegisterRequest;
import com.rca.stock.model.ResetToken;
import com.rca.stock.model.Role;
import com.rca.stock.model.User;
import com.rca.stock.model.VerificationToken;
import com.rca.stock.repository.ResetTokenRepository;
import com.rca.stock.repository.TokenRepository;
import com.rca.stock.repository.UserRepository;
import com.rca.stock.template.EmailTemplate;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Value("${application.security.mailing.frontend.activation-url}")
    private String activationUrl;
    @Value("${application.security.mailing.frontend.reset-url}")
    private String reseturl;

    public void register(RegisterRequest request) throws MessagingException {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .createdDate(LocalDateTime.now())
                .role(Role.USER)
                .build();

        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);


        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplate.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );

    }

    public void sendResetTokenEmail(User user) throws  MessagingException{
        var newToken = generateAndSaveResetToken(user);
        String resetToken = String.format("http://localhost:8080/api/v1/auth/reset-password?token=%s", newToken);

        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplate.RESET_PASSWORD,
                reseturl,
                resetToken,
                "Reset Password"
        );

        System.out.println("reset token: " + resetToken);
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken =  generateActivationCode(6);
        var token = VerificationToken.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        tokenRepository.save(token);

        return generatedToken;
    }



    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for(int i=0; i < length; i++){
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String , Object>();
        var user = ((User)auth.getPrincipal());
        claims.put("fullName", user.fullName());
        var jwt = jwtService.generateToken(claims ,user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .user(user)
                .build();
    }

    public void activateAccount(String token) throws MessagingException {
        VerificationToken savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Token"));

        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            sendValidationEmail(savedToken.getUser());
            throw  new RuntimeException("Activation Token has expired. A new token has been sent to the same email");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }







    public String forgotPassword(String email) throws MessagingException {
        try {
            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User with the email not found"));

            sendResetTokenEmail(user);

            return "Password reset token sent to email.";
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace to see the exact error
            throw new RuntimeException("Error occurred during password reset."); // Handle the error
        }
    }


    public String resetPassword(String token, String password) {
        ResetToken savedToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Token"));

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            throw new RuntimeException("Reset token has expired.");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("Saved token: " + savedToken);
        System.out.println("new password: " + password);


        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        resetTokenRepository.delete(savedToken);

        return "Password successfully reset.";
    }


    public String updatePassword(String email , String currentPassword, String newPassword){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email , currentPassword)
        );

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password successfully updated.";
    }
    private String generateAndSaveResetToken(User user) {
        String generatedToken = generateToken();
        var token = ResetToken.builder()
                .user(user)
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        resetTokenRepository.save(token);
        return generatedToken;
    }

    private String generateToken() {
        return UUID.randomUUID().toString() + UUID.randomUUID().toString();
    }

    public AuthenticationResponse getAuthenticatedUserDetail() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authentication context found");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return AuthenticationResponse.builder()
                    .user(user)
                    .build();
        } else {
            throw new RuntimeException("User details not found");
        }
    }

}
