package br.com.hapvida.controledevisitas.ControleDeVisitas.entityValidadions;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.VisitanteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.*;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.PacienteRepository;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.VisitanteRepository;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Categoria;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Visitante;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ValidacaoVisitante {
    @Autowired
    PacienteRepository pacienteRepository;
    @Autowired
    VisitanteRepository visitanteRepository;



    protected boolean validaVisitantesDuplicados(VisitanteRequestDTO data) {
        boolean validacaoAceita = false;

        var nome = visitanteRepository.findByNome(data.nome());
        var cpf = visitanteRepository.findByCpf(data.cpf());
        var paciente = pacienteRepository.findByNome(data.paciente().getNome());

        if(nome.isEmpty()){
            if(cpf.isEmpty()){
                if(paciente.isPresent()){
                    validacaoAceita = true;
                }else {
                    throw new PacienteNotFoundException("Paciente nao encontrado");
                }
            }else{
                throw new CpfJaCadastradoException("cpf ja cadastrado");
            }
        }else {
            throw new NomeVisitanteJaExisteException("o visitante ja existe");
        }

        return validacaoAceita;
    }


    protected boolean validaQuantidadeDePessoasNoLeito(VisitanteRequestDTO data) {
        boolean podeCadastrar = false;

        var categoriaFornecida = data.categoria();

        List<Visitante> listaDeVisitantesDoPaciente = visitanteRepository.findByPacienteId(data.paciente().getId());

        //System.out.println("tamanho da lista de visitantes do paciente = "+ listaDeVisitantesDoPaciente.size());
        if(listaDeVisitantesDoPaciente.size() < 2){

            int contadorA = 0;
            int contadorV = 0;

            for(Visitante j : listaDeVisitantesDoPaciente){

                if(j.getCategoria() == Categoria.ACOMPANHANTE){
                    contadorA++;

                }else if (j.getCategoria() == Categoria.VISITANTE){
                    contadorV++;

                }


            }

            System.out.println("Contador a = " + contadorA);
            System.out.println("Contador v = " + contadorV);

            if(contadorA == 0 && contadorV == 0 && categoriaFornecida == Categoria.ACOMPANHANTE){
                podeCadastrar = true;
            }
            if(contadorA == 0 && contadorV <=1 && categoriaFornecida == Categoria.ACOMPANHANTE){
                podeCadastrar = true;
            }
            if(contadorA == 0 && contadorV <= 1 && categoriaFornecida == Categoria.VISITANTE){
                podeCadastrar = true;
            }
            if(contadorA == 1 && contadorV == 0 &&  categoriaFornecida == Categoria.VISITANTE){
                podeCadastrar = true;
            }

        }else{
            throw new LeitoCheioException("O leito está cheio");
        }

        return podeCadastrar;

    }


    protected boolean verificaSeHaAcompanhante(List<Visitante> listaDeVisitantesDoPaciente) {


        boolean existeAcompanhante = false;
        for(Visitante visitante : listaDeVisitantesDoPaciente){

            if(visitante.getCategoria() == Categoria.ACOMPANHANTE){
                existeAcompanhante = true;
            }

        }

        return existeAcompanhante;
    }


    protected Visitante pegaReferenciaDoAcompanhante(Long idPaciente) {

        var acompanahnteOptional = visitanteRepository.buscaAcompanhante(idPaciente);
        if(acompanahnteOptional.isPresent()){
            return acompanahnteOptional.get();
        }else{
            throw new VisitanteNotFoundException("nenhum acompanhante nao foi encontrado");
        }
    }
}
