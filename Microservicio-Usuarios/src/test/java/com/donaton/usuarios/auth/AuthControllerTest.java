package com.donaton.usuarios.auth;

import com.donaton.usuarios.model.Rol;
import com.donaton.usuarios.model.Usuario;
import com.donaton.usuarios.repository.UsuarioRepository;
import com.donaton.usuarios.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController controller;

    @Test
    void loginExitoso() {

        LoginRequest request = new LoginRequest();
        request.setCorreo("admin@test.cl");
        request.setPassword("123456");

        Usuario usuario = new Usuario();
        usuario.setCorreo("admin@test.cl");
        usuario.setPassword("HASH");
        usuario.setRol(Rol.ADMIN);

        when(usuarioRepository.findByCorreo("admin@test.cl"))
                .thenReturn(usuario);

        when(passwordEncoder.matches("123456", "HASH"))
                .thenReturn(true);

        when(jwtService.generarToken(usuario))
                .thenReturn("TOKEN123");

        ResponseEntity<?> response =
                controller.login(request);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void loginUsuarioNoExiste() {

        LoginRequest request = new LoginRequest();
        request.setCorreo("fake@test.cl");

        when(usuarioRepository.findByCorreo("fake@test.cl"))
                .thenReturn(null);

        ResponseEntity<?> response =
                controller.login(request);

        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void loginPasswordIncorrecta() {

        LoginRequest request = new LoginRequest();
        request.setCorreo("admin@test.cl");
        request.setPassword("mala");

        Usuario usuario = new Usuario();
        usuario.setPassword("HASH");

        when(usuarioRepository.findByCorreo("admin@test.cl"))
                .thenReturn(usuario);

        when(passwordEncoder.matches("mala", "HASH"))
                .thenReturn(false);

        ResponseEntity<?> response =
                controller.login(request);

        assertEquals(401, response.getStatusCode().value());
    }
}