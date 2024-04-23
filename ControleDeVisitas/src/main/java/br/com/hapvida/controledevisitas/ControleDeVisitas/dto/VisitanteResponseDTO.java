package br.com.hapvida.controledevisitas.ControleDeVisitas.dto;

import br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel.Paciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Categoria;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Visitante;

public record VisitanteResponseDTO(String id, String nome, Categoria categoria, Paciente paciente) {

    public VisitanteResponseDTO(Visitante visitante){
        this(visitante.getNome(), visitante.getCpf(), visitante.getCategoria(), visitante.getPaciente());

    }

}
