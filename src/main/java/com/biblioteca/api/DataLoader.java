package com.biblioteca.api;

import com.biblioteca.api.model.ApiKey;
import com.biblioteca.api.model.Autor;
import com.biblioteca.api.model.Livro;
import com.biblioteca.api.repository.ApiKeyRepository;
import com.biblioteca.api.repository.AutorRepository;
import com.biblioteca.api.repository.LivroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DataLoader {

    @Bean
    @Profile("!prod")
    public CommandLineRunner loadData(AutorRepository autorRepository,
                                      LivroRepository livroRepository,
                                      ApiKeyRepository apiKeyRepository) {
        return args -> {
            Autor a1 = new Autor("Machado de Assis", "Brasileiro");
            Autor a2 = new Autor("Clarice Lispector", "Brasileira");
            autorRepository.save(a1);
            autorRepository.save(a2);
            livroRepository.save(new Livro("Dom Casmurro", 1899, a1));
            livroRepository.save(new Livro("Memorias Postumas", 1881, a1));
            livroRepository.save(new Livro("A Hora da Estrela", 1977, a2));
            apiKeyRepository.save(new ApiKey("sk-biblioteca-key-12345", "Chave de Teste 1"));
            apiKeyRepository.save(new ApiKey("sk-biblioteca-key-67890", "Chave de Teste 2"));
            System.out.println("==== Dados carregados! ====");
            System.out.println("Chave 1: sk-biblioteca-key-12345");
            System.out.println("Chave 2: sk-biblioteca-key-67890");
        };
    }
}