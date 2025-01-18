package br.com.hapvida.controledevisitas.ControleDeVisitas.service;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteResponseDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteUpdateDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.PacienteNotFoundException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.model.paciente.Paciente;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PacienteServiceTest {

    @Mock
    PacienteRepository pacienteRepository;

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
    @DisplayName("Retorna Exception por paciente nao existir")
    void getPacienteByNomeFail(){
        Paciente p1 = new Paciente("1234566789", LocalDateTime.now(),1L, "emanuel", 4);
        when(pacienteRepository.findByNome("fail")).thenReturn(Optional.empty());
        Assertions.assertThrows(PacienteNotFoundException.class, () -> pacienteService.getPacienteByNome("fail"));
    }

    @Test
    @DisplayName("Nao deve deixar cadastrar paciente por nome")
    void registerNewPacienteFailCpf(){
        String nome = "nome";
        String cpf = "11122233312";
        int numeroLeito = 1;

        PacienteRequestDTO dto = new PacienteRequestDTO(nome, cpf, numeroLeito);

        Paciente paciente = new Paciente(dto);

        when(pacienteRepository.findByNome(nome)).thenReturn(Optional.of(paciente));

        Assertions.assertThrows(RuntimeException.class, () -> pacienteService.registerNewPaciente(dto));
    }

    @Test
    @DisplayName("Nao deve deixar cadastrar paciente por cpf")
    void registerNewPacienteFailLeito(){
        String nome = "nome";
        String cpf = "11122233312";
        int numeroLeito = 1;

        PacienteRequestDTO dto = new PacienteRequestDTO(nome, cpf, numeroLeito);

        Paciente paciente = new Paciente(dto);

        when(pacienteRepository.findByCpf(cpf)).thenReturn(Optional.of(paciente));

        Assertions.assertThrows(RuntimeException.class, () -> pacienteService.registerNewPaciente(dto));
    }

    @Test
    @DisplayName("Nao deve deixar cadastrar paciente leito")
    void registerNewPacienteFail(){
        String nome = "nome";
        String cpf = "11122233312";
        int numeroLeito = 1;

        PacienteRequestDTO dto = new PacienteRequestDTO(nome, cpf, numeroLeito);

        Paciente paciente = new Paciente(dto);

        when(pacienteRepository.findByNumeroLeito(numeroLeito)).thenReturn(Optional.of(paciente));

        Assertions.assertThrows(RuntimeException.class, () -> pacienteService.registerNewPaciente(dto));
    }

    @Test
    @DisplayName("Deixa cadastrar paciente")
    void registerNewPacienteSucess(){
        String nome = "nome";
        String cpf = "11122233312";
        int numeroLeito = 1;

        PacienteRequestDTO dto = new PacienteRequestDTO(nome, cpf, numeroLeito);
        when(pacienteRepository.findByNome(nome)).thenReturn(Optional.empty());
        when(pacienteRepository.findByCpf(cpf)).thenReturn(Optional.empty());
        when(pacienteRepository.findByNumeroLeito(numeroLeito)).thenReturn(Optional.empty());

        var result = pacienteService.registerNewPaciente(dto);
        assertThat(result.nome()).isEqualTo("nome");
    }

    @Test
    @DisplayName("Nao deve deixar atualizar paciente")
    void updatePacienteFail(){
        PacienteUpdateDTO dto = new PacienteUpdateDTO(1L, 1);
        when(pacienteRepository.findById(dto.id())).thenReturn(Optional.empty());

        Assertions.assertThrows(PacienteNotFoundException.class, () -> pacienteService.updatePaciente(dto));
    }

    @Test
    @DisplayName("Deve deixar atualizar paciente")
    void updatePacienteSucess(){
        PacienteUpdateDTO dto = new PacienteUpdateDTO(1L, 1);

        Paciente paciente = new Paciente(1L, 1);

        when(pacienteRepository.findById(dto.id())).thenReturn(Optional.of(paciente));

        var result = pacienteService.updatePaciente(dto);
        assertThat(result.id()).isEqualTo(dto.id());
    }

}