package com.cep_service.cep_service.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsernameOrEmail(String username,String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Usuario> findByUsername(String username);
}
