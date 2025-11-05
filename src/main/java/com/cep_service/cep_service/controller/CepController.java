package com.cep_service.cep_service.controller;

import com.cep_service.cep_service.domain.cep.DadosDetalharCep;
import com.cep_service.cep_service.domain.cep.DadosSalvarCep;
import com.cep_service.cep_service.service.CepService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<DadosDetalharCep> avewCep(@RequestBody DadosSalvarCep dados, UriComponentsBuilder uriBuilder) {
        var cep = cepService.salvar(dados);
        var uri = uriBuilder.path("/ceps/{id}").buildAndExpand(cep.id()).toUri();
        return ResponseEntity.created(uri).body(cep);
    }
}
