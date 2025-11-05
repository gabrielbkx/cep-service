package com.cep_service.cep_service.domain.usuario.dto;

import jakarta.validation.constraints.NotNull;

public record DadosAutenticacao(@NotNull
                                 String login,
                                @NotNull
                                 String senha) {
}
