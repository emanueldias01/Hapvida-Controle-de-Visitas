package br.com.hapvida.controledevisitas.ControleDeVisitas.service;


import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.VisitanteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.VisitanteResponseDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.*;
import br.com.hapvida.controledevisitas.ControleDeVisitas.model.paciente.Paciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.PacienteRepository;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.VisitanteRepository;
import br.com.hapvida.controledevisitas.ControleDeVisitas.model.visitante.Categoria;
import br.com.hapvida.controledevisitas.ControleDeVisitas.model.visitante.Visitante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VisitanteService {

    @Autowired
    VisitanteRepository visitanteRepository;
    @Autowired
    PacienteRepository pacienteRepository;

    public List<VisitanteResponseDTO> getAllVisitantes(){
        List<VisitanteResponseDTO> list = visitanteRepository.findAll().stream().map(VisitanteResponseDTO::new).toList();
        return list;
    }

    public VisitanteResponseDTO getVisitanteByNome(String nome){
        var visitante = visitanteRepository.findByNome(nome);
        if(visitante.isPresent()){
            var visitanteManipulavel = visitante.get();
            return new VisitanteResponseDTO(visitanteManipulavel);
        }else{
            throw new VisitanteNotFoundException("Visitante nao encontrado");
        }
    }

    public VisitanteResponseDTO registerNewVisitante(VisitanteRequestDTO data){
        if(validaVisitantesDuplicados(data)) {
            List<Visitante> listaDeVisitantes = visitanteRepository.findByPacienteId(data.paciente().id());
            if (verificaSeHaAcompanhante(listaDeVisitantes) && data.categoria() == Categoria.ACOMPANHANTE) {
                trocarDeAcompanhante(data);
                Visitante novoAcompanhante = new Visitante(data);
                return new VisitanteResponseDTO(novoAcompanhante);
           }
            else{
                if (validaQuantidadeDePessoasNoLeito(data)) {
                    Visitante visitanteSave = new Visitante(data);
                    visitanteSave.setPaciente(pacienteRepository.findById(data.paciente().id()).get());
                    visitanteRepository.save(visitanteSave);

                    var paciente = pacienteRepository.findById(data.paciente().id());
                    List<Visitante> listaDeVisitantesDoPaciente = paciente.get().getVisitantes();
                    listaDeVisitantesDoPaciente.add(visitanteSave);

                    return new VisitanteResponseDTO(visitanteSave);
                } else {
                    throw new RuntimeException("Leito cheio");
                }
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

    public void trocarDeAcompanhante(@RequestBody VisitanteRequestDTO data) {
        Optional<Paciente> pacienteReferencia = pacienteRepository.findByNome(data.paciente().nome());
        if (pacienteReferencia.isPresent()) {
            var pacienteManipulavel = pacienteReferencia.get();

            List<Visitante> listaDeVisitantesDoPaciente = pacienteManipulavel.getVisitantes();

            Visitante acompanhanteDoPaciente = pegaReferenciaDoAcompanhante(pacienteManipulavel.getId());
            Duration diferencaDeHorario = Duration.between(LocalDateTime.now(), acompanhanteDoPaciente.getDataEntrada());
            Long diferencaEmHoras = diferencaDeHorario.toHours();
            diferencaEmHoras *=-1;

            if (diferencaEmHoras > 2) {
                deleteVisitante(acompanhanteDoPaciente.getId());

                Optional<Paciente> paciente = pacienteRepository.findByNome(data.paciente().nome());
                var list = paciente.get().getVisitantes();
                Visitante novoAcompanhante = new Visitante(data);
                list.add(novoAcompanhante);
            }else {
                throw new TempoLimiteAcompanhanteException("O acompanhante atual nao está a mais de 2 horas");
            }
        }else{
            throw new PacienteNotFoundException("O paciente nao foi encontrado");
        }
    }

    private boolean validaVisitantesDuplicados(VisitanteRequestDTO data) {
        boolean validacaoAceita;
        var nome = visitanteRepository.findByNome(data.nome());
        var cpf = visitanteRepository.findByCpf(data.cpf());
        var paciente = pacienteRepository.findByNome(data.paciente().nome());

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


    private boolean validaQuantidadeDePessoasNoLeito(VisitanteRequestDTO data) {
        boolean podeCadastrar = false;
        var categoriaFornecida = data.categoria();
        List<Visitante> listaDeVisitantesDoPaciente = visitanteRepository.findByPacienteId(data.paciente().id());

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


    private boolean verificaSeHaAcompanhante(List<Visitante> listaDeVisitantesDoPaciente) {
        boolean existeAcompanhante = false;
        for(Visitante visitante : listaDeVisitantesDoPaciente){
            if(visitante.getCategoria() == Categoria.ACOMPANHANTE){
                existeAcompanhante = true;
            }
        }
        return existeAcompanhante;
    }


    private Visitante pegaReferenciaDoAcompanhante(Long idPaciente) {
        var acompanahnteOptional = visitanteRepository.buscaAcompanhante(idPaciente);
        if(acompanahnteOptional.isPresent()){
            return acompanahnteOptional.get();
        }else{
            throw new VisitanteNotFoundException("nenhum acompanhante nao foi encontrado");
        }
    }
}