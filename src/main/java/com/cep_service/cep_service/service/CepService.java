package com.cep_service.cep_service.service;

import com.cep_service.cep_service.domain.cep.Cep;
import com.cep_service.cep_service.domain.cep.DadosDetalharCep;
import com.cep_service.cep_service.domain.cep.DadosSalvarCep;
import com.cep_service.cep_service.domain.cep.DadosatualizarCep;
import com.cep_service.cep_service.repositpry.CepRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class CepService {

    private final CepRepository cepRepository;

    public CepService(CepRepository cepRepository) {
        this.cepRepository = cepRepository;
    }

    public DadosDetalharCep salvar(DadosSalvarCep dados) {

        if (verificarExistenciaCep(dados.numeroCep()) == true) {

            throw new IllegalArgumentException("O CEP informado já existe no sistema.");
        }

        Cep cep = new Cep(dados);
        var cepSalvo = cepRepository.save(cep);
        return new DadosDetalharCep(cepSalvo);
    }

    public DadosDetalharCep atualizar(@Valid DadosatualizarCep dados) {

       if (verificarExistenciaCep(dados.numeroCep())){
            Cep cep = cepRepository.getReferenceById(dados.id());
            cepRepository.save(cep);
            return new DadosDetalharCep(cep);
        }
        throw new IllegalArgumentException("Não foi possível atualizar o CEP pois ele não existe no sistema.");
    }

    // verifica se o cep existe no banco de dados
    public boolean verificarExistenciaCep(String numeroCep) {

        if (cepRepository.existsByNumeroCep(numeroCep)) {
            return true;
        }else {
            throw new IllegalArgumentException("O CEP informado não existe no sistema.");
        }
    }
}
