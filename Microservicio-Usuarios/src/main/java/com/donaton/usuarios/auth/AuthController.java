package com.donaton.usuarios.auth;

import com.donaton.usuarios.security.JwtService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        if (
                request.getCorreo().equals("admin@donaton.cl")
                        &&
                        request.getPassword().equals("1234")
        ) {

            String token = jwtService.generarToken(request.getCorreo());

            return new LoginResponse(token);
        }

        throw new RuntimeException("Credenciales invalidas");
    }
}