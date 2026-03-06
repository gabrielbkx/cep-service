package com.cep_service.cep_service.domain.cep;


import com.cep_service.cep_service.domain.cep.dto.DadosDetalharCep;
import com.cep_service.cep_service.domain.cep.dto.DadosSalvarCep;
import com.cep_service.cep_service.domain.cep.dto.DadosatualizarCep;
import com.cep_service.cep_service.domain.cep.exceptions.DadosJaExistenteException;
import com.cep_service.cep_service.domain.cep.exceptions.CepNaoExistenteException;
import com.cep_service.cep_service.domain.cep.mapper.CepMapper;
import com.cep_service.cep_service.excel.ExcelProcessadorService;
import com.cep_service.cep_service.excel.exception.ExcelProcessamentoException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;


@Service
public class CepService {

    private final CepRepository cepRepository;
    private final ExcelProcessadorService excelProcessadorService;
    private final CepMapper mapper;


    public CepService(CepRepository cepRepository, ExcelProcessadorService excelProcessadorService, CepMapper mapper) {
        this.cepRepository = cepRepository;
        this.excelProcessadorService = excelProcessadorService;
        this.mapper = mapper;
    }

    @Transactional
    public DadosDetalharCep salvar(DadosSalvarCep dados) {

        // Verifica se o CEP já existe no sistema, caso exista lança uma exceção peresonalizada
        if (cepRepository.existsByNumeroCep(dados.numeroCep())) {
            throw new DadosJaExistenteException("O CEP informado já existe no sistema.");
        }

        return mapper.paraDto(cepRepository.save(mapper.paraEntidade(dados)));
    }

    @Transactional
    public List<DadosDetalharCep> salvarLista(List<DadosSalvarCep> dadosList) {

        // Valida se a lista não está vazia
        if (dadosList == null || dadosList.isEmpty()) {
            throw new IllegalArgumentException("A lista de CEPs não pode estar vazia");
        }

        // Verifica se algum dos CEPs já existe no sistema
        for (DadosSalvarCep dados : dadosList) {
            if (cepRepository.existsByNumeroCep(dados.numeroCep())) {
                throw new DadosJaExistenteException("O CEP " + dados.numeroCep() + " já existe no sistema.");
            }
        }

        // Converte a lista de DTOs para entidades
        List<Cep> ceps = dadosList.stream()
                .map(Cep::new)
                .toList();

        // Salva todos os CEPs usando saveAll
        var cepsSalvos = cepRepository.saveAll(ceps);

        // Converte as entidades salvas para DTOs de resposta
        return cepsSalvos.stream()
                .map(DadosDetalharCep::new)
                .toList();
    }

    @Transactional
    public DadosDetalharCep atualizar(@Valid DadosatualizarCep dados, Long cepId) {

            boolean cep = cepRepository.existsById(cepId);

            if (cep){
                Cep cepParaAtualizar = cepRepository.getReferenceById(cepId);

                mapper.atualizarCep(dados, cepParaAtualizar);
                return mapper.paraDto(cepRepository.save(cepParaAtualizar));

            } else {
                throw new CepNaoExistenteException("O CEP informado não existe no sistema.");
            }
    }


    @Transactional(readOnly = true)
    public DadosDetalharCep buscarPorCep(String numeroCep) {

        //se o cep existir ele vem no retorno, caso nao exista lança minha exceção
        var cep = cepRepository.findByNumeroCep(numeroCep)
                .orElseThrow(() -> new CepNaoExistenteException("CEP não encontrado"));
        return new DadosDetalharCep(cep);
    }


    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public List<DadosDetalharCep> buscarPorCidade(String cidade) {
        var ceps = cepRepository.findByCidade(cidade);       // busca uma lista de ceps pelo logradouro

        if (ceps.isEmpty()) {
            throw new IllegalArgumentException("Nenhum CEP encontrado para a cidade: " + cidade);
        }// caso esteja vazia lança uma exceção

        return ceps.stream().map(DadosDetalharCep::new).toList();
    }

    public byte [] exportar(){

        try {
           return excelProcessadorService.exportarCepsViaExcell();
        } catch (IOException e) {
            throw new ExcelProcessamentoException(e.getMessage());
        }
    }


    @Transactional
    public void deletar(Long id) {

        if (cepRepository.existsById(id)) {

            cepRepository.deleteById(id);

        } else {
            throw new CepNaoExistenteException("O CEP informado não existe no sistema.");
        }
    }

}

