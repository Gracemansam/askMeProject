package com.askMeProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
	
	private String jwt;
	
	private boolean status;
	private boolean mfaEnabled;
	private String secretImageUri;


	public AuthResponse(String jwt, boolean status) {
		this.jwt = jwt;
		this.status = status;
	}
}
