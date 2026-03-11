package com.cep_service.cep_service.domain.cep.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosatualizarCep(

        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 números")
        String numeroCep,
        String logradouro,
        String cidade) {
}
