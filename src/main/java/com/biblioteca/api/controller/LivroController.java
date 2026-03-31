package com.biblioteca.api.controller;

import java.util.List;
import com.biblioteca.api.model.Livro;
import com.biblioteca.api.repository.LivroRepository;
import com.biblioteca.api.service.AuditoriaService;
import com.biblioteca.api.service.NotificacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private AuditoriaService auditoriaService;

    @PostMapping
    public ResponseEntity<Livro> criarLivro(@Valid @RequestBody Livro livro) {
        Livro novoLivro = livroRepository.save(livro);

        // Chamada assíncrona do Caio
        notificacaoService.enviarNotificacao(novoLivro);

        // Chamada assíncrona do Nicholas
        auditoriaService.registrarCriacao(novoLivro);

        return ResponseEntity.status(201).body(novoLivro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizarLivro(@PathVariable Long id, @Valid @RequestBody Livro livroAtualizado) {
        return livroRepository.findById(id).map(livro -> {
            livro.setTitulo(livroAtualizado.getTitulo());
            livro.setAno(livroAtualizado.getAno());
            Livro salvo = livroRepository.save(livro);

            // Registro de atualização assíncrono
            auditoriaService.registrarAtualizacao(salvo);

            return ResponseEntity.ok(salvo);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        if (!livroRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        livroRepository.deleteById(id);

        // Registro de exclusão assíncrono
        auditoriaService.registrarExclusao(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }
}