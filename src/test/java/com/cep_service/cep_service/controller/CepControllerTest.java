package com.cep_service.cep_service.controller;


import com.cep_service.cep_service.domain.cep.Cep;
import com.cep_service.cep_service.domain.cep.CepController;
import com.cep_service.cep_service.domain.cep.dto.DadosDetalharCep;
import com.cep_service.cep_service.domain.cep.CepService;
import com.cep_service.cep_service.domain.usuario.UsuarioRepository;
import com.cep_service.cep_service.infra.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CepController.class)
@AutoConfigureMockMvc(addFilters = false)
class CepControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CepService cepService;

    // --- Mocks Adicionais Necessários para o SecurityFilter ---

    // O filtro precisa validar o token, então precisamos do TokenService mockado
    @MockitoBean
    private TokenService tokenService;

    // O filtro busca o usuário no banco, precisamos do Repository mockado
    @MockitoBean
    private UsuarioRepository usuarioRepository;


    @Test
    void deveRetornarSucessoQuandoCepExiste() throws Exception {
        String cepSolicitado = "28070077";
        Cep cepFalso = new Cep(cepSolicitado, "Rua Teste", "Campos");

        when(cepService.buscarPorCep(cepSolicitado))
                .thenReturn(new DadosDetalharCep(cepFalso));

        // por padrão ele rejeitaria requests sem token (401/403).
        // Em um cenário real de teste de unidade de Controller,
        // costumamos usar @AutoConfigureMockMvc(addFilters = false)
        // OU configurar um usuário mockado com @WithMockUser.
        // Mas para corrigir o erro de "ApplicationContext", os mocks acima resolvem o load.

        mockMvc.perform(get("/cep/numeroCep/{numeroCep}", cepSolicitado)
                        .contentType(MediaType.APPLICATION_JSON))
                // Se o seu Security estiver ativo, isso pode retornar 403 aqui.
                // Se der erro 403, adicione .with(user("admin")) ou desative os filtros.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCep").value(cepSolicitado))
                .andExpect(jsonPath("$.cidade").value("Campos"));
    }

    @Test
    void deveRetornarErroQuandoServiceFalha() throws Exception {
        // Arrange
        when(cepService.buscarPorCep(anyString())).thenThrow(new RuntimeException("Erro interno"));

        // Act & Assert
        // Usamos a estrutura correta da URL e passamos o valor "00000000" como variável
        mockMvc.perform(get("/cep/numeroCep/{numeroCep}", "00000000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
