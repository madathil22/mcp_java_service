package com.example.mcpjavaservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mcpjavaservice.ui.request.ChatRequest;
import com.example.mcpjavaservice.ui.response.ChatResponse;

@RestController
@RequestMapping("/api")
@Tag(name = "Company", description = "Chat endpoint for company directory queries")
public class CompanyController {

    private final ChatService chatService;

    public CompanyController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    @Operation(summary = "Handle a chat-style company query")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return chatService.handleMessage(request.message());
    }
}
