package com.cep_service.cep_service.config;

import com.cep_service.cep_service.domain.usuario.Usuario;
import com.cep_service.cep_service.domain.usuario.UsuarioRepository;
import com.cep_service.cep_service.domain.usuario.enums.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {


    private final PasswordEncoder passwordEncoder;

    private final UsuarioRepository usuarioRepository;

    public AdminSeeder(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        boolean usuarioExiste = usuarioRepository.existsByUsuario("admin");

        if (usuarioExiste) {
            return;
        }

        var usuarioAdmin = new Usuario("admin", "testadoradmin@gmail.com", "admin");
        usuarioAdmin.setSenha(passwordEncoder.encode(usuarioAdmin.getSenha()));
        usuarioAdmin.setRole(UserRole.ROLE_ADMIN);
        usuarioRepository.save(usuarioAdmin);
    }

}
