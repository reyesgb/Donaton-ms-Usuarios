package com.donaton.usuarios.controller;

import com.donaton.usuarios.dto.UsuarioDTO;
import com.donaton.usuarios.model.Rol;
import com.donaton.usuarios.model.Usuario;
import com.donaton.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public Usuario crear(@Valid @RequestBody UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setPassword(dto.getPassword());

        // Asignar rol automáticamente según dominio del correo
        String correo = dto.getCorreo().trim().toLowerCase();
        if (correo.endsWith("@mun.cl")) {
            usuario.setRol(Rol.MUNICIPALIDAD);
        } else if (correo.endsWith("@logistica.cl")) {
            usuario.setRol(Rol.LOGISTICA);
        } else {
            usuario.setRol(Rol.USUARIO); // rol por defecto
        }

        return service.guardar(usuario);
    }

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }
}
