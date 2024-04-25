package br.com.hapvida.controledevisitas.ControleDeVisitas.controller;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteUpdateDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.service.PacienteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/paciente")
public class PacienteController {

    @Autowired
    PacienteService service;

    @GetMapping("/allPacientes")
    public ResponseEntity getAllPAcientesController(){
        var body = service.getAllPacientes();
        return ResponseEntity.ok(body);

    }

    @GetMapping("/pacienteByNome/{nome}")
    public ResponseEntity getPacienteByNomeController(@PathVariable String nome){
        var body = service.getPacienteByNome(nome);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/savePaciente")
    @Transactional
    public ResponseEntity savePacienteController(@RequestBody PacienteRequestDTO data, UriComponentsBuilder uriComponentsBuilder){
        var body = service.registerNewPaciente(data);
        var uri = uriComponentsBuilder.path("{id}").buildAndExpand(body.id()).toUri();

        return ResponseEntity.created(uri).body(body);
    }

    @PutMapping("/updatePaciente")
    @Transactional
    public ResponseEntity updatePacienteController(@RequestBody PacienteUpdateDTO data){
        var body = service.updatePaciente(data);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/deletePaciente/{id}")
    @Transactional
    public ResponseEntity deletePacienteController(@PathVariable Long id){
        service.deletePaciente(id);

        return ResponseEntity.noContent().build();
    }

}
