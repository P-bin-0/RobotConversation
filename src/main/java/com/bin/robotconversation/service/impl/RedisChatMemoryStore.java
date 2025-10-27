package com.bin.robotconversation.service.impl;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class RedisChatMemoryStore implements ChatMemoryStore {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    private static final Duration SESSION_TTL = Duration.ofHours(24);

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String key = "chat:memory:" + memoryId;
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) {
            return new ArrayList<>();
        }
        return ChatMessageDeserializer.messagesFromJson(json);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String key = "chat:memory:" + memoryId;
        String json = ChatMessageSerializer.messagesToJson(messages);
        redisTemplate.opsForValue().set(key, json, SESSION_TTL);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        String key = "chat:memory:" + memoryId;
        redisTemplate.delete(key);
    }

}