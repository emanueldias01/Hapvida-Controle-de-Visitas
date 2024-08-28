package br.com.hapvida.controledevisitas.ControleDeVisitas.service;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteResponseDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.VisitanteResponseDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.entityValidadions.ValidacaoVisitante;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.VisitanteNotFoundException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel.Paciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.PacienteRepository;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.VisitanteRepository;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Categoria;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Visitante;
import org.hibernate.validator.constraints.Mod10Check;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    @DisplayName("Busca visitantes")
    void getAllVisitantesSucess() {
        VisitanteResponseDTO v1 = new VisitanteResponseDTO(1L, "emanuel", "12345678900", Categoria.VISITANTE, "nomePaciente");
        VisitanteResponseDTO v2 = new VisitanteResponseDTO(1L, "maria", "12345678901", Categoria.VISITANTE, "nomePaciente");

        List<VisitanteResponseDTO> list = Arrays.asList(v1, v2);

        assertThat(list.size()).isEqualTo(2);
        when(visitanteService.getAllVisitantes()).thenReturn(list);
    }

    @Test
    @DisplayName("Deve retornar um visitante pelo nome")
    void getVisitanteByNomeSucess(){
        Paciente paciente = new Paciente(new PacienteRequestDTO("nome", "1234566778", 1));
        Visitante visitante = new Visitante(1L, "emanuel", "12345678900", Categoria.VISITANTE, LocalDateTime.now(), paciente);
        when(visitanteRepository.findByNome("emanuel")).thenReturn(Optional.of(visitante));

        var result = visitanteService.getVisitanteByNome("emanuel");
        assertNotNull(result);
        assertThat(result.nome()).isEqualTo(visitante.getNome());
    }

    @Test
    @DisplayName("Nao deve retornar o visitante se ele nao está no sistema")
    void getVisitanteByNomeFail(){
        Paciente paciente = new Paciente(new PacienteRequestDTO("nome", "1234566778", 1));
        Visitante visitante = new Visitante(1L, "emanuel", "12345678900", Categoria.VISITANTE, LocalDateTime.now(), paciente);
        when(visitanteRepository.findByNome("fail")).thenReturn(Optional.empty());

        Assertions.assertThrows(VisitanteNotFoundException.class, () -> visitanteService.getVisitanteByNome("fail"));

    }
}