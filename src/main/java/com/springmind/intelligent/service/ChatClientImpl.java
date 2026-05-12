package com.springmind.intelligent.service;

import com.springmind.intelligent.entity.LoveEntity;

public interface ChatClientImpl {
    LoveEntity chat(String prompt);
}
