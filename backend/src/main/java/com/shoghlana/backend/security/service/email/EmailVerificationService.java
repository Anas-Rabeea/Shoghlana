package com.shoghlana.backend.security.service.email;


import com.shoghlana.backend.security.entity.AppUser;
import com.shoghlana.backend.security.repository.AppUserRepo;
import com.shoghlana.backend.security.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {


    private static final Duration token_ttl = Duration.ofMinutes(3);

    private final RedisTemplate<String,String> redisTemplate;
    private final EmailSenderService emailSenderService;
    private final VerificationTokenService verificationTokenService;
    private final AppUserRepo userRepo;


    public void sendVerificationEmail(String email) {

        String token =  verificationTokenService.generateEmailVerificationToken(10);
        String redisKey = "email-verification:token:" + token;
        redisTemplate
                .opsForValue()
                .set(redisKey,email ,token_ttl);

        String verificationLink = "http://localhost:8055/api/v1/auth/verify-email?token=" + token;
        // send the email
        String verificationEmailContent = """
                    Hello %s,
                    Please verify your account by clicking the link below:
                    %s
                    NOTE: This link expires in 3 minutes.
                    If you did not make this request, please disregard this email.
                """.formatted(email.substring( 0 , email.indexOf("@") ) , verificationLink) ;
        String verificationEmailTitle = "Your Verification Link For Mehna";
        emailSenderService.send(
                email,
                verificationEmailContent,
                verificationEmailTitle
        );


    }

    public void verifyEmail(String token){

        String redisKey = "email-verification:token:" + token;
        String email = redisTemplate
                            .opsForValue()
                            .get(redisKey);
        // if the given token = token in redis > verify isEmailVerified = true else = false and exception
        if ( email == null)
            throw new RuntimeException("Invalid Verification Token.");

        AppUser user =  this.userRepo
                            .findByEmail(email)
                            .orElseThrow(() -> new UsernameNotFoundException("Email not found."));
        user.setEmailVerified(true);
        log.info( email + " is verified at " + LocalDateTime.now());
        this.userRepo.saveAndFlush(user); // to update the new registered user email verification state
        redisTemplate.delete(redisKey);
    }

}
