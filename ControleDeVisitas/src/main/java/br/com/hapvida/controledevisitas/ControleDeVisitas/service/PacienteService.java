package br.com.hapvida.controledevisitas.ControleDeVisitas.service;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteResponseDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteUpdateDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.entityValidadions.ValidacaoPaciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.PacienteNotFoundException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel.Paciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService{

    @Autowired
    PacienteRepository pacienteRepository;
    @Autowired
    ValidacaoPaciente validacaoPaciente;

    //metodos service

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

        if(validacaoPaciente.validaPaciente(data)){
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
            var leitoBusca = pacienteRepository.findByNumeroLeito(data.leito());
            if(leitoBusca.isPresent()){
                throw new RuntimeException("O leito em que quer editar está sendo usado");
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


}
