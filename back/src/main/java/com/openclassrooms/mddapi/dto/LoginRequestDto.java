package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String identifier;
    private String password;
}