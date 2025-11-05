package com.cep_service.cep_service.domain.cep;


import jakarta.validation.constraints.Pattern;

public record DadosSalvarCep(
        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 números")
        String numeroCep,
        String logradouro,
        String cidade) {

}
