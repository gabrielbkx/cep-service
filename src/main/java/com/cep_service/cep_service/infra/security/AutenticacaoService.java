package com.cep_service.cep_service.infra.security;

import com.cep_service.cep_service.domain.usuario.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AutenticacaoService implements UserDetailsService {

    UsuarioRepository usuarioRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {

        return usuarioRepository.findByUsuarioOrEmail(usuario,usuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário ou Email não encontrad" + usuario));
        // criar uma exceção personalizada depois
    }



}
