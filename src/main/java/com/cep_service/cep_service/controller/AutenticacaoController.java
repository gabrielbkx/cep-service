package com.cep_service.cep_service.controller;

import com.cep_service.cep_service.domain.usuario.AutenticacaoService;
import com.cep_service.cep_service.domain.usuario.Usuario;
import com.cep_service.cep_service.domain.usuario.dto.DadosAutenticacao;
import com.cep_service.cep_service.domain.usuario.dto.DadosTokenJWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {


    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @PostMapping
    ResponseEntity<?> autenticar(@RequestBody @Valid DadosAutenticacao dadosAutenticacao) {

        UsernamePasswordAuthenticationToken uPA = new UsernamePasswordAuthenticationToken(
                dadosAutenticacao.usuario(),
                dadosAutenticacao.senha()
        );

        var usuarioAutenticado = authenticationManager.authenticate(uPA);
        var usuario =  (Usuario) usuarioAutenticado.getPrincipal();
        var token = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(new DadosTokenJWT(token));
    }
}
