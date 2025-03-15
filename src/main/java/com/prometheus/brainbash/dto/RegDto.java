package com.prometheus.brainbash.dto;

import com.prometheus.brainbash.model.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegDto {
	@NotBlank(message = "Username is required")
	private String username;

    @NotBlank(message = "Password is required")
    private String password;
    
    @NotNull(message = "Role must not be null")
    private Role role;
}
