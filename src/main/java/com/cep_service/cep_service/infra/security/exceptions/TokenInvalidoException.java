package com.cep_service.cep_service.infra.security.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;

// Herdamos de JWTVerificationException para manter a semântica, mas com nosso nome
public class TokenInvalidoException extends JWTVerificationException {
    public TokenInvalidoException(String message) {
        super(message);
    }
}