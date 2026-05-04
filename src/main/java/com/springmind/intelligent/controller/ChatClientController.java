package com.springmind.intelligent.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ChatClientController {

    public ChatClient chatClient;

    public ChatClientController(ChatClient.Builder builder){
        this.chatClient=builder.build();
    }

    @GetMapping("/chat")
    public ResponseEntity<String> call(@RequestParam(value = "q") String q){
        var resultResponse=chatClient.prompt(q).call().content();
        return ResponseEntity.ok(resultResponse);
    }
}
