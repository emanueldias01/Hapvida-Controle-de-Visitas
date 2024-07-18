package br.com.hapvida.controledevisitas.ControleDeVisitas.controller;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteUpdateDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/paciente")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    PacienteService service;

    @GetMapping("/allPacientes")
    @Operation(description = "busca por todos os pacientes que estao registrados no sistema")
    public ResponseEntity getAllPAcientesController(){
        var body = service.getAllPacientes();
        return ResponseEntity.ok(body);

    }

    @GetMapping("/pacienteByNome/{nome}")
    @Operation(description = "busca paciente pelo nome na URL")
    public ResponseEntity getPacienteByNomeController(@PathVariable String nome){
        var body = service.getPacienteByNome(nome);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/savePaciente")
    @Transactional
    @Operation(description = "registra um novo paciente no sistema")
    public ResponseEntity savePacienteController(@RequestBody @Valid PacienteRequestDTO data, UriComponentsBuilder uriComponentsBuilder){
        var body = service.registerNewPaciente(data);
        var uri = uriComponentsBuilder.path("{id}").buildAndExpand(body.id()).toUri();

        return ResponseEntity.created(uri).body(body);
    }

    @PutMapping("/updatePaciente")
    @Transactional
    @Operation(description = "atualiza o leito em que o paciente está")
    public ResponseEntity updatePacienteController(@RequestBody @Valid PacienteUpdateDTO data){
        var body = service.updatePaciente(data);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/deletePaciente/{id}")
    @Transactional
    @Operation(description = "exclui um paciente do sistema")
    public ResponseEntity deletePacienteController(@PathVariable Long id){
        service.deletePaciente(id);

        return ResponseEntity.noContent().build();
    }

}
