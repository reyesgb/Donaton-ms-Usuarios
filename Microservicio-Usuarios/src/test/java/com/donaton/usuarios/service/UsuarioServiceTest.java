package com.donaton.usuarios.service;

import com.donaton.usuarios.model.Rol;
import com.donaton.usuarios.model.Usuario;
import com.donaton.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    @Test
    void guardarDebeEncriptarPasswordYGuardarUsuario() {

        Usuario usuario = new Usuario();
        usuario.setNombre("Matias");
        usuario.setCorreo("matias@gmail.com");
        usuario.setPassword("123456");
        usuario.setRol(Rol.USUARIO);

        when(passwordEncoder.encode("123456"))
                .thenReturn("HASH123");

        when(repository.save(any(Usuario.class)))
                .thenReturn(usuario);

        Usuario resultado = service.guardar(usuario);

        verify(passwordEncoder).encode("123456");
        verify(repository).save(usuario);

        assertNotNull(resultado);
    }

    @Test
    void listarDebeRetornarListaDeUsuarios() {

        List<Usuario> usuarios = List.of(
                new Usuario(),
                new Usuario()
        );

        when(repository.findAll())
                .thenReturn(usuarios);

        List<Usuario> resultado = service.listar();

        assertEquals(2, resultado.size());

        verify(repository).findAll();
    }
}