package com.bin.robotconversation.controller;

import com.bin.robotconversation.model.dto.ApiResponse;
import com.bin.robotconversation.model.dto.ChatRequest;
import com.bin.robotconversation.service.impl.ChatModelServiceImpl;
import dev.langchain4j.data.message.AiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 聊天模型控制器
 */
@RestController
@RequestMapping("/chat-model")
public class ChatModelController {

    @Autowired
    private ChatModelServiceImpl chatModelService;

    //调用聊天模型
    @PostMapping("/generate-response")
    public ResponseEntity<ApiResponse<String>> generateResponse(@RequestBody ChatRequest chatRequest) {
        try {
            // 参数校验
            if (chatRequest.getMessage() == null || chatRequest.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("用户消息不能为空"));
            }

            String sessionId = chatRequest.getSessionId();

            AiMessage aiMessage = chatModelService.generateResponse(chatRequest.getMessage(), sessionId);

            // 构造返回结果
            var result = ApiResponse.success(aiMessage.text());


            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("抱歉，服务暂时不可用，请稍后再试。"));
        }
    }

}