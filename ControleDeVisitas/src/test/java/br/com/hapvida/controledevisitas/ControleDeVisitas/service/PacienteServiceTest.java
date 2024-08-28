package br.com.hapvida.controledevisitas.ControleDeVisitas.service;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteResponseDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.entityValidadions.ValidacaoPaciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.PacienteNotFoundException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel.Paciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.PacienteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PacienteServiceTest {

    @Mock
    PacienteRepository pacienteRepository;

    @Mock
    ValidacaoPaciente validacaoPaciente;

    @Autowired
    @InjectMocks
    PacienteService pacienteService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar todos os pacientes")
    void getAllPacientesSucess() {
        Paciente p1 = new Paciente("1234566789", LocalDateTime.now(),1L, "emanuel", 4);
        Paciente p2 = new Paciente("1234566788", LocalDateTime.now(),2L, "maria", 5);

        List<PacienteResponseDTO> result = new ArrayList<>();
        result.add(new PacienteResponseDTO(p1));
        result.add(new PacienteResponseDTO(p2));

        assertThat(result.size()).isEqualTo(2);
        when(pacienteService.getAllPacientes()).thenReturn(result);

    }

    @Test
    @DisplayName("Deve retornar o paciente pelo nome")
    void getPacienteByNomeSucess(){
        Paciente p1 = new Paciente("1234566789", LocalDateTime.now(),1L, "emanuel", 4);

        when(pacienteRepository.findByNome("emanuel")).thenReturn(Optional.of(p1));

        var result = pacienteService.getPacienteByNome("emanuel");
        assertNotNull(result);
        assertThat(result.nome()).isEqualTo(p1.getNome());
    }

    @Test
    @DisplayName("Retorna Exception por usuario nao existir")
    void getPacienteByNomeFail(){
        Paciente p1 = new Paciente("1234566789", LocalDateTime.now(),1L, "emanuel", 4);
        when(pacienteRepository.findByNome("fail")).thenReturn(Optional.empty());
        Assertions.assertThrows(PacienteNotFoundException.class, () -> pacienteService.getPacienteByNome("fail"));
    }

    @Test
    @DisplayName("Nao deve deixar cadastrar paciente")
    void registerNewPacienteFail(){
        PacienteRequestDTO dto = new PacienteRequestDTO("nome", "cpf", 1);
        when(pacienteService.validacaoPaciente.validaPaciente(dto)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class, () -> pacienteService.registerNewPaciente(dto));
    }

    @Test
    @DisplayName("Deixa cadastrar paciente")
    void registerNewPacienteSucess(){
        PacienteRequestDTO dto = new PacienteRequestDTO("nome", "cpf", 1);
        when(pacienteService.validacaoPaciente.validaPaciente(dto)).thenReturn(true);

        var result = pacienteService.registerNewPaciente(dto);
        assertThat(result.nome()).isEqualTo("nome");
    }

}