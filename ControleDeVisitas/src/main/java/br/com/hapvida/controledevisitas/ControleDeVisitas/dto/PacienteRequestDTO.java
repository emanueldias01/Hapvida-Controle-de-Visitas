package br.com.hapvida.controledevisitas.ControleDeVisitas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PacienteRequestDTO(
        @NotBlank
        String nome,
        @NotBlank
        String cpf,
        @NotNull
        int numeroLeito) {
}
