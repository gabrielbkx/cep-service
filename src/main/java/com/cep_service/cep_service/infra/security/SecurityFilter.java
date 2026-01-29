package com.cep_service.cep_service.infra.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cep_service.cep_service.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

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
            var token = authorizationHeader.split(" ")[1];

            // Validamos o token e pegamos o objeto completo
            var login = tokenService.validarToken(token);

            // Extraímos os dados direto da "pulseira"
            String nomeUsuario = login.getSubject();
            String role = login.getClaim("role").asString();



            // Transformamos a role em uma lista de autoridades
            var authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

            // Criamos o objeto de autenticação SEM ir ao banco
            var authentication = new UsernamePasswordAuthenticationToken(
                    nomeUsuario,
                    null,
                    authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
