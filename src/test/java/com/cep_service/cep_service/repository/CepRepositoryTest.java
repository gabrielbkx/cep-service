package com.cep_service.cep_service.repository;

import com.cep_service.cep_service.domain.cep.Cep;
import com.cep_service.cep_service.domain.cep.CepRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // 1. Configura o H2 e o contexto JPA para testes
class CepRepositoryTest {

    @Autowired
    private TestEntityManager entityManager; // 2. O nosso gestor para inserir dados de teste

    @Autowired
    private CepRepository cepRepository; // 3. O repositório real a ser testado

    @Test
    void deveEncontrarCepComSucesso() {
        // 1. Arrange (Preparar)
        // Criamos um CEP e salvamo-lo no banco de dados em memória (H2)
        String cepOriginal = "28070077";
        Cep novoCep = new Cep(cepOriginal, "Rua Teste", "Campos");
        entityManager.persist(novoCep);

        // 2. Act (Executar)
        // Buscamos pelo método do repositório
        Optional<Cep> resultado = cepRepository.findByNumeroCep(cepOriginal);

        // 3. Assert (Verificar)
        // Confirmamos se foi encontrado e se os dados batem certo
        assertThat(resultado.isPresent()).isTrue();
        assertThat(resultado.get().getNumeroCep()).isEqualTo(cepOriginal);
        assertThat(resultado.get().getCidade()).isEqualTo("Campos");
    }

    @Test
    void deveRetornarVazioQuandoCepNaoExiste() {
        // Teste extra: Garantir que retorna vazio se não existir
        Optional<Cep> resultado = cepRepository.findByNumeroCep("00000000");
        assertThat(resultado).isEmpty();
    }
}