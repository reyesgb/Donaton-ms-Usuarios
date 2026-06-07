package com.donaton.usuarios.security;

import com.donaton.usuarios.controller.UsuarioController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsuarioController.class)
@Import({SecurityConfig.class, JwtFilter.class}) // <-- Importamos el filtro real
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired // <-- Usamos el filtro real en lugar del Mock
    private JwtFilter jwtFilter;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private com.donaton.usuarios.service.UsuarioService usuarioService;

    @Test
    void passwordEncoder_DebeEstarDisponibleComoBean() {
        assertNotNull(passwordEncoder);
    }

    @Test
    void rutaUsuarios_DebeSerPublica() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    void rutaAuth_DebeSerPublica() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isNotFound());
    }

    @Test
    void otraRuta_SinAutenticacion_DebeRetornar401o403() throws Exception {
        mockMvc.perform(get("/ruta-protegida-aleatoria"))
                .andExpect(status().isForbidden());
    }
}