package com.cep_service.cep_service.domain.cep;

import com.cep_service.cep_service.csv.dto.DadosArquivo;
import com.cep_service.cep_service.domain.cep.dto.DadosatualizarCep;
import com.cep_service.cep_service.domain.cep.dto.DadosDetalharCep;
import com.cep_service.cep_service.domain.cep.dto.DadosSalvarCep;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;

@CrossOrigin(origins = "*")
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

    @GetMapping("/numeroCep/{numeroCep}")
    public ResponseEntity<DadosDetalharCep> buscarPorCep(@PathVariable String numeroCep) {
        var cep = cepService.buscarPorCep(numeroCep);
        return ResponseEntity.ok(cep);
    }

    @GetMapping("/logradouro/{logradouro}")
    public ResponseEntity<List<DadosDetalharCep>> buscarPorLogradouro(@PathVariable String logradouro) {
        var ceps = cepService.buscarPorLogradouro(logradouro);
        return ResponseEntity.ok(ceps);
    }

    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<List<DadosDetalharCep>> buscarPorCidade(@PathVariable String cidade) {
        var ceps = cepService.buscarPorCidade(cidade);
        return ResponseEntity.ok(ceps);
    }

    // Admins abaixo
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")// Apenas administradores podem deletar ceps
    @Transactional
    @DeleteMapping({"/{id}"})
    public ResponseEntity<ResponseEntity> deletarCep(@PathVariable Long id) {
        cepService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")// Apenas administradores podem fazer upload de ceps
    @PostMapping(value = "/import",consumes = {"Multipart/form-data"})
    public ResponseEntity<DadosArquivo> uploadCeps(@RequestPart("arquivo") MultipartFile arquivo) {

        cepService.validarCsv(arquivo);
        return null;
    }

}
