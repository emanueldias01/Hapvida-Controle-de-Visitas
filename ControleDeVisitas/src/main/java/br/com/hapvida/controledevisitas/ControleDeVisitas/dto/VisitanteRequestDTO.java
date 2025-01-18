package br.com.hapvida.controledevisitas.ControleDeVisitas.dto;


import br.com.hapvida.controledevisitas.ControleDeVisitas.model.visitante.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VisitanteRequestDTO(
        @NotBlank
        String nome,
        @NotBlank
        String cpf,
        @NotNull
        PacienteResponseDTO paciente,
        @NotNull
        Categoria categoria) {
}
