package com.donaton.usuarios.security;

import com.donaton.usuarios.model.Rol;
import com.donaton.usuarios.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        // Al no tener dependencias inyectadas, instanciamos la clase directamente
        jwtService = new JwtService();
    }

    @Test
    void generarToken_DebeRetornarStringNoVacio() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@correo.com");
        usuario.setRol(Rol.USUARIO);

        String token = jwtService.generarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isEmpty(), "El token generado no debería estar vacío");
    }

    @Test
    void extraerCorreo_DebeRetornarElCorreoOriginal() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@correo.com");
        usuario.setRol(Rol.USUARIO);

        String token = jwtService.generarToken(usuario);
        String correoExtraido = jwtService.extraerCorreo(token);

        assertEquals("test@correo.com", correoExtraido, "El correo extraído debe coincidir con el del usuario original");
    }

    @Test
    void tokenValido_ConTokenCorrecto_DebeRetornarTrue() {
        // Escenario del "try" exitoso
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@correo.com");
        usuario.setRol(Rol.USUARIO);

        String token = jwtService.generarToken(usuario);
        boolean esValido = jwtService.tokenValido(token);

        assertTrue(esValido, "El token recién generado debería ser válido");
    }

    @Test
    void tokenValido_ConTokenFalso_DebeRetornarFalse() {
        // Escenario donde cae en el "catch"
        String tokenMalo = "esto.no.es.un.token.valido";

        boolean esValido = jwtService.tokenValido(tokenMalo);

        assertFalse(esValido, "Un token inventado debe ser detectado como inválido");
    }
}