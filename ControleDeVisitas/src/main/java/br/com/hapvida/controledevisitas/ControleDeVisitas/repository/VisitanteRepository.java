package br.com.hapvida.controledevisitas.ControleDeVisitas.repository;

import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
}
