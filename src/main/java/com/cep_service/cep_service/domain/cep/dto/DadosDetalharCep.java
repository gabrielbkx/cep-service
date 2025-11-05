package com.cep_service.cep_service.domain.cep.dto;

import com.cep_service.cep_service.domain.cep.Cep;

// para evitar trabalhar diretamente com a entidade criei dtos
public record DadosDetalharCep(Long id,String numeroCep, String logradouro, String cidade) {

    public DadosDetalharCep(Cep cep) {
        this(cep.getId(), cep.getNumeroCep(), cep.getLogradouro(), cep.getCidade());
    }

}
