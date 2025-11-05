package com.cep_service.cep_service.service;

import com.cep_service.cep_service.domain.cep.Cep;
import com.cep_service.cep_service.domain.cep.DadosDetalharCep;
import com.cep_service.cep_service.domain.cep.DadosSalvarCep;
import com.cep_service.cep_service.repositpry.CepRepository;
import org.springframework.stereotype.Service;

@Service
public class CepService {

    private final CepRepository cepRepository;

    public CepService(CepRepository cepRepository) {
        this.cepRepository = cepRepository;
    }

    public DadosDetalharCep salvar(DadosSalvarCep dados) {

        // Consicional para verificar se o CEP já existe
        if (cepRepository.existsByNumeroCep(dados.numeroCep())) {
            throw new IllegalArgumentException("Esse CEP já existe no sistema.");
        }

        Cep cep = new Cep(dados);
        var cepSalvo = cepRepository.save(cep);
        return new DadosDetalharCep(cepSalvo);
    }
}
