package com.cep_service.cep_service.repository;


import com.cep_service.cep_service.domain.usuario.Usuario;
import com.cep_service.cep_service.domain.usuario.UsuarioRepository;
import com.cep_service.cep_service.domain.usuario.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/*
 Testes unitários (slice) para UsuarioRepository.
 Usa @DataJpaTest para carregar apenas a camada de persistência (H2 por padrão).
 Cada método testa um cenário específico e está comentado.
*/
@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioA;
    private Usuario usuarioB;

    @BeforeEach
    public void setUp() {
        // Criação de instâncias de usuário reutilizáveis para os testes.
        // Usa o construtor presente no projeto (usuario, email, senha).
        usuarioA = new Usuario("user_a", "user_a@example.com", "senhaA");
        usuarioA.setRole(UserRole.ROLE_USUARIO); // evita NullPointer em validações/constraints

        usuarioB = new Usuario("user_b", "user_b@example.com", "senhaB");
        usuarioB.setRole(UserRole.ROLE_USUARIO);
    }

    @Test
    @DisplayName("Salvar usuário -> deve persistir e atribuir id")
    public void salvarUsuario_devePersistirEGerarId() {
        // Arrange & Act: salva o usuário
        var salvo = usuarioRepository.save(usuarioA);

        // Assert: id gerado e dados persistidos
        assertNotNull(salvo.getId(), "ID deve ser gerado ao persistir");
        assertEquals("user_a", salvo.getUsuario(), "usuario salvo deve manter o campo usuario");
        assertEquals("user_a@example.com", salvo.getEmail(), "usuario salvo deve manter o campo email");
    }

    @Test
    @DisplayName("existsByUsuario -> true quando usuário existe")
    public void existsByUsuario_deveRetornarTrueQuandoExistir() {
        // Arranje: persiste um usuário
        usuarioRepository.save(usuarioA);

        // Act & Assert: método de existência retorna true
        assertTrue(usuarioRepository.existsByUsuario("user_a"), "existsByUsuario deve retornar true para usuário persistido");
    }

    @Test
    @DisplayName("existsByUsuario -> false quando usuário não existe")
    public void existsByUsuario_deveRetornarFalseQuandoNaoExistir() {
        // Sem persistência: deve ser false
        assertFalse(usuarioRepository.existsByUsuario("nao_existe"), "existsByUsuario deve retornar false para usuário inexistente");
    }

    @Test
    @DisplayName("existsByEmail -> true quando email existe")
    public void existsByEmail_deveRetornarTrueQuandoExistir() {
        // Persiste e verifica
        usuarioRepository.save(usuarioA);
        assertTrue(usuarioRepository.existsByEmail("user_a@example.com"), "existsByEmail deve retornar true para email persistido");
    }

    @Test
    @DisplayName("existsByEmail -> false quando email não existe")
    public void existsByEmail_deveRetornarFalseQuandoNaoExistir() {
        assertFalse(usuarioRepository.existsByEmail("nao@existe.com"), "existsByEmail deve retornar false para email inexistente");
    }

    @Test
    @DisplayName("findByUsuario -> retorna Optional com usuário quando existe")
    public void findByUsuario_deveRetornarUsuarioQuandoExistir() {
        // Persiste e recupera
        usuarioRepository.save(usuarioA);
        Optional<Usuario> encontrado = usuarioRepository.findByUsuario("user_a");

        assertTrue(encontrado.isPresent(), "findByUsuario deve retornar Optional presente para usuário existente");
        assertEquals("user_a@example.com", encontrado.get().getEmail(), "Email do usuário encontrado deve corresponder");
    }

    @Test
    @DisplayName("findByUsuario -> retorna Optional vazio quando não existe")
    public void findByUsuario_deveRetornarVazioQuandoNaoExistir() {
        Optional<Usuario> encontrado = usuarioRepository.findByUsuario("inexistente");
        assertTrue(encontrado.isEmpty(), "findByUsuario deve retornar Optional vazio para usuário inexistente");
    }

    @Test
    @DisplayName("findByUsuarioOrEmail -> encontra por usuario")
    public void findByUsuarioOrEmail_encontraPorUsuario() {
        // Persiste usuárioA
        usuarioRepository.save(usuarioA);

        // Busca usando usuario (primeiro parâmetro)
        Optional<Usuario> resultado = usuarioRepository.findByUsuarioOrEmail("user_a", "qualquer@dominio.com");

        assertTrue(resultado.isPresent(), "findByUsuarioOrEmail deve encontrar quando usuario corresponder");
        assertEquals("user_a", resultado.get().getUsuario(), "usuario retornado deve ser o esperado");
    }

    @Test
    @DisplayName("findByUsuarioOrEmail -> encontra por email")
    public void findByUsuarioOrEmail_encontraPorEmail() {
        // Persiste usuárioB
        usuarioRepository.save(usuarioB);

        // Busca usando email (segundo parâmetro)
        Optional<Usuario> resultado = usuarioRepository.findByUsuarioOrEmail("nao_existe", "user_b@example.com");

        assertTrue(resultado.isPresent(), "findByUsuarioOrEmail deve encontrar quando email corresponder");
        assertEquals("user_b@example.com", resultado.get().getEmail(), "email retornado deve ser o esperado");
    }

    @Test
    @DisplayName("findByUsuarioOrEmail -> vazio quando nenhum corresponder")
    public void findByUsuarioOrEmail_deveRetornarVazioQuandoNenhumCorresponder() {
        // Persiste apenas usuarioA
        usuarioRepository.save(usuarioA);

        // Busca com valores que não batem
        Optional<Usuario> resultado = usuarioRepository.findByUsuarioOrEmail("outro", "outro@dominio.com");

        assertTrue(resultado.isEmpty(), "findByUsuarioOrEmail deve retornar vazio quando nenhum campo corresponder");
    }
}

