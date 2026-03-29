package com.biblioteca.api.controller;

import com.biblioteca.api.service.NotificacaoService;
import com.biblioteca.api.model.Livro;
import com.biblioteca.api.repository.LivroRepository;
import com.biblioteca.api.repository.AutorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller REST para gerenciar livros
@RestController
@RequestMapping("/livros")
public class LivroController {

    // Injeção de dependências dos repositories
    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private NotificacaoService notificacaoService;


    @PostMapping
    public ResponseEntity<?> criarLivro(@Valid @RequestBody Livro livro) {
    if (!autorRepository.existsById(livro.getAutor().getId())) {
        return ResponseEntity.badRequest().body("Autor invalido!");
    }

    // 1. Salva o livro (caminho critico - rapido)
    Livro novoLivro = livroRepository.save(livro);

    // 2. Dispara notificacao assincrona (nao bloqueia a resposta!)
    // Esta linha retorna IMEDIATAMENTE, a notificacao roda em outra thread
    notificacaoService.notificarLivroCriado(novoLivro);

    // 3. Responde 201 sem esperar a notificacao terminar
    return ResponseEntity.status(201).body(novoLivro);
}

    @GetMapping
    public ResponseEntity<List<Livro>> listarLivros() {
        List<Livro> livros = livroRepository.findAll();
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarLivroPorId(@PathVariable Long id) {
        return livroRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarLivro(@PathVariable Long id,
                                             @Valid @RequestBody Livro livroAtualizado) {
        return livroRepository.findById(id).map(livro -> {
            // Valida se o autor existe
            if (!autorRepository.existsById(livroAtualizado.getAutor().getId())) {
                return ResponseEntity.badRequest().body("Autor inválido!");
            }

            livro.setTitulo(livroAtualizado.getTitulo());
            livro.setAno(livroAtualizado.getAno());
            livro.setAutor(livroAtualizado.getAutor());

            Livro salvo = livroRepository.save(livro);
            return ResponseEntity.ok(salvo);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        if (!livroRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        livroRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
