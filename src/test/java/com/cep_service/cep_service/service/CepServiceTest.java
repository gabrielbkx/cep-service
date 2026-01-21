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
        // 1. Arrange (Preparar)
        String cepSolicitado = "28070077";
        Cep cepSimulado = new Cep(cepSolicitado, "Rua Teste", "Campos");
        when(cepRepository.findByNumeroCep(cepSolicitado)).thenReturn(Optional.of(cepSimulado));

        // 2. Act (Executar)
        // Nota: Assumi que seu método retorna o objeto Cep diretamente
        DadosDetalharCep resultado = cepService.buscarPorCep(cepSolicitado);

        // 3. Assert (Verificar)
        assertNotNull(resultado, "O resultado não deveria ser nulo");
        assertEquals(cepSolicitado, resultado.numeroCep(), "O CEP retornado deve ser igual ao solicitado");

        // Verifica se o método do repositório foi chamado exatamente 1 vez
        verify(cepRepository, times(1)).findByNumeroCep(cepSolicitado);
    }

    @Test
    void deveFalharQuandoCepNaoExiste() {
        // 1. Arrange
        String cepInexistente = "00000000";
        when(cepRepository.findByNumeroCep(cepInexistente)).thenReturn(Optional.empty());

        // 2. & 3. Act & Assert
        // Aqui verificamos se o serviço lança a exceção correta quando o CEP não vem do banco
        // IMPORTANTE: Substitua 'RuntimeException.class' pela classe da sua exceção personalizada (ex: EntityNotFoundException.class)
        assertThrows(CepNaoExistenteException.class, () -> {
            cepService.buscarPorCep(cepInexistente);
        });
    }
}