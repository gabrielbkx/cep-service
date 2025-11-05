package com.cep_service.cep_service.service;

import com.cep_service.cep_service.domain.cep.Cep;
import com.cep_service.cep_service.domain.cep.DadosDetalharCep;
import com.cep_service.cep_service.domain.cep.DadosSalvarCep;
import com.cep_service.cep_service.domain.cep.DadosatualizarCep;
import com.cep_service.cep_service.exception.CepExistenteException;
import com.cep_service.cep_service.exception.CepNaoExistenteException;
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

       verificarExistenciaCep(dados.numeroCep());// verifica se a o cep ja existe no banco de dados, caso sim
        // retorna uma exceção
        Cep cep = new Cep(dados);
        var cepSalvo = cepRepository.save(cep);
        return new DadosDetalharCep(cepSalvo);
    }

    public DadosDetalharCep atualizar(@Valid DadosatualizarCep dados) {

            boolean cep = cepRepository.existsById(dados.id());

            if (cep){
                Cep CepParaAtualizar = cepRepository.getReferenceById(dados.id());
                CepParaAtualizar.setNumeroCep(dados.numeroCep());
                CepParaAtualizar.setLogradouro(dados.logradouro());
                CepParaAtualizar.setCidade(dados.cidade());
                return new DadosDetalharCep(CepParaAtualizar);
            } else {
                throw new CepNaoExistenteException("O CEP informado não existe no sistema.");
            }
    }

    // verifica se o cep existe no banco de dados
    public void verificarExistenciaCep(String numeroCep) {

        if (cepRepository.existsByNumeroCep(numeroCep)) {
            throw new CepExistenteException("O CEP informado já existe no sistema.");
        }
    }
}
