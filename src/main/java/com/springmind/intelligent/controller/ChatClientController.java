package com.springmind.intelligent.controller;

import com.springmind.intelligent.config.AiConfig;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ChatClientController {

    public ChatClient openAiChatClient;
    public ChatClient ollamaAiChatClient;

    public ChatClientController(@Qualifier("openAiChatClient") ChatClient openAiChatClient, @Qualifier("ollamaAiChatClient") ChatClient ollamaChatClient){
        this.openAiChatClient=openAiChatClient;
        this.ollamaAiChatClient=ollamaChatClient;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> call(@RequestParam(value = "q") String q){
        var resultResponse=ollamaAiChatClient.prompt(q).call().content();
        return ResponseEntity.ok(resultResponse);
    }
}
