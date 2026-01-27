package com.cep_service.cep_service.controller;

import com.cep_service.cep_service.infra.security.AutenticacaoService;
import com.cep_service.cep_service.domain.usuario.Usuario;
import com.cep_service.cep_service.domain.usuario.UsuarioRepository;
import com.cep_service.cep_service.domain.usuario.dto.DadosAutenticacao;
import com.cep_service.cep_service.domain.usuario.dto.DadosCadastro;
import com.cep_service.cep_service.domain.usuario.dto.DadosDetalhamentoDeUsuario;
import com.cep_service.cep_service.domain.usuario.dto.DadosTokenJWT;
import com.cep_service.cep_service.exception.DadosJaExistenteException;
import com.cep_service.cep_service.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@RequestMapping("/login")
public class AutenticacaoController {


    private UsuarioRepository usuarioRepository;
    private AutenticacaoService autenticacaoService;
    private  AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;


    public AutenticacaoController(UsuarioRepository usuarioRepository,
                                  AutenticacaoService autenticacaoService,
                                  AuthenticationManager authenticationManager, TokenService tokenService,
                                  PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.autenticacaoService = autenticacaoService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<DadosDetalhamentoDeUsuario> cadastrar(@RequestBody @Valid DadosCadastro dadosCadastro) {

        // Verifica se o usuario ja existe pelo email já existe
      if (usuarioRepository.existsByEmail(dadosCadastro.email())){
         throw new DadosJaExistenteException("Email já cadastrado!");
      }

        // Verifica se o usuario ja existe pelo nome de usuario já existe
        if (usuarioRepository.existsByUsuario(dadosCadastro.usuario())){
            throw new DadosJaExistenteException("Nome de usuário já cadastrado!");
        }

        // transformar essas validações em umma classe encadeada depos

        var novoUsuario = new Usuario(
                dadosCadastro.usuario(),
                dadosCadastro.email(),
                passwordEncoder.encode(dadosCadastro.senha())

        );

        usuarioRepository.save(novoUsuario);


        var uri = URI.create("/login/cadastrar/" + novoUsuario.getId());

        return ResponseEntity.created(uri).body(new DadosDetalhamentoDeUsuario(novoUsuario));
    }

    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody @Valid DadosAutenticacao dadosAutenticacao) {

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
