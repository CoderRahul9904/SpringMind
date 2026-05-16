package com.springmind.intelligent.service;

import com.springmind.intelligent.entity.LoveEntity;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;


@Service
public class ChatClientService implements ChatClientImpl{
    public ChatClient chatClient;
    public VectorStore vectorStore;

    @Value("classpath:/prompts/user-prompt.st")
    public Resource userPrompt;

    @Value("classpath:/prompts/system-prompt.st")
    public Resource systemPrompt;


    public ChatClientService(
            @Qualifier("ollamaAiChatClient") ChatClient chatClient,
            VectorStore vectorStore
    ) {
        this.chatClient = chatClient;
        this.vectorStore=vectorStore;
    }

    @Override
    public List<LoveEntity> chat(String prompt) {
        return chatClient.prompt(prompt).call().entity(new ParameterizedTypeReference<List<LoveEntity>>() {
        });
    }

    // Use of fluent API to give prompt
    @Override
    public String solveQues(String q) {
        String queryStr="Act as expert in Java Programming and Solve the Question: {q}";
        return chatClient.prompt().user(u-> u.text(queryStr).param("q",q)).call().content();
    }

    // Use of PromptTemplate
    @Override
    public String spiritualGuru(String topic, String name){
        return chatClient
                .prompt()
                .user(u -> u.text(this.userPrompt).params(Map.of("name",name,"topic",topic)))
                .system(s -> s.text(this.systemPrompt))
                .call()
                .content();
    }

    @Override
    public Flux<String> getStreamData(String topic) {
        return chatClient
                .prompt()
                .user(u-> u.text("Tell about {topic}").param("topic",topic))
                .stream()
                .content();
    }

    @Override
    public String userSpec(String s, String userId){
        return chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,userId))
                .user(u-> u.text("{s}").param("s",s))
                .system(a->a.text("Answer to each question"))
                .call()
                .content();
    }


    @Override
    public void storeDocumentedData(List<String> stringData){
        List<Document> documentedData=stringData.stream().map(Document::new).toList();
        try {
            this.vectorStore.add(documentedData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
