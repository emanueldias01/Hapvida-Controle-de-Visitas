package br.com.hapvida.controledevisitas.ControleDeVisitas.repository;

import br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByNome(String nome);

    Optional<Paciente> findByCpf(String cpf);

    Optional<Paciente> findByNumeroLeito(int leito);
}
