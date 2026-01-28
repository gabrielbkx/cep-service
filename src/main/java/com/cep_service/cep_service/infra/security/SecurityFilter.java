package com.cep_service.cep_service.infra.security;

import com.cep_service.cep_service.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            // O split cria um array: ["Bearer", "eyJhb..."]
            // Pegamos a posição 1, que é o token
            var token = authorizationHeader.split(" ")[1];

            System.out.println("Token limpo: " + token);

            var login = tokenService.validarToken(token);

            var usuario = usuarioRepository.findByUsuario(login).orElseThrow();

            var authentication = new UsernamePasswordAuthenticationToken(
                    usuario,
                    usuario.getRole(),
                    usuario.getAuthorities());

            // O segurança "salva" o crachá no sistema da festa
            SecurityContextHolder.getContext().setAuthentication(authentication);


        }
        // O segurança libera a passagem para o próximo passo
        filterChain.doFilter(request, response);
    }
}
