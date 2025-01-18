package br.com.hapvida.controledevisitas.ControleDeVisitas.dto;

import br.com.hapvida.controledevisitas.ControleDeVisitas.model.paciente.Paciente;

import java.time.LocalDateTime;

public record PacienteResponseDTO(Long id, String nome, String cpf, int leito, LocalDateTime dataEntrada) {

    public PacienteResponseDTO(Paciente paciente){
        this(paciente.getId(), paciente.getNome(), paciente.getCpf(), paciente.getNumeroLeito(), paciente.getDataEntrada());
    }
}
