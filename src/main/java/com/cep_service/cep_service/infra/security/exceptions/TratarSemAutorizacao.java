package com.cep_service.cep_service.infra.security.exceptions;

import com.cep_service.cep_service.infra.exceptions.dto.DadosErro;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;


public class TratarSemAutorizacao implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // 1. Carimbamos o envelope (Status 403 e Tipo JSON)
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        // 2. Escrevemos a carta
        DadosErro dadosErro = new DadosErro("Falha na auenticação: Você precisa se " +
                "autenticar para acessar este recurso.",
                HttpStatus.UNAUTHORIZED.value());

        // 3. Convertemos para texto e enviamos
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), dadosErro);

    }
}
