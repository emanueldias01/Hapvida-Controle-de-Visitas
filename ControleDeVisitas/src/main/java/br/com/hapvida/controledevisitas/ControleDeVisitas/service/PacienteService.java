package br.com.hapvida.controledevisitas.ControleDeVisitas.service;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteResponseDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteUpdateDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.CpfJaCadastradoException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.LeitoIndisponivelException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.PacienteJaCadastradoException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.PacienteNotFoundException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.model.paciente.Paciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService{

    @Autowired
    PacienteRepository pacienteRepository;

    public List<PacienteResponseDTO> getAllPacientes(){
        List<PacienteResponseDTO> list = pacienteRepository.findAll().stream().map(PacienteResponseDTO::new).toList();
        return list;
    }

    public PacienteResponseDTO getPacienteByNome(String nome){
        var paciente = pacienteRepository.findByNome(nome);
        if(paciente.isPresent()){
            return new PacienteResponseDTO(paciente.get());
        }else{
            throw new PacienteNotFoundException("Nao encontramos o paciente em nosso sistema");
        }
    }

    public PacienteResponseDTO registerNewPaciente(PacienteRequestDTO data){
        if(validaPaciente(data)){
            Paciente pacienteSave = new Paciente(data);
            pacienteRepository.save(pacienteSave);
            return new PacienteResponseDTO(pacienteSave);
        }else{
            throw new RuntimeException("Paciente nao registrado");
        }
    }

    public PacienteResponseDTO updatePaciente(PacienteUpdateDTO data){
        var paciente = pacienteRepository.findById(data.id());

        if(paciente.isPresent()){
            var pacienteLeitoBusca = pacienteRepository.findByNumeroLeito(data.leito());
            if(pacienteLeitoBusca.isPresent()){
                Paciente pacienteQueTrocaLeito = pacienteRepository.getReferenceById(pacienteLeitoBusca.get().getId());
                pacienteQueTrocaLeito.setNumeroLeito(paciente.get().getNumeroLeito());
            }
            var pacienteManipulavel = paciente.get();
            pacienteManipulavel.updateInfo(data);
            return new PacienteResponseDTO(pacienteManipulavel);

        }else{
            throw new PacienteNotFoundException("O paciente nao foi encontrado");
        }
    }

    public void deletePaciente(Long id){
        pacienteRepository.deleteById(id);
    }

    private boolean validaPaciente(PacienteRequestDTO data) {

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
