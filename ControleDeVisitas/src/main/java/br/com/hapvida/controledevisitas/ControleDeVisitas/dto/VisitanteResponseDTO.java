package br.com.hapvida.controledevisitas.ControleDeVisitas.dto;

import br.com.hapvida.controledevisitas.ControleDeVisitas.model.visitante.Categoria;
import br.com.hapvida.controledevisitas.ControleDeVisitas.model.visitante.Visitante;

public record VisitanteResponseDTO(Long id, String nome,String cpf, Categoria categoria, String paciente) {

    public VisitanteResponseDTO(Visitante visitante){
        this(visitante.getId(),visitante.getNome(), visitante.getCpf(), visitante.getCategoria(), visitante.getPaciente().getNome());

    }

}
