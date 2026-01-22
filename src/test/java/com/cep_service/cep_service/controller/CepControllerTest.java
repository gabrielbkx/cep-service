package com.cep_service.cep_service.controller;


import com.cep_service.cep_service.domain.cep.Cep;
import com.cep_service.cep_service.domain.cep.dto.DadosDetalharCep;
import com.cep_service.cep_service.domain.cep.CepService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CepController.class) // 1. Foca apenas neste Controller
class CepControllerTest {

    @Autowired
    private MockMvc mockMvc; // O Robô piloto

    @MockitoBean
    private CepService cepService; // O Motor simulado

    @Test
    void deveRetornarSucessoQuandoCepExiste() throws Exception {
        // 1. Arrange
        String cepSolicitado = "28070077";
        Cep cepFalso = new Cep(cepSolicitado, "Rua Teste", "Campos");

        // CORREÇÃO: Retornando o DTO em vez da entidade pura
        when(cepService.buscarPorCep(cepSolicitado))
                .thenReturn(new DadosDetalharCep(cepFalso));

        // 2. & 3. Act & Assert
        mockMvc.perform(get("/cep/numeroCep/{numeroCep}", cepSolicitado)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCep").value(cepSolicitado))
                .andExpect(jsonPath("$.cidade").value("Campos"));
    }

    @Test
    void deveRetornarErroQuandoServiceFalha() throws Exception {
        // 1. Arrange
        // Simulamos que o Service vai explodir com aquele erro que você viu antes
        when(cepService.buscarPorCep(anyString())).thenThrow(new RuntimeException("Erro interno"));

        // 2. & 3. Act & Assert
        mockMvc.perform(get("/cep/00000000")
                        .contentType(MediaType.APPLICATION_JSON))
                // Aqui esperamos o status que sua API retorna no erro (500 ou 404)
                // Ajuste conforme o seu ExceptionHandler
                .andExpect(status().isInternalServerError());
    }
}