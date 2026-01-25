package com.shoghlana.backend.security.dto.request;

import com.shoghlana.backend.common.validation.EgyptianPhone.ValidEgyptianPhone;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PhoneAuthRequest(

        @NotBlank
        @ValidEgyptianPhone
        String phone,

        @Enumerated(EnumType.STRING)
        String role

        )
{}
