package com.cep_service.cep_service.domain.usuario.dto;

import com.cep_service.cep_service.domain.usuario.Usuario;

public record DadosDetalhamentoDeUsuario(Long id, String usuario) {

    public DadosDetalhamentoDeUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getUsuario());
    }
}
