package com.biblioteca.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; // Import necessário para o seu serviço
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.api.model.Autor;
import com.biblioteca.api.repository.AutorRepository;
import com.biblioteca.api.service.RelatorioService;

import jakarta.validation.Valid;

// @RestController indica que esta classe é um controlador REST
@RestController
// @RequestMapping define o caminho base para todos os endpoints deste controller
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private RelatorioService relatorioService; // Injeção do seu novo serviço assíncrono

    @PostMapping
    public ResponseEntity<Autor> criarAutor(@Valid @RequestBody Autor autor) {
        // 1. Salva o autor no banco (operação síncrona/rápida)
        Autor novoAutor = autorRepository.save(autor);

        // 2. Dispara a geração do relatório em background (operação assíncrona/lenta)
        // Graças ao @Async no serviço, o Spring não espera os 3 segundos aqui
        relatorioService.gerarRelatorioAutor(novoAutor);

        // 3. Responde 201 imediatamente para o usuário
        return ResponseEntity.status(201).body(novoAutor);
    }

    @GetMapping
    public ResponseEntity<List<Autor>> listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        return ResponseEntity.ok(autores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> buscarAutorPorId(@PathVariable Long id) {
        return autorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> atualizarAutor(@PathVariable Long id,
                                                 @Valid @RequestBody Autor autorAtualizado) {
        return autorRepository.findById(id).map(autor -> {
            autor.setNome(autorAtualizado.getNome());
            autor.setNacionalidade(autorAtualizado.getNacionalidade());
            Autor salvo = autorRepository.save(autor);
            return ResponseEntity.ok(salvo);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAutor(@PathVariable Long id) {
        if (!autorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        autorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}