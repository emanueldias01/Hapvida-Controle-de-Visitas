package br.com.hapvida.controledevisitas.ControleDeVisitas.service;


import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.VisitanteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.VisitanteResponseDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.CpfJaCadastradoException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.PacienteNotFoundException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.PacienteRepository;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.VisitanteRepository;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Categoria;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Visitante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitanteService {

    @Autowired
    VisitanteRepository visitanteRepository;
    @Autowired
    PacienteRepository pacienteRepository;

    //metodos de validacao

    private boolean validaVisitantesDuplicados(VisitanteRequestDTO data) {
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
            throw new RuntimeException("o visitante ja existe");
        }

        return validacaoAceita;
    }


    private boolean validaQuantidadeDePessoasNoLeito(VisitanteRequestDTO data) {
        boolean podeCadastrar = false;
        var categoriaFornecida = data.categoria();
        List<Visitante> listaDeVisitantesDoPaciente = data.paciente().getVisitantes();
        if(listaDeVisitantesDoPaciente.size() < 2){

            int contadorA = 0;
            int contadorV = 0;

            for(Visitante j : listaDeVisitantesDoPaciente){

                if(j.getCategoria() == Categoria.ACOMPANHANTE){
                    contadorA++;
                }else{
                    contadorV++;
                }

            }

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
            throw new RuntimeException("O leito está cheio");
        }

        return podeCadastrar;

    }


    //metodos de service

    public List<VisitanteResponseDTO> getAllVisitantes(){
        List<VisitanteResponseDTO> list = visitanteRepository.findAll().stream().map(VisitanteResponseDTO::new).toList();
        return list;
    }

    public VisitanteResponseDTO getVisitanteById(Long id){
        var visitante = visitanteRepository.findById(id);

        if(visitante.isPresent()){
            var visitanteManipulavel = visitante.get();
            return new VisitanteResponseDTO(visitanteManipulavel);
        }else{
            throw new RuntimeException("Visitante nao encontrado");
        }
    }

    public VisitanteResponseDTO registerNewVisitante(VisitanteRequestDTO data){
        if(validaVisitantesDuplicados(data)){
            if(validaQuantidadeDePessoasNoLeito(data)){
                Visitante visitanteSave = new Visitante(data);
                visitanteRepository.save(visitanteSave);
                return new VisitanteResponseDTO(visitanteSave);
            }else{
                throw new RuntimeException("Leito cheio");
            }
        }else{
            throw new RuntimeException("Nao foi possivel cadastrar o visitante/acompanhante");
        }
    }

    public void deleteVisitante(Long id){
        var visitante = visitanteRepository.findById(id).get();
        List<Visitante> listaDeVisitantesDoTalPaciente = visitante.getPaciente().getVisitantes();

        listaDeVisitantesDoTalPaciente.remove(visitante);
        visitanteRepository.deleteById(id);

    }


}
