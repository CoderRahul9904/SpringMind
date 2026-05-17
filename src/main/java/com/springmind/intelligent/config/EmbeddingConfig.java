package com.springmind.intelligent.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingConfig {

    @Bean
    public EmbeddingModel embeddingModel(
            OllamaEmbeddingModel ollamaEmbeddingModel
    ) {
        return ollamaEmbeddingModel;
    }
}
