package com.shoghlana.backend.user.entity;

import com.shoghlana.backend.security.entity.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_profile")
@Getter @Setter
@AllArgsConstructor  @NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // endpoint will be like this /users/132

    // Personal Info (Can add some more)
    private String fullName;
    private String city;
    @Column(name = "profile_image_path")
    private String profileImagePath;

    // user must have app_user_id to get user_profile:id
    @OneToOne(optional = false)
    @JoinColumn(name = "app_user_id" , unique = true, nullable = false)
    private AppUser appUser;


}
