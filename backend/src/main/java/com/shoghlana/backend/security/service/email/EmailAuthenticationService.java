package com.shoghlana.backend.security.service.email;



import com.shoghlana.backend.security.entity.AppUser;
import com.shoghlana.backend.security.jwt.JwtUtils;
import com.shoghlana.backend.security.repository.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailAuthenticationService {

    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepo appUserRepo;
    private final AuthenticationManager authManager;
    private final EmailVerificationService verificationService;

    public EmailAuthResponse authenticate(EmailAuthRequest request) {

        Optional<AppUser> userFromDb = appUserRepo
                .findByEmail(request.email());

        if(userFromDb.isEmpty()){
            AppUser newUser = registerNewUser(request);
            this.verificationService.sendVerificationEmail(request.email());
        }
        AppUser user = userFromDb.get();

        if(!user.isEmailVerified()){
            this.verificationService.sendVerificationEmail(user.getEmail());
        }

        authenticateUser(user);

        return generateTokens(user);
    }

    private EmailAuthResponse generateTokens(AppUser userFromDb) {
        final String accessToken = this.jwtUtils.generateAccessToken(userFromDb.getUsername());
        final String refreshToken = this.jwtUtils.generateRefreshToken(userFromDb.getUsername());

        return EmailAuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    private AppUser registerNewUser(EmailAuthRequest request){
        final AppUser newAppUser =
                AppUser
                        .builder()
                        .email(request.email())
                        .password(passwordEncoder.encode( request.password()) )
                        .appAuthProvider(AppAuthProvider.EMAIL)
                        .emailVerified(false)
                        .role(Role.valueOf(request.role()))
                        .build();
        return appUserRepo.save(newAppUser);
    }

    private void authenticateUser(AppUser user) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                ) ;
        authManager.authenticate(authToken);
    }
}
