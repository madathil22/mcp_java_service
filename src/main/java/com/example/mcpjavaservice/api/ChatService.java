package com.example.mcpjavaservice.api;

import com.example.mcp.CompanyMcpTools;
import com.example.mcpjavaservice.ui.response.ChatResponse;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private static final String SYSTEM_PROMPT = """
        You are the backend assistant for a company directory application.
        Use the available tools whenever the user asks about employees or departments.
        Base your answer only on tool results.
        If the request is ambiguous, ask a short clarifying question.
        If no available tool can satisfy the request, say so plainly.
        Keep responses concise.
        """;

    private final ObjectProvider<ChatClient.Builder> chatClientBuilderProvider;
    private final CompanyMcpTools companyMcpTools;

    public ChatService(ObjectProvider<ChatClient.Builder> chatClientBuilderProvider, CompanyMcpTools companyMcpTools) {
        this.chatClientBuilderProvider = chatClientBuilderProvider;
        this.companyMcpTools = companyMcpTools;
    }

    public ChatResponse handleMessage(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message is required.");
        }

        ChatClient.Builder builder = chatClientBuilderProvider.getIfAvailable();
        if (builder == null) {
            throw new IllegalStateException(
                "No Spring AI chat model is configured. Add a chat model starter such as spring-ai-starter-model-openai or spring-ai-starter-model-ollama and its corresponding properties."
            );
        }

        String reply = builder.build()
            .prompt()
            .system(SYSTEM_PROMPT)
            .user(message)
            .tools(companyMcpTools)
            .call()
            .content();

        return new ChatResponse(reply);
    }
}
