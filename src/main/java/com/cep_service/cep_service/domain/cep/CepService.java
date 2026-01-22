package com.cep_service.cep_service.domain.cep;

import com.cep_service.cep_service.domain.cep.dto.DadosDetalharCep;
import com.cep_service.cep_service.domain.cep.dto.DadosSalvarCep;
import com.cep_service.cep_service.domain.cep.dto.DadosatualizarCep;
import com.cep_service.cep_service.exception.CepExistenteException;
import com.cep_service.cep_service.exception.CepNaoExistenteException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public DadosDetalharCep buscarPorCep(String numeroCep) {

        //se o cep existir ele vem no retorno, caso nao exista lança minha exceção
        var cep = cepRepository.findByNumeroCep(numeroCep)
                .orElseThrow(() -> new CepNaoExistenteException("CEP não encontrado"));
        return new DadosDetalharCep(cep);
    }


    public List<DadosDetalharCep> buscarPorLogradouro(String logradouro) {

        // busca uma lista de ceps pelo logradouro
        var ceps = cepRepository.findByLogradouro(logradouro);

        // caso esteja vazia lança uma exceção
        if (ceps.isEmpty()) {
            throw new IllegalArgumentException("Nenhum CEP encontrado para o logradouro: " + logradouro);
        }

        //retorna a nossa lista
        return ceps.stream().map(DadosDetalharCep::new).toList();
    }

    public List<DadosDetalharCep> buscarPorCidade(String cidade) {
        var ceps = cepRepository.findByCidade(cidade);       // busca uma lista de ceps pelo logradouro

        if (ceps.isEmpty()) {
            throw new IllegalArgumentException("Nenhum CEP encontrado para a cidade: " + cidade);
        }// caso esteja vazia lança uma exceção

        return ceps.stream().map(DadosDetalharCep::new).toList();
    }
}
