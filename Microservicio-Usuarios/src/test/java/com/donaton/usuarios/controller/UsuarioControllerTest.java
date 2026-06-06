package com.donaton.usuarios.controller;

import com.donaton.usuarios.dto.UsuarioDTO;
import com.donaton.usuarios.model.Usuario;
import com.donaton.usuarios.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService service;

    @InjectMocks
    private UsuarioController controller;

    @Test
    void listarDebeRetornarUsuarios() {

        Usuario usuario = new Usuario();
        usuario.setNombre("Matias");

        when(service.listar())
                .thenReturn(List.of(usuario));

        List<Usuario> resultado =
                controller.listar();

        assertEquals(1, resultado.size());

        verify(service).listar();
    }

    @Test
    void crearDebeGuardarUsuario() {

        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Matias");
        dto.setCorreo("matias@gmail.com");
        dto.setPassword("123456");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setNombre("Matias");

        when(service.guardar(any(Usuario.class)))
                .thenReturn(usuarioGuardado);

        Usuario resultado =
                controller.crear(dto);

        assertNotNull(resultado);

        assertEquals("Matias",
                resultado.getNombre());

        verify(service).guardar(any(Usuario.class));
    }
}