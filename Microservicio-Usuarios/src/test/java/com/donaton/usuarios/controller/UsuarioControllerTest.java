package com.donaton.usuarios.controller;

import com.donaton.usuarios.dto.UsuarioDTO;
import com.donaton.usuarios.model.Rol;
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

        when(service.listar()).thenReturn(List.of(usuario));

        List<Usuario> resultado = controller.listar();

        assertEquals(1, resultado.size());
        verify(service).listar();
    }

    @Test
    void crearDebeGuardarUsuarioConRolPorDefecto() {
        // Escenario: Correo normal (cae en el "else")
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Matias");
        dto.setCorreo("matias@gmail.com");
        dto.setPassword("123456");

        // Configuramos el mock para que retorne el mismo usuario que recibe
        when(service.guardar(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        Usuario resultado = controller.crear(dto);

        assertNotNull(resultado);
        assertEquals("Matias", resultado.getNombre());
        assertEquals(Rol.USUARIO, resultado.getRol(), "Debe asignar Rol.USUARIO por defecto");

        verify(service).guardar(any(Usuario.class));
    }

    @Test
    void crearDebeGuardarUsuarioConRolMunicipalidad() {
        // Escenario: Correo termina en @mun.cl (cae en el primer "if")
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Matias Muni");
        dto.setCorreo("contacto@mun.cl");
        dto.setPassword("123456");

        when(service.guardar(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        Usuario resultado = controller.crear(dto);

        assertNotNull(resultado);
        assertEquals(Rol.MUNICIPALIDAD, resultado.getRol(), "Debe asignar Rol.MUNICIPALIDAD");

        verify(service).guardar(any(Usuario.class));
    }

    @Test
    void crearDebeGuardarUsuarioConRolLogistica() {
        // Escenario: Correo termina en @logistica.cl (cae en el "else if")
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Matias Log");
        dto.setCorreo("despachos@logistica.cl");
        dto.setPassword("123456");

        when(service.guardar(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        Usuario resultado = controller.crear(dto);

        assertNotNull(resultado);
        assertEquals(Rol.LOGISTICA, resultado.getRol(), "Debe asignar Rol.LOGISTICA");

        verify(service).guardar(any(Usuario.class));
    }
}