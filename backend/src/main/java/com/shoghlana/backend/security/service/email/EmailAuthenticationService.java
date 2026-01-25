package com.shoghlana.backend.security.service.email;



import com.shoghlana.backend.security.dto.request.EmailAuthRequest;
import com.shoghlana.backend.security.dto.response.AuthResponse;
import com.shoghlana.backend.security.entity.AppAuthProvider;
import com.shoghlana.backend.security.entity.AppUser;
import com.shoghlana.backend.security.entity.Roles;
import com.shoghlana.backend.security.jwt.JwtUtils;
import com.shoghlana.backend.security.repository.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmailAuthenticationService {

    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepo appUserRepo;
    private final AuthenticationManager authManager;
    private final EmailVerificationService verificationService;

    public AuthResponse authenticate(EmailAuthRequest request) {

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

    private AuthResponse generateTokens(AppUser userFromDb) {
        final String accessToken = this.jwtUtils.generateAccessToken(userFromDb.getUsername());
        final String refreshToken = this.jwtUtils.generateRefreshToken(userFromDb.getUsername());

        return AuthResponse.builder()
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
                        .roles(Set.of(Roles.builder().name(request.role()).build())) // TODO--
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
