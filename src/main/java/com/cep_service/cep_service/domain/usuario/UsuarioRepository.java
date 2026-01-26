package com.cep_service.cep_service.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsuarioOrEmail(String usuario,String email);
    boolean existsByUsuario(String usuario);
    boolean existsByEmail(String email);
}
