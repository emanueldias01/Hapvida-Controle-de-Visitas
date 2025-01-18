package br.com.hapvida.controledevisitas.ControleDeVisitas.repository;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteResponseDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.VisitanteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.model.paciente.Paciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.model.visitante.Categoria;
import br.com.hapvida.controledevisitas.ControleDeVisitas.model.visitante.Visitante;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class VisitanteRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    VisitanteRepository visitanteRepository;

    @Test
    @DisplayName("Deve retornar o acompanhante daquele paciente")
    void buscaAcompanhanteSucess(){

        PacienteRequestDTO pacienteRequestDTO = new PacienteRequestDTO(
                "nomeQualquer", "12345678910", 1
        );

        Paciente paciente = createPaciente(pacienteRequestDTO);
        Long pacienteId = paciente.getId();

        VisitanteRequestDTO visitanteRequestDTO = new VisitanteRequestDTO(
                "nomeVisitante",
                "12345678910",
                new PacienteResponseDTO(paciente),
                Categoria.ACOMPANHANTE);

        Visitante acompanhante = createVisitante(visitanteRequestDTO, paciente);

        Optional<Visitante> result = visitanteRepository.buscaAcompanhante(pacienteId);
        assertThat(result.isPresent()).isTrue();

    }

    @Test
    @DisplayName("Deve falhar ao tentar encontrar o acompanhante do paciente")
    void buscaAcompanhanteFail(){

        PacienteRequestDTO pacienteRequestDTO = new PacienteRequestDTO(
                "nomeQualquer", "12345678910", 1
        );

        Paciente paciente = createPaciente(pacienteRequestDTO);
        Long pacienteIdFalso = paciente.getId() + 1;

        VisitanteRequestDTO visitanteRequestDTO = new VisitanteRequestDTO(
                "nomeVisitante",
                "12345678910",
                new PacienteResponseDTO(paciente),
                Categoria.ACOMPANHANTE);

        Visitante acompanhante = createVisitante(visitanteRequestDTO, paciente);

        Optional<Visitante> result = visitanteRepository.buscaAcompanhante(pacienteIdFalso);
        assertThat(result.isPresent()).isFalse();

    }

    private Paciente createPaciente(PacienteRequestDTO dto){
        Paciente pacienteSave = new Paciente(dto);
        entityManager.persist(pacienteSave);
        entityManager.flush();
        return pacienteSave;
    }

    private Visitante createVisitante(VisitanteRequestDTO dto, Paciente paciente){
        Visitante visitanteSave = new Visitante(dto);
        visitanteSave.setPaciente(paciente);
        entityManager.persist(visitanteSave);
        entityManager.flush();
        return visitanteSave;
    }

}