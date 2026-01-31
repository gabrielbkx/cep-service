package com.cep_service.cep_service.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cep_service.cep_service.domain.usuario.Usuario;
import com.cep_service.cep_service.domain.usuario.enums.UserRole;
import com.cep_service.cep_service.infra.security.TokenService;
import com.cep_service.cep_service.infra.security.exceptions.TokenInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

/*
 Classe de testes unitários para TokenService.
 Cada método de teste cobre um cenário descrito acima.
 É usado ReflectionTestUtils para injetar o secret no bean TokenService,
 e a biblioteca com.auth0.jwt para criar tokens com diferentes características.
*/
public class TokenServiceTest {

    private static final String SEGREDO = "teste-secreto-123";
    private static final String OUTRO_SEGREDO = "outro-secreto-456";

    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        // Instancia o serviço e injeta o secret (substitui o @Value)
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", SEGREDO);
    }

    @Test
    public void gerarToken_usuarioValido_deveGerarTokenComClaimsCorretos() {
        // 1. Arrange: Preparamos um usuário fictício
        Usuario usuario = new Usuario();
        usuario.setUsuario("joao.silva");

        // --- AQUI ESTÁ A LINHA NOVA ---
        // Definimos a Role para evitar o NullPointerException
        // (Certifique-se de usar o valor do seu Enum, ex: UserRole.USER ou "ROLE_USUARIO")
        usuario.setRole(UserRole.ROLE_USUARIO);
        // ------------------------------

        // 2. Act: Chamamos a fábrica
        String tokenGerado = tokenService.gerarToken(usuario);

        // 3. Assert: Verificamos o resultado
        assertNotNull(tokenGerado, "O token não deve ser nulo");
        assertFalse(tokenGerado.isEmpty(), "O token não deve estar vazio");

        // A "Auditoria": Usamos o verificador para abrir o token que acabou de ser gerado
        var decoded = JWT.require(Algorithm.HMAC256(SEGREDO))
                .withIssuer("cep-service")
                .build()
                .verify(tokenGerado);

        // Conferimos se o recheio do envelope está certo
        assertEquals("joao.silva", decoded.getSubject(), "O subject (dono) do token deve ser o login do usuário");
        // Verifica se a data de expiração existe e é futura
        assertTrue(decoded.getExpiresAt().toInstant().isAfter(Instant.now()), "O token deve expirar no futuro");
    }

    @Test
    public void validarToken_tokenValido_deveRetornarDecodedJWT() {
        // Cria um token válido com issuer, subject, claim role e expiração futura
        String subject = "usuario-teste";
        String role = "ROLE_USER";
        String token = JWT.create()
                .withIssuer("cep-service")
                .withSubject(subject)
                .withClaim("role", role)
                .withExpiresAt(Instant.now().plusSeconds(3600))
                .sign(Algorithm.HMAC256(SEGREDO));

        // Chama o serviço e verifica claims básicos
        var decoded = tokenService.validarToken(token);
        assertNotNull(decoded, "DecodedJWT não deve ser nulo para token válido");
        assertEquals("cep-service", decoded.getIssuer(), "Issuer deve corresponder");
        assertEquals(subject, decoded.getSubject(), "Subject deve corresponder");
        assertEquals(role, decoded.getClaim("role").asString(), "Claim role deve corresponder");
        assertTrue(decoded.getExpiresAt().toInstant().isAfter(Instant.now()), "Token deve ter expiração no futuro");
    }

    @Test
    public void validarToken_tokenExpirado_deveLancarTokenInvalidoException() {
        // Gera token com expiração passada
        String token = JWT.create()
                .withIssuer("cep-service")
                .withSubject("usuario-expirado")
                .withClaim("role", "ROLE_USER")
                .withExpiresAt(Instant.now().minusSeconds(10))
                .sign(Algorithm.HMAC256(SEGREDO));

        // Espera TokenInvalidoException conforme implementação do serviço
        assertThrows(TokenInvalidoException.class, () -> tokenService.validarToken(token));
    }

    @Test
    public void validarToken_assinaturaInvalida_deveLancarTokenInvalidoException() {
        // Gera token assinado com outro secret (assinatura inválida para o serviço)
        String token = JWT.create()
                .withIssuer("cep-service")
                .withSubject("usuario")
                .withClaim("role", "ROLE_USER")
                .withExpiresAt(Instant.now().plusSeconds(3600))
                .sign(Algorithm.HMAC256(OUTRO_SEGREDO));

        // Espera TokenInvalidoException por falha na verificação de assinatura
        assertThrows(TokenInvalidoException.class, () -> tokenService.validarToken(token));
    }

    @Test
    public void validarToken_tokenMalformado_deveLancarTokenInvalidoException() {
        // Token com formato inválido (não JWT)
        String malformed = "nao.um.token.jwt";
        assertThrows(TokenInvalidoException.class, () -> tokenService.validarToken(malformed));
    }

    @Test
    public void validarToken_tokenVazio_deveLancarTokenInvalidoException() {
        // Token vazio deve ser tratado como inválido
        String empty = "";
        assertThrows(TokenInvalidoException.class, () -> tokenService.validarToken(empty));
    }

    @Test
    public void validarToken_tokenNulo_deveLancarExcecao() {
        // Comportamento pode variar conforme a biblioteca; garante que uma exceção é lançada
        assertThrows(Exception.class, () -> tokenService.validarToken(null));
    }
}