package br.com.hapvida.controledevisitas.ControleDeVisitas.service;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteResponseDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.VisitanteResponseDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.entityValidadions.ValidacaoVisitante;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.PacienteRepository;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.VisitanteRepository;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Categoria;
import org.hibernate.validator.constraints.Mod10Check;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class VisitanteServiceTest {

    @Mock
    VisitanteRepository visitanteRepository;
    @Mock
    PacienteRepository pacienteRepository;
    @Mock
    ValidacaoVisitante validacaoVisitante;

    @Autowired
    @InjectMocks
    VisitanteService visitanteService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Falha ao buscar visitantes")
    void getAllVisitantesFail() {
        VisitanteResponseDTO v1 = new VisitanteResponseDTO(1L, "emanuel", "12345678900", Categoria.VISITANTE, "nomePaciente");
        VisitanteResponseDTO v2 = new VisitanteResponseDTO(1L, "maria", "12345678901", Categoria.VISITANTE, "nomePaciente");

        List<VisitanteResponseDTO> list = Arrays.asList(v1, v2);

        assertThat(list.size()).isEqualTo(2);
        when(visitanteService.getAllVisitantes()).thenReturn(list);
    }
}