package com.biblioteca.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.biblioteca.api.model.Livro;

@Service
public class AuditoriaService {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaService.class);
    private static final DateTimeFormatter fmt =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Registra auditoria de criacao
    @Async
    public void registrarCriacao(Livro livro) {
        try {
            Thread.sleep(1500);
            log.info("[AUDITORIA] CRIACAO | Livro: '{}' | Ano: {} | Autor ID: {} | Em: {}",
                livro.getTitulo(), livro.getAno(),
                livro.getAutor().getId(),
                LocalDateTime.now().format(fmt));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[AUDITORIA] Registro de criacao interrompido");
        }
    }

    // Registra auditoria de atualizacao
    @Async
    public void registrarAtualizacao(Livro livro) {
        try {
            Thread.sleep(1500);
            log.info("[AUDITORIA] ATUALIZACAO | Livro ID: {} | Novo titulo: '{}' | Em: {}",
                livro.getId(), livro.getTitulo(),
                LocalDateTime.now().format(fmt));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[AUDITORIA] Registro de atualizacao interrompido");
        }
    }

    // Registra auditoria de exclusao
    @Async
    public void registrarExclusao(Long livroId) {
        try {
            Thread.sleep(1500);
            log.info("[AUDITORIA] EXCLUSAO | Livro ID: {} removido | Em: {}",
                livroId, LocalDateTime.now().format(fmt));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[AUDITORIA] Registro de exclusao interrompido");
        }
    }
}
