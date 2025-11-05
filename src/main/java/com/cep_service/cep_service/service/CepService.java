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

        // Verifica se o CEP já existe no sistema, caso exista lança uma exceção peresonalizada
        if (cepRepository.existsByNumeroCep(dados.numeroCep())) {
            throw new CepExistenteException("O CEP informado já existe no sistema.");
        }

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

    public void deletar(Long id) {

        if (cepRepository.existsById(id)) {

            cepRepository.deleteById(id);

        } else {
            throw new CepNaoExistenteException("O CEP informado não existe no sistema.");
        }
    }
}
