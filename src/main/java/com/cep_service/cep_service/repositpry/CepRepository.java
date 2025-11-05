package com.cep_service.cep_service.repositpry;

import com.cep_service.cep_service.domain.cep.Cep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CepRepository extends JpaRepository<Cep, Long> {

    boolean existsByNumeroCep(String numeroCep);
    boolean existsById(Long id);

    // Buscar pelo número do Cep
    @Query("SELECT c FROM Cep c WHERE c.numeroCep = :numeroCep")
   Optional<Cep> findByNumeroCep(String numeroCep);

    // Buscar pelo nome da cidade
    @Query("SELECT c FROM Cep c WHERE LOWER(c.cidade) LIKE LOWER(CONCAT('%', :cidade, '%'))")
    List<Cep> findByCidade(@Param("cidade") String cidade);

    // Buscar pelo nome do logradouro
    @Query("SELECT c FROM Cep c WHERE LOWER(c.logradouro) LIKE LOWER(CONCAT('%', :logradouro, '%'))")
    List<Cep> findByLogradouro(@Param("logradouro") String logradouro);

}
