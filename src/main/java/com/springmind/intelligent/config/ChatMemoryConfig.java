package com.springmind.intelligent.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryConfig {

    @Bean(name = "inMemoryChatMemoryRepository")
    public ChatMemoryRepository chatMemoryRepository(){
        return new InMemoryChatMemoryRepository();
    }

    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository inMemoryChatMemoryRepository){
        return MessageWindowChatMemory.builder().chatMemoryRepository(inMemoryChatMemoryRepository).maxMessages(10).build();
    }
}
