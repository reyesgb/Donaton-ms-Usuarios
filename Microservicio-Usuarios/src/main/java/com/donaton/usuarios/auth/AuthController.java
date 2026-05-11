package com.donaton.usuarios.auth;

import com.donaton.usuarios.model.Usuario;
import com.donaton.usuarios.repository.UsuarioRepository;
import com.donaton.usuarios.security.JwtService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(
            JwtService jwtService,
            UsuarioRepository usuarioRepository
    ) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioRepository
                .findByCorreo(request.getCorreo())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(request.getPassword())) {

            throw new RuntimeException("Contraseña incorrecta");
        }

        String token =
                jwtService.generarToken(usuario.getCorreo());

        return new LoginResponse(token);
    }
}