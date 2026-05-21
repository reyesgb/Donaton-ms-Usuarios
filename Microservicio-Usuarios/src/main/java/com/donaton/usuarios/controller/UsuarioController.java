package com.donaton.usuarios.controller;

import com.donaton.usuarios.dto.UsuarioDTO;
import com.donaton.usuarios.model.Rol;
import com.donaton.usuarios.model.Usuario;
import com.donaton.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

        try {
            String rolStr = dto.getRol() == null ? "" : dto.getRol().trim().toUpperCase();
            usuario.setRol(Rol.valueOf(rolStr));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Rol inválido: " + dto.getRol() + ". Valores permitidos: " + java.util.Arrays.toString(Rol.values()));
        }

        return service.guardar(usuario);
    }

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }
}
