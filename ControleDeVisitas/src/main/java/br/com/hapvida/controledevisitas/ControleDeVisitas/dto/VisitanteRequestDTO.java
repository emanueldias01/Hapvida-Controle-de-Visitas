package br.com.hapvida.controledevisitas.ControleDeVisitas.dto;

import br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel.Paciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Categoria;

public record VisitanteRequestDTO(String nome, String cpf, Paciente paciente, Categoria categoria) {
}
