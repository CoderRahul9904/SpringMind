package com.springmind.intelligent.service;

import com.springmind.intelligent.entity.LoveEntity;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatClientImpl {
    List<LoveEntity> chat(String prompt);
    String solveQues(String q);
    String spiritualGuru(String topic, String name);
    Flux<String> getStreamData(String topic);
    String userSpec(String s, String userId);
    void storeDocumentedData(List<String> stringData);
    String similaritySearchDemo(String question);
    String naiveRagImplentation(String question);
    String advanceRagResponse(String question);
}
