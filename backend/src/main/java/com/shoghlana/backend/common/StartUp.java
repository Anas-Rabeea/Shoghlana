package com.shoghlana.backend.common;

import com.shoghlana.backend.security.entity.AppAuthProvider;
import com.shoghlana.backend.security.entity.AppUser;
import com.shoghlana.backend.security.entity.Roles;
import com.shoghlana.backend.security.repository.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StartUp implements CommandLineRunner {

    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if(appUserRepo.count() == 0){

            AppUser appUserDeveloper =
                    AppUser
                            .builder()
                            .email("developer-backend@mehna.com")
                            .password(passwordEncoder.encode("P#@!sswor$#!!99"))
                            .roles(Set.of(Roles.builder().name("DEVELOPER").build())) // TODO--

                    .appAuthProvider(AppAuthProvider.EMAIL)
                            .emailVerified(true)
                            .build();
            AppUser appUserAdmin =
                    AppUser
                            .builder()
                            .email("admin-backend@mehna.com")
                            .password(passwordEncoder.encode("P#@!sswor$#!!99"))
                            .roles(Set.of(Roles.builder().name("ADMIN").build())) // TODO--
                            .appAuthProvider(AppAuthProvider.EMAIL)
                            .emailVerified(true)
                            .build();

            this.appUserRepo.saveAll(List.of(appUserAdmin, appUserDeveloper));
        }





    }
}
