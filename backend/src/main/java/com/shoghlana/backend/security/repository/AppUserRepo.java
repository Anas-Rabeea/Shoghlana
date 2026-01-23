package com.shoghlana.backend.security.repository;


import com.shoghlana.backend.security.entity.AppAuthProvider;
import com.shoghlana.backend.security.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser,String> {


    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByPhone(String phone);
    Optional<AppUser> findByUsername(String username);

    boolean existsByEmailVerified(boolean emailVerified);
    boolean existsByPhoneVerified(boolean phoneVerified);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

//    Optional<AppUser> findByAppAuthProviderAndProviderId(
//            String appAuthProvider, String providerId);
    Optional<AppUser> findByAppAuthProviderAndProviderId(
            AppAuthProvider appAuthProvider, String providerId);

    @Query(nativeQuery = true , value = "SELECT COUNT(id) as count FROM `app-user` ")
    long count();

    long countByEmailVerified(boolean emailVerified);
    long countByPhoneVerified(boolean phoneVerified);
}
