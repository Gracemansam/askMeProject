package com.askMeProject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TwoFAVerificationRequest {
    private String email;
    private String code;

}
