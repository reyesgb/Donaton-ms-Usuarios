package com.donaton.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioDTO {

    @NotBlank
    private String nombre;

    @Email
    private String correo;
    
    private String password;

    @NotBlank
    private String rol;
}