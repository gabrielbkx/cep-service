package com.cep_service.cep_service.service;

import com.cep_service.cep_service.domain.cep.Cep;
import com.cep_service.cep_service.domain.cep.dto.DadosDetalharCep;
import com.cep_service.cep_service.exception.CepNaoExistenteException;
import com.cep_service.cep_service.repositpry.CepRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CepServiceTest {

    @Mock
    private CepRepository cepRepository;

    @InjectMocks
    private CepService cepService;

    @Test
    void deveBuscarCepComSucesso() {

        String cepSolicitado = "28070077";
        Cep cepSimulado = new Cep(cepSolicitado, "Rua Teste", "Campos");
        when(cepRepository.findByNumeroCep(cepSolicitado)).thenReturn(Optional.of(cepSimulado));


        DadosDetalharCep resultado = cepService.buscarPorCep(cepSolicitado);


        assertNotNull(resultado, "O resultado não deveria ser nulo");
        assertEquals(cepSolicitado, resultado.numeroCep(), "O CEP retornado deve ser igual ao solicitado");


        verify(cepRepository, times(1)).findByNumeroCep(cepSolicitado);
    }

    @Test
    void deveFalharQuandoCepNaoExiste() {

        String cepInexistente = "00000000";
        when(cepRepository.findByNumeroCep(cepInexistente)).thenReturn(Optional.empty());

        assertThrows(CepNaoExistenteException.class, () -> {
            cepService.buscarPorCep(cepInexistente);
        });
    }
}