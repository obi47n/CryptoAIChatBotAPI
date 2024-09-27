package com.obi.controller;

import com.obi.dto.PromptBody;
import com.obi.response.ApiResponse;
import com.obi.service.ChatbotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai/chat")
public class ChatbotController {
    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> getCoinDetails(@RequestBody PromptBody prompt) throws Exception {

        chatbotService.getCoinDetails(prompt.getPrompt());
        ApiResponse response = new ApiResponse();
        response.setMessage(prompt.getPrompt());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}