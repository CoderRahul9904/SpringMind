package com.springmind.intelligent.service;

import com.springmind.intelligent.entity.LoveEntity;

import java.util.List;

public interface ChatClientImpl {
    List<LoveEntity> chat(String prompt);
    String solveQues(String q);
}
