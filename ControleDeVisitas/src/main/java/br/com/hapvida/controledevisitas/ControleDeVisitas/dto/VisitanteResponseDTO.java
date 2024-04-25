package br.com.hapvida.controledevisitas.ControleDeVisitas.dto;

import br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel.Paciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Categoria;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Visitante;

public record VisitanteResponseDTO(Long id, String nome,String cpf, Categoria categoria, String paciente) {

    public VisitanteResponseDTO(Visitante visitante){
        this(visitante.getId(),visitante.getNome(), visitante.getCpf(), visitante.getCategoria(), visitante.getPaciente().getNome());

    }

}
