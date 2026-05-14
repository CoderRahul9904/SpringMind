package com.springmind.intelligent.config;

import com.springmind.intelligent.advisors.MemoryAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfig {

    public Logger logger= LoggerFactory.getLogger(AiConfig.class);

    @Bean(name = "openAiChatClient")
    public ChatClient openAiChatModel(OpenAiChatModel openAiChatModel){
        return ChatClient.builder(openAiChatModel).build();
    }

    @Bean(name = "ollamaAiChatClient")
    public ChatClient ollamaAiChatModel(OllamaChatModel ollamaChatModel,ChatMemory chatMemory){
        this.logger.info("ChatMemory Implementation Class: {}", chatMemory.getClass().getName());

        MessageChatMemoryAdvisor messageChatMemoryAdvisor=MessageChatMemoryAdvisor.builder(chatMemory).build();

        return ChatClient
                .builder(ollamaChatModel)
                .defaultAdvisors(messageChatMemoryAdvisor,new SimpleLoggerAdvisor(), new SafeGuardAdvisor(List.of("sex","fuck")))
                .build();
    }
}
