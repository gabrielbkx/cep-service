package com.cep_service.cep_service.infra.security.exceptions;

import com.cep_service.cep_service.infra.exceptions.dto.DadosErro;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;


@Component
public class TratadorDeAcessoNegado implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 1. Carimbamos o envelope (Status 403 e Tipo JSON)
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        // 2. Escrevemos a carta
        DadosErro dadosErro = new DadosErro("Você nao tem permissão para acessar este recurso.",
                HttpStatus.FORBIDDEN.value());

        // 3. Convertemos para texto e enviamos
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), dadosErro);

    }
}
