package br.com.hapvida.controledevisitas.ControleDeVisitas.service;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.CpfJaCadastradoException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.LeitoIndisponivelException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.PacienteJaCadastradoException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidacaoPaciente {

    @Autowired
    PacienteRepository pacienteRepository;

    protected boolean validaPaciente(PacienteRequestDTO data) {

        boolean podeCriarPaciente = false;
        var nome = pacienteRepository.findByNome(data.nome());
        var cpf = pacienteRepository.findByCpf(data.cpf());
        var leito = pacienteRepository.findByNumeroLeito(data.numeroLeito());


        //VALIDACOES DE DUPLICACOES
        if(nome.isEmpty()){
            if(cpf.isEmpty()){
                if(leito.isEmpty()){
                    podeCriarPaciente = true;
                }else {
                    throw new LeitoIndisponivelException("leito indisponivel");
                }
            }else{
                throw new CpfJaCadastradoException("cpf ja cadastrado");
            }
        }else {
            throw new PacienteJaCadastradoException("nome ja cadastrado");
        }


        //valida cpf
        if(data.cpf().length() == 11){

        }else {
            podeCriarPaciente = false;
        }

        return podeCriarPaciente;
    }
}
