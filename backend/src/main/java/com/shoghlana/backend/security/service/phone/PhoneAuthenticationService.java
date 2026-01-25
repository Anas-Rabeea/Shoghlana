package com.shoghlana.backend.security.service.phone;



import com.shoghlana.backend.security.dto.request.PhoneAuthRequest;
import com.shoghlana.backend.security.dto.response.AuthResponse;
import com.shoghlana.backend.security.entity.AppAuthProvider;
import com.shoghlana.backend.security.entity.AppUser;
import com.shoghlana.backend.security.entity.Roles;
import com.shoghlana.backend.security.jwt.JwtUtils;
import com.shoghlana.backend.security.repository.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PhoneAuthenticationService {


        private final OtpServiceImpl otpServiceImpl;
        private final AppUserRepo appUserRepo;
        private final JwtUtils jwtUtils;

        public AuthResponse authenticate(PhoneAuthRequest request)
        {
            Optional<AppUser> userFromDb = appUserRepo
                    .findByPhone(request.phone());

            if(userFromDb.isEmpty()){
                AppUser newUser =
                        registerNewUser(request);
                this.otpServiceImpl.sendPhoneVerificationOtp(request.phone());
            }

            // TODO-- Handle this exception to not return 500 Internal Server Error
            AppUser user = userFromDb.get();

            // Check if email is verified (Some users add email and password and wait till email verification)
            if(!user.isPhoneVerified()){
                this.otpServiceImpl.sendPhoneVerificationOtp(user.getPhone());
            }
            // cant make user authenticated via SecurityContextHolder as it needs username/password combination
            return generateJwtTokens(user);
        }

    private AppUser registerNewUser(PhoneAuthRequest request){
        final AppUser newAppUser =
                AppUser
                        .builder()
                        .phone(request.phone())
                        .appAuthProvider(AppAuthProvider.PHONE)
                        .phoneVerified(false)
                        .roles(Set.of(Roles.builder().name(request.role()).build())) // TODO--
                        .build();
        return appUserRepo.save(newAppUser);
    }

    private AuthResponse generateJwtTokens(AppUser userFromDb) {
        final String accessToken = this.jwtUtils.generateAccessToken(userFromDb.getUsername());
        final String refreshToken = this.jwtUtils.generateRefreshToken(userFromDb.getUsername());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
