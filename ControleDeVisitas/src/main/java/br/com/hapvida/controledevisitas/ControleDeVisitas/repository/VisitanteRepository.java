package br.com.hapvida.controledevisitas.ControleDeVisitas.repository;

import br.com.hapvida.controledevisitas.ControleDeVisitas.model.visitante.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
    Optional<Visitante> findByNome(String nome);

    Optional<Visitante> findByCpf(String cpf);

    List<Visitante> findByPacienteId(Long id);

    @Query("SELECT v FROM Visitante v WHERE v.paciente.id = :pacienteId AND v.categoria = 'ACOMPANHANTE'")
    Optional<Visitante> buscaAcompanhante(Long pacienteId);
}
