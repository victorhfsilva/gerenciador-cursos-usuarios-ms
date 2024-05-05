package com.example.usuariosms.service;

import com.example.usuariosms.model.dto.UsuarioRequest;
import com.example.usuariosms.model.resources.UsuarioResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface IUsuarioService {
    UsuarioResource save(UsuarioRequest usuarioRequest);
    UsuarioResource findById(UUID id);
    Page<UsuarioResource> findAll(Pageable pageable);
    UsuarioResource update(UUID id, UsuarioRequest usuarioRequest);
    UsuarioResource delete(UUID id);
}