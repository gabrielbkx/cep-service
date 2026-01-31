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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
 Testes unitários para AutenticacaoController.
 - Usa Mockito para simular dependências.
 - Invoca os métodos do controller diretamente (sem MockMvc) para verificar comportamento/fluxos.
 - Cada método cobre um cenário comentado.
*/
public class AutenticacaoControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AutenticacaoService autenticacaoService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AutenticacaoController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void cadastrar_quandoSucesso_deveRetornarCreatedComDetalhes() {
        // Arrange: prepara dados e mocks (email e usuario não existem)
        DadosCadastro dados = new DadosCadastro("novoUsuario", "novo@email.com", "senha123");
        when(usuarioRepository.existsByEmail(dados.email())).thenReturn(false);
        when(usuarioRepository.existsByUsuario(dados.usuario())).thenReturn(false);
        when(passwordEncoder.encode(dados.senha())).thenReturn("senhaCriptografada");

        // Mock do save: retorna usuário com id preenchido
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(100L);
            return u;
        });

        // Act
        ResponseEntity<?> resposta = controller.cadastrar(dados);

        // Assert: status 201 Created e corpo do tipo DadosDetalhamentoDeUsuario
        assertEquals(201, resposta.getStatusCodeValue(), "Deve retornar 201 Created");
        assertNotNull(resposta.getBody(), "Corpo não deve ser nulo");
        assertTrue(resposta.getBody().getClass().getSimpleName().toLowerCase().contains("detalhamento"),
                "Corpo deve ser um DTO de detalhamento de usuário");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    public void cadastrar_quandoEmailExistente_deveLancarDadosJaExistenteException() {
        // Arrange: email já existe
        DadosCadastro dados = new DadosCadastro("qualquer", "existente@email.com", "senha");
        when(usuarioRepository.existsByEmail(dados.email())).thenReturn(true);

        // Act & Assert: exceção esperada
        assertThrows(DadosJaExistenteException.class, () -> controller.cadastrar(dados));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void cadastrar_quandoUsuarioExistente_deveLancarDadosJaExistenteException() {
        // Arrange: usuário já existe (email não existe)
        DadosCadastro dados = new DadosCadastro("usuarioExistente", "novo@email.com", "senha");
        when(usuarioRepository.existsByEmail(dados.email())).thenReturn(false);
        when(usuarioRepository.existsByUsuario(dados.usuario())).thenReturn(true);

        // Act & Assert
        assertThrows(DadosJaExistenteException.class, () -> controller.cadastrar(dados));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void autenticar_quandoCredenciaisValidas_deveRetornarToken() {
        // Arrange: prepara dados de autenticação e mocks para autenticação bem sucedida
        DadosAutenticacao dados = new DadosAutenticacao("usuarioLogin", "senhaLogin");

        // Cria um Usuario que será o principal do Authentication
        Usuario usuario = new Usuario();
        usuario.setUsuario("usuarioLogin");
        usuario.setEmail("u@ex.com");
        usuario.setId(50L);

        // Cria um Authentication autenticado com principal = Usuario
        UsernamePasswordAuthenticationToken authResult =
                new UsernamePasswordAuthenticationToken(usuario, null, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authResult);

        when(tokenService.gerarToken(usuario)).thenReturn("token-jwt-gerado");

        // Act
        ResponseEntity<?> resposta = controller.autenticar(dados);

        // Assert: 200 OK e token gerado (verifica que tokenService foi chamado com o usuário principal)
        assertEquals(200, resposta.getStatusCodeValue(), "Deve retornar 200 OK");
        verify(tokenService).gerarToken(usuario);
        assertNotNull(resposta.getBody(), "Corpo da resposta não deve ser nulo");
        String bodyStr = resposta.getBody().toString();
        assertTrue(bodyStr.contains("token") || bodyStr.contains("Token") || bodyStr.contains("jwt"),
                "Corpo deve conter o token gerado");
    }

    @Test
    public void autenticar_quandoCredenciaisInvalidas_deveLancarBadCredentialsException() {
        // Arrange: autenticação falha (credenciais inválidas)
        DadosAutenticacao dados = new DadosAutenticacao("usuarioLogin", "senhaInvalida");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> controller.autenticar(dados));
        verify(tokenService, never()).gerarToken(any());
    }
}