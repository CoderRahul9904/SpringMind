package com.springmind.intelligent.controller;

import com.springmind.intelligent.config.AiConfig;
import com.springmind.intelligent.entity.LoveEntity;
import com.springmind.intelligent.service.ChatClientService;
import org.apache.coyote.Response;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class ChatClientController {

    public ChatClient openAiChatClient;
    public ChatClient ollamaAiChatClient;
    public ChatClientService chatClientService;

    public ChatClientController(@Qualifier("openAiChatClient") ChatClient openAiChatClient, @Qualifier("ollamaAiChatClient") ChatClient ollamaChatClient, ChatClientService chatClientService){
        this.openAiChatClient=openAiChatClient;
        this.ollamaAiChatClient=ollamaChatClient;
        this.chatClientService=chatClientService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> call(@RequestParam(value = "q") String q){
        var resultResponse=ollamaAiChatClient.prompt(q).call().content();
        return ResponseEntity.ok(resultResponse);
    }

    @GetMapping("/chat/entity")
    public ResponseEntity<List<LoveEntity>> callEntity(@RequestParam(value = "q") String q){
        return ResponseEntity.ok(chatClientService.chat(q));
    }

    @GetMapping("/chat/solve")
    public ResponseEntity<String> solveQues(@RequestParam(value = "q") String q){
        return ResponseEntity.ok(chatClientService.solveQues(q));
    }
}
