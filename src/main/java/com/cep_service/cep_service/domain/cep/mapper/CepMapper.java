package com.cep_service.cep_service.domain.cep.mapper;


import com.cep_service.cep_service.domain.cep.Cep;
import com.cep_service.cep_service.domain.cep.dto.DadosDetalharCep;
import com.cep_service.cep_service.domain.cep.dto.DadosSalvarCep;
import com.cep_service.cep_service.domain.cep.dto.DadosatualizarCep;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CepMapper {


    Cep paraEntidade(DadosSalvarCep dadosSalvarCep);

    DadosDetalharCep paraDto(Cep cep);

    void atualizarCep(DadosatualizarCep cep, @MappingTarget Cep entidade);

}
