package com.springmind.intelligent.service;

import com.springmind.intelligent.entity.LoveEntity;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ChatClientService implements ChatClientImpl{
    public ChatClient chatClient;

    public ChatClientService(
            @Qualifier("ollamaAiChatClient") ChatClient chatClient
    ) {
        this.chatClient = chatClient;
    }

    @Override
    public LoveEntity chat(String prompt) {
        return chatClient.prompt(prompt).call().entity(LoveEntity.class);
    }
}
