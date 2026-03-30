package com.biblioteca.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.biblioteca.api.model.Autor;

/**
 * Serviço assíncrono para geração de relatórios de autores.
 * Implementado por: Dev João
 */
@Service
public class RelatorioService {

    private static final Logger log = LoggerFactory.getLogger(RelatorioService.class);

    @Async // Esta anotação faz o método rodar em uma thread separada
    public void gerarRelatorioAutor(Autor autor) {
        try {
            log.info("[ASYNC] Iniciando geracao de relatorio para o autor: {}", autor.getNome());

            // Simula um processamento pesado de 3 segundos (ex: gerar um PDF complexo)
            Thread.sleep(3000); 

            log.info("[ASYNC] Relatorio gerado com sucesso!");
            log.info("[ASYNC] Detalhes do Autor - Nome: {} | Nacionalidade: {}", 
                     autor.getNome(), 
                     autor.getNacionalidade());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[ASYNC] O processamento do relatório foi interrompido.");
        } catch (Exception e) {
            log.error("[ASYNC] Erro inesperado ao gerar relatorio: {}", e.getMessage());
        }
    }
}