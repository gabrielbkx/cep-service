package com.cep_service.cep_service.domain.usuario.dto;

import java.time.Instant;

public record DadosCadastro(String usuario, String email, String senha, Instant dataCadastro) {
}
