package com.donaton.usuarios.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;

    @AfterEach
    void tearDown() {
        // Limpiamos el contexto después de cada prueba para no afectar a las demás
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_SinHeader_DebeContinuarFiltro() throws ServletException, IOException {
        // Simulamos que la petición no trae el header "Authorization"
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        // Verificamos que NUNCA se llame al validador de tokens
        verify(jwtService, never()).tokenValido(anyString());
        // Verificamos que no se haya autenticado a nadie en Spring Security
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        // Verificamos que la petición siga su curso hacia el siguiente filtro
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ConHeaderInvalido_DebeContinuarFiltro() throws ServletException, IOException {
        // Simulamos que trae un header pero no es "Bearer"
        when(request.getHeader("Authorization")).thenReturn("Basic auth123");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, never()).tokenValido(anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ConTokenInvalido_DebeContinuarFiltroSinAutenticar() throws ServletException, IOException {
        String token = "tokenMalo123";
        // Simulamos que trae un Bearer token
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        // Pero el servicio nos dice que el token NO es válido
        when(jwtService.tokenValido(token)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        // Verificamos que sí se consultó la validez del token
        verify(jwtService).tokenValido(token);
        // Verificamos que NUNCA se extraiga el correo de un token inválido
        verify(jwtService, never()).extraerCorreo(anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ConTokenValido_DebeAutenticarYContinuar() throws ServletException, IOException {
        String token = "tokenBueno123";
        String correoMock = "usuario@correo.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.tokenValido(token)).thenReturn(true);
        when(jwtService.extraerCorreo(token)).thenReturn(correoMock);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).tokenValido(token);
        verify(jwtService).extraerCorreo(token);

        // Verificamos que el usuario SÍ fue inyectado en el contexto de seguridad
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(correoMock, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain).doFilter(request, response);
    }
}