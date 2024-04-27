package br.com.hapvida.controledevisitas.ControleDeVisitas.service;


import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.VisitanteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.VisitanteResponseDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.CpfJaCadastradoException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.NomeVisitanteJaExisteException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.PacienteNotFoundException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.VisitanteNotFoundException;
import br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel.Paciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.PacienteRepository;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.VisitanteRepository;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Categoria;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Visitante;
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
            throw new NomeVisitanteJaExisteException("o visitante ja existe");
        }

        return validacaoAceita;
    }


    private boolean validaQuantidadeDePessoasNoLeito(VisitanteRequestDTO data) {
        boolean podeCadastrar = false;

        var categoriaFornecida = data.categoria();
        //List<Visitante> listaDeVisitantesDoPaciente = data.paciente().getVisitantes();

        List<Visitante> listaDeVisitantesDoPaciente = visitanteRepository.findByPacienteId(data.paciente().getId());

        //nao está contabilizando o tamanho da lista conforme o numero de visitantes
        System.out.println("tamanho da lista de visitantes do paciente = "+ listaDeVisitantesDoPaciente.size());
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
            throw new RuntimeException("O leito está cheio");
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


    private Visitante pegaReferenciaDoAcompanhante(List<Visitante> listaDeVisitantesDoPaciente) {
        Visitante acompanhanteReferencia = null;
        for(Visitante visitante : listaDeVisitantesDoPaciente){
            if(visitante.getCategoria() == Categoria.ACOMPANHANTE){
                acompanhanteReferencia = visitante;
            }
        }
        return acompanhanteReferencia;
    }

    //metodos de service

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
        if(validaVisitantesDuplicados(data)){
            if(validaQuantidadeDePessoasNoLeito(data)){
                Visitante visitanteSave = new Visitante(data);
                visitanteRepository.save(visitanteSave);

                var paciente = pacienteRepository.findById(data.paciente().getId());
                List<Visitante> listaDeVisitantesDoPaciente = paciente.get().getVisitantes();
                listaDeVisitantesDoPaciente.add(visitanteSave);

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

    public void trocarDeAcompanhante(@RequestBody VisitanteRequestDTO data) {
        Optional<Paciente> pacienteReferencia = pacienteRepository.findByNome(data.paciente().getNome());

        if (pacienteReferencia.isPresent()) {
            if (data.categoria() == Categoria.ACOMPANHANTE) {
                var pacienteManipulavel = data.paciente();

                List<Visitante> listaDeVisitantesDoPaciente = pacienteManipulavel.getVisitantes();

                if (verificaSeHaAcompanhante(listaDeVisitantesDoPaciente)) {
                    Visitante acompanhanteDoPaciente = pegaReferenciaDoAcompanhante(listaDeVisitantesDoPaciente);
                    Duration diferencaDeHorario = Duration.between(LocalDateTime.now(), acompanhanteDoPaciente.getDataEntrada());
                    if (diferencaDeHorario.compareTo(Duration.ofHours(2)) >= 0) {
                        Optional<Paciente> paciente = pacienteRepository.findByNome(data.paciente().getNome());
                        var list = paciente.get().getVisitantes();
                        list.remove(acompanhanteDoPaciente);
                        list.add(new Visitante(data));
                    }

                }

            }

        }

    }

}