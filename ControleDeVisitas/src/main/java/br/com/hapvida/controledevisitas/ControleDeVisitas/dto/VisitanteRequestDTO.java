package br.com.hapvida.controledevisitas.ControleDeVisitas.dto;


import br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel.Paciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VisitanteRequestDTO(
        @NotBlank
        String nome,
        @NotBlank
        String cpf,
        @NotNull
        Long pacienteId,
        @NotBlank
        Categoria categoria) {
}
