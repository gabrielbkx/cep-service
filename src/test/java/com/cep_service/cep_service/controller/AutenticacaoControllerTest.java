package com.cep_service.cep_service.controller;


import com.cep_service.cep_service.domain.usuario.Usuario;
import com.cep_service.cep_service.domain.usuario.UsuarioRepository;
import com.cep_service.cep_service.domain.usuario.dto.DadosAutenticacao;
import com.cep_service.cep_service.domain.usuario.dto.DadosCadastro;
import com.cep_service.cep_service.infra.security.AutenticacaoService;
import com.cep_service.cep_service.infra.security.TokenService;
import com.cep_service.cep_service.domain.cep.exceptions.DadosJaExistenteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
 Testes unitários para AutenticacaoController.
 - Usa Mockito para simular dependências.
 - Invoca os métodos do controller diretamente (sem MockMvc) para verificar comportamento/fluxos.
 - Cada método cobre um cenário comentado.
*/

@WebMvcTest(AutenticacaoController.class)
@AutoConfigureMockMvc(addFilters = false) // Ignora filtros de segurança para focar na lógica do Controller
public class AutenticacaoControllerTest {

    @Autowired
    private AutenticacaoController controller; // Injetamos o controller para testes diretos de unidade se necessário

    // Mocks geridos pelo Spring Context (@MockitoBean)
    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private AutenticacaoService autenticacaoService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    // Teste 1: Cadastro com Sucesso
    @Test
    public void cadastrar_quandoSucesso_deveRetornarCreatedComDetalhes() {
        // Arrange
        DadosCadastro dados = new DadosCadastro("novoUsuario", "novo@email.com", "senha123");
        when(usuarioRepository.existsByEmail(dados.email())).thenReturn(false);
        when(usuarioRepository.existsByUsuario(dados.usuario())).thenReturn(false);
        when(passwordEncoder.encode(dados.senha())).thenReturn("senhaCriptografada");

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(100L); // Simulamos o ID gerado pelo banco
            return u;
        });

        // Act
        ResponseEntity<?> resposta = controller.cadastrar(dados);

        // Assert
        assertEquals(201, resposta.getStatusCodeValue(), "Deve retornar 201 Created");
        assertNotNull(resposta.getBody(), "Corpo não deve ser nulo");
        // Verifica se chamou o save
        verify(usuarioRepository).save(any(Usuario.class));
    }

    // Teste 2: Cadastro com Email Duplicado
    @Test
    public void cadastrar_quandoEmailExistente_deveLancarDadosJaExistenteException() {
        DadosCadastro dados = new DadosCadastro("qualquer", "existente@email.com", "senha");
        when(usuarioRepository.existsByEmail(dados.email())).thenReturn(true);

        assertThrows(DadosJaExistenteException.class, () -> controller.cadastrar(dados));
        verify(usuarioRepository, never()).save(any());
    }

    // Teste 3: Cadastro com Usuário Duplicado
    @Test
    public void cadastrar_quandoUsuarioExistente_deveLancarDadosJaExistenteException() {
        DadosCadastro dados = new DadosCadastro("usuarioExistente", "novo@email.com", "senha");
        when(usuarioRepository.existsByEmail(dados.email())).thenReturn(false);
        when(usuarioRepository.existsByUsuario(dados.usuario())).thenReturn(true);

        assertThrows(DadosJaExistenteException.class, () -> controller.cadastrar(dados));
        verify(usuarioRepository, never()).save(any());
    }

    // Teste 4: Autenticação com Sucesso
    @Test
    public void autenticar_quandoCredenciaisValidas_deveRetornarToken() {
        DadosAutenticacao dados = new DadosAutenticacao("usuarioLogin", "senhaLogin");

        Usuario usuario = new Usuario();
        usuario.setUsuario("usuarioLogin");
        usuario.setEmail("u@ex.com");
        usuario.setId(50L);

        // Simulamos o retorno do AuthenticationManager
        UsernamePasswordAuthenticationToken authResult =
                new UsernamePasswordAuthenticationToken(usuario, null, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authResult);

        when(tokenService.gerarToken(usuario)).thenReturn("token-jwt-gerado");

        ResponseEntity<?> resposta = controller.autenticar(dados);

        assertEquals(200, resposta.getStatusCodeValue());
        verify(tokenService).gerarToken(usuario);
        String bodyStr = resposta.getBody().toString();
        assertTrue(bodyStr.contains("token") || bodyStr.contains("jwt") || bodyStr.contains("token-jwt-gerado"));
    }

    // Teste 5: Autenticação Falha
    @Test
    public void autenticar_quandoCredenciaisInvalidas_deveLancarBadCredentialsException() {
        DadosAutenticacao dados = new DadosAutenticacao("usuarioLogin", "senhaInvalida");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        assertThrows(BadCredentialsException.class, () -> controller.autenticar(dados));
        verify(tokenService, never()).gerarToken(any());
    }
}