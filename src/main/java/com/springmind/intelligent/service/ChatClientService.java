package com.springmind.intelligent.service;

import com.springmind.intelligent.entity.LoveEntity;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
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

    @Override
    public String similaritySearchDemo(String question){
        return chatClient
                .prompt()
                .advisors(QuestionAnswerAdvisor.builder(this.vectorStore).searchRequest(SearchRequest.builder().topK(5).query(question).similarityThreshold(0.6).build()).build())
                .user(u -> u.text(this.userPrompt).params(Map.of("question",question)))
                .call()
                .content();
    }

    // Naive RAG
    // In Naive Rag the flow is simple, user query the data it get search in vector db through similarity Search.
    // Below is the implementation of Naive Rag which is similar to QuestionAnswerAdvisor but uses RetrievalAugmentationAdvisor
    @Override
    public String naiveRagImplentation(String question){
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor=RetrievalAugmentationAdvisor
                .builder()
                .documentRetriever(VectorStoreDocumentRetriever
                        .builder()
                        .vectorStore(this.vectorStore)
                        .topK(5)
                        .build()
                )
                .queryAugmenter(ContextualQueryAugmenter
                        .builder()
                        .allowEmptyContext(true)
                        .build())
                .build();
        return chatClient
                .prompt()
                .advisors(retrievalAugmentationAdvisor)
                .user(u->u.text("Answer me in detail: {question}").param("question",question))
                .call()
                .content();
    }


    // Advance RAG Implementation --> Pre-retrival, Retrival, Post-retrival and Generation Step.
    // RAG PIPELINE IMPLEMENTATION
    @Override
    public String advanceRagResponse(String question){
        Advisor advisor=RetrievalAugmentationAdvisor
                .builder()
                .queryTransformers(RewriteQueryTransformer
                        .builder()
                        .chatClientBuilder(chatClient.mutate().clone())
                        .build()
                )
                .queryExpander(MultiQueryExpander.builder()
                        .chatClientBuilder(chatClient.mutate().clone())
                        .build()
                )
                .documentRetriever(VectorStoreDocumentRetriever
                        .builder()
                        .topK(5)
                        .vectorStore(vectorStore)
                        .build()
                )
                .documentJoiner(new ConcatenationDocumentJoiner())
                .queryAugmenter(ContextualQueryAugmenter.builder().allowEmptyContext(true).build())
                .build();
        return chatClient
                .prompt()
                .advisors(advisor)
                .user(u->u.text("{question}").param("question",question))
                .call()
                .content();
    }
}
