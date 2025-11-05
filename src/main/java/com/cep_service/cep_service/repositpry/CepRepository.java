package com.cep_service.cep_service.repositpry;

import com.cep_service.cep_service.domain.cep.Cep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CepRepository extends JpaRepository<Cep, Long> {
    boolean existsByNumeroCep(String numeroCep);
    boolean existsById(Long id);
}
