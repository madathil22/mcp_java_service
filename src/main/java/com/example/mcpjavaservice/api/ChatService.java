package com.example.mcpjavaservice.api;

import com.example.mcp.CompanyMcpGraphQlTools;
import com.example.mcpjavaservice.ui.response.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private static final String SYSTEM_PROMPT = """
        You are the backend assistant for a company directory application.
        Use the available GraphQL-backed tools whenever the user asks about employees or departments.
        Prefer the employee query tool for employee lookups and filters.
        Prefer the department query or count-by-department tool for department-focused questions.
        Base your answer only on tool results.
        If the request is ambiguous, ask a short clarifying question.
        If no available tool can satisfy the request, say so plainly.
        Keep responses concise.
        """;

    private final ObjectProvider<ChatClient.Builder> chatClientBuilderProvider;
    private final CompanyMcpGraphQlTools companyMcpGraphQlTools;

    public ChatService(ObjectProvider<ChatClient.Builder> chatClientBuilderProvider, CompanyMcpGraphQlTools companyMcpGraphQlTools) {
        this.chatClientBuilderProvider = chatClientBuilderProvider;
        this.companyMcpGraphQlTools = companyMcpGraphQlTools;
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
            .tools(companyMcpGraphQlTools)
            .call()
            .content();

        return new ChatResponse(reply);
    }
}
