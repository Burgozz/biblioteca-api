package main.java.com.biblioteca.api.service;

import com.biblioteca.api.model.Livro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

// @Service = este bean e gerenciado pelo Spring
@Service
public class NotificacaoService {

    // Logger para imprimir mensagens no console
    private static final Logger log = LoggerFactory.getLogger(NotificacaoService.class);

    // @Async = este metodo roda em outra thread, sem bloquear a requisicao
    @Async
    public void notificarLivroCriado(Livro livro) {
        try {
            log.info("[ASYNC] Iniciando notificacao para o livro: {}", livro.getTitulo());

            // Simula uma operacao lenta (ex: enviar email, chamar API externa)
            // Em producao, aqui estaria a logica real de notificacao
            Thread.sleep(2000); // 2 segundos de espera simulada

            log.info("[ASYNC] Notificacao enviada com sucesso para: '{}' (ano: {})",
                     livro.getTitulo(), livro.getAno());

        } catch (InterruptedException e) {
            // Restaura o status de interrupcao da thread
            Thread.currentThread().interrupt();
            log.error("[ASYNC] Notificacao interrompida para: {}", livro.getTitulo());
        } catch (Exception e) {
            // Captura qualquer outro erro sem derrubar a aplicacao
            log.error("[ASYNC] Erro ao enviar notificacao para '{}': {}",
                      livro.getTitulo(), e.getMessage());
        }
    }
}
