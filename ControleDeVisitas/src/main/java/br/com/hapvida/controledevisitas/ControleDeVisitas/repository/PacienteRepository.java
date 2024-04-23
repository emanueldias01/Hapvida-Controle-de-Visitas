package br.com.hapvida.controledevisitas.ControleDeVisitas.repository;

import br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
