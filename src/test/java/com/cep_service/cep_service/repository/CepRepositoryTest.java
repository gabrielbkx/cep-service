package com.cep_service.cep_service.repository;

import com.cep_service.cep_service.domain.cep.Cep;
import com.cep_service.cep_service.domain.cep.CepRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CepRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CepRepository cepRepository;

    @Test
    void deveEncontrarCepComSucesso() {

        String cepOriginal = "28070077";
        Cep novoCep = new Cep(cepOriginal, "Rua Teste", "Campos");
        entityManager.persist(novoCep);



        Optional<Cep> resultado = cepRepository.findByNumeroCep(cepOriginal);


        assertThat(resultado.isPresent()).isTrue();
        assertThat(resultado.get().getNumeroCep()).isEqualTo(cepOriginal);
        assertThat(resultado.get().getCidade()).isEqualTo("Campos");
    }

    @Test
    void deveRetornarVazioQuandoCepNaoExiste() {

        Optional<Cep> resultado = cepRepository.findByNumeroCep("00000000");
        assertThat(resultado).isEmpty();
    }
}