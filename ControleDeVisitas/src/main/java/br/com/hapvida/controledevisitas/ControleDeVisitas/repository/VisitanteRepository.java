package br.com.hapvida.controledevisitas.ControleDeVisitas.repository;

import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
}
