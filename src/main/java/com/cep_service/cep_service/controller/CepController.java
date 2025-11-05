package com.cep_service.cep_service.controller;

import com.cep_service.cep_service.domain.cep.DadosatualizarCep;
import com.cep_service.cep_service.domain.cep.DadosDetalharCep;
import com.cep_service.cep_service.domain.cep.DadosSalvarCep;
import com.cep_service.cep_service.service.CepService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cep")
public class CepController {

    private final CepService cepService;

    public CepController(CepService cepService) {
        this.cepService = cepService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalharCep> salvarCep(@RequestBody @Valid DadosSalvarCep dados, UriComponentsBuilder uriBuilder) {
        var cep = cepService.salvar(dados);
        var uri = uriBuilder.path("/cep/{id}").buildAndExpand(cep.id()).toUri();
        return ResponseEntity.created(uri).body(cep);
    }

    @Transactional
    @PutMapping
    public ResponseEntity<DadosDetalharCep> atualizar(@RequestBody @Valid DadosatualizarCep dados) {
        var cepAtualizado = cepService.atualizar(dados);
        return ResponseEntity.ok(cepAtualizado);
    }
}
