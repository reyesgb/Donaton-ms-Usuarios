package com.donaton.usuarios.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String correo;

    private String password;

    private String organizacion; //Se usa mas adelante para poder implementarlo en el frontend

    @Enumerated(EnumType.STRING)
    private Rol rol;

}