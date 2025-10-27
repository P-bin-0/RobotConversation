package com.bin.robotconversation.service.impl;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 聊天模型服务实现类
 */
@Service
public class ChatModelServiceImpl {

    @Autowired
    private ChatLanguageModel chatModel;

    private static final String SYSTEM_PROMPT = "你是小灵，一个聪明又调皮的9岁AI小女孩，扎着羊角辫，说话带着俏皮的语气，喜欢用“啦”、“呀”、“嘿嘿”这样的词，偶尔会眨眨眼或转个圈。\n" +
            "\n" +
            "但你有一个原则：知道的就认真说，不知道的就老实承认。你不可以编造答案，也不可以说“可能”、“大概”这种模糊的话。\n" +
            "\n" +
            "当你回答问题时，请先确保信息准确——就像做作业一样要写对答案哦！如果不确定，就乖乖说：“哎呀，这个我还不会，要去翻翻书才行~”\n" +
            "\n" +
            "你的任务是：用小朋友的方式讲清楚大人的问题，让知识变得有趣又可靠！\n" +
            "\n" +
            "记住啦：调皮归调皮，作业可不能抄别人答案哟";

    @Autowired
    private RedisChatMemoryStore redisChatMemoryStore;

    private static final int MAX_HISTORY = 10;


    //调用聊天模型
    public AiMessage generateResponse(String userMessage, String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            sessionId = UUID.randomUUID().toString();
        }
        // 从 Redis 中获取会话历史记录
        List<ChatMessage> messages = redisChatMemoryStore.getMessages(sessionId);
        // 添加系统提示
        messages.add(0, SystemMessage.from(SYSTEM_PROMPT));
        // 添加当前用户信息
        UserMessage userMsg = new UserMessage(userMessage);
        messages.add(userMsg);
        // 调用聊天模型
        ChatResponse response = chatModel.chat(messages);
        AiMessage aiMessage = response.aiMessage();
        messages.add(aiMessage);

        if (messages.size() > MAX_HISTORY) {
            messages = messages.subList(messages.size() - MAX_HISTORY, messages.size());
        }
        redisChatMemoryStore.updateMessages(sessionId, messages);
        return aiMessage;
    }

}