package com.cep_service.cep_service.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cep_service.cep_service.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {

        Algorithm algorithm = Algorithm.HMAC256(secret); // Algoritmo de assinatura do token

        Instant expiracaoToken = Instant.now().plusSeconds(3600); // Define a expiração do token
        // para 1 hora a partir de agora

        return JWT.create()
                .withIssuer("cep-service") // Emissor do token
                .withSubject(usuario.getUsuario()) // Dono do token
                .withExpiresAt(expiracaoToken) // Tempo de expiração do token (1 hora)
                .sign(algorithm);  // Assinatura do token
    }
}
