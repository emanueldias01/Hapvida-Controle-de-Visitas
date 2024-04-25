package br.com.hapvida.controledevisitas.ControleDeVisitas.controller;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.VisitanteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.service.VisitanteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/visitante")
public class VisitanteController {

    @Autowired
    VisitanteService service;

    @GetMapping("/allVisitantes")
    public ResponseEntity getAllVisitantesController(){
        var body = service.getAllVisitantes();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/visitanteByNome/{nome}")
    public ResponseEntity getVisitanteByNomeController(@PathVariable String nome){
        var body = service.getVisitanteByNome(nome);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/saveVisitante")
    @Transactional
    public ResponseEntity saveVisitanteController(@RequestBody VisitanteRequestDTO data, UriComponentsBuilder uriComponentsBuilder){
        var body = service.registerNewVisitante(data);
        var uri = uriComponentsBuilder.path("{id}").buildAndExpand(body.id()).toUri();

        return ResponseEntity.created(uri).body(body);
    }

    @DeleteMapping("deleteVisitante/{id}")
    @Transactional
    public ResponseEntity deleteVisitanteByIdController(@PathVariable Long id){
        service.deleteVisitante(id);

        return ResponseEntity.noContent().build();
    }

}
