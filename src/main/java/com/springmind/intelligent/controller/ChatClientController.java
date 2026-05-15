package com.springmind.intelligent.controller;

import com.springmind.intelligent.entity.LoveEntity;
import com.springmind.intelligent.service.ChatClientService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

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

    @GetMapping("/chat/motivation")
    public ResponseEntity<String> giveMotivation(@RequestParam(value = "topic") String topic, @RequestParam(value = "name") String name){
        return ResponseEntity.ok(chatClientService.spiritualGuru(topic,name));
    }

    @GetMapping("/chat/stream-data")
    public ResponseEntity<Flux<String>> streamData(@RequestParam(value = "q") String q){
        return ResponseEntity.ok(chatClientService.getStreamData(q));
    }

    @GetMapping("/chat/user/session")
    public ResponseEntity<String> getUserSessionResponse(@RequestParam(value = "q") String q, @RequestHeader("userId") String userId){
        return ResponseEntity.ok(chatClientService.userSpec(q,userId));
    }
}
