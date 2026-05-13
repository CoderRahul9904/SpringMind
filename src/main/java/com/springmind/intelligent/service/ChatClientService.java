package com.springmind.intelligent.service;

import com.springmind.intelligent.entity.LoveEntity;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatClientService implements ChatClientImpl{
    public ChatClient chatClient;

    public ChatClientService(
            @Qualifier("ollamaAiChatClient") ChatClient chatClient
    ) {
        this.chatClient = chatClient;
    }

    @Override
    public List<LoveEntity> chat(String prompt) {
        return chatClient.prompt(prompt).call().entity(new ParameterizedTypeReference<List<LoveEntity>>() {
        });
    }

    // Use of PromptTemplate
    @Override
    public String solveQues(String q) {
        String queryStr="Act as expert in Java Programming and Solve the Question: {q}";
        return chatClient.prompt().user(u-> u.text(queryStr).param("q",q)).call().content();
    }
}
