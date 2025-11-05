package com.cep_service.cep_service.domain.cep;

import jakarta.validation.Valid;

public record DadosSalvarCep(
        @Valid
        String numeroCep,
        String logradouro,
        String cidade) {

}
