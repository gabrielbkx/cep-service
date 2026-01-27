package com.cep_service.cep_service.domain.usuario.dto;

import com.cep_service.cep_service.domain.usuario.Usuario;

public record DadosCadastro(String usuario, String email, String senha) {

    public DadosCadastro(Usuario usuario) {
        this(usuario.getUsuario(), usuario.getEmail(), usuario.getSenha());
    }
}
