package com.example.mcpjavaservice.api;

import com.example.mcpjavaservice.ui.request.ChatRequest;
import com.example.mcpjavaservice.ui.request.GraphQlHttpRequest;
import com.example.mcpjavaservice.ui.response.ChatResponse;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Company", description = "Chat and GraphQL endpoints for company directory queries")
public class CompanyController {

    private final ChatService chatService;
    private final GraphQlSource graphQlSource;

    public CompanyController(ChatService chatService, GraphQlSource graphQlSource) {
        this.chatService = chatService;
        this.graphQlSource = graphQlSource;
    }

    @PostMapping("/chat")
    @Operation(summary = "Handle a chat-style company query")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return chatService.handleMessage(request.message());
    }

    @PostMapping("/graphql")
    @Operation(summary = "Execute a GraphQL query through a REST endpoint")
    public Map<String, Object> graphql(@RequestBody GraphQlHttpRequest request) {
        if (request == null || request.query() == null || request.query().isBlank()) {
            throw new IllegalArgumentException("GraphQL query is required.");
        }

        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
            .query(request.query())
            .operationName(request.operationName())
            .variables(request.variables() != null ? request.variables() : Collections.emptyMap())
            .build();

        ExecutionResult executionResult = graphQlSource.graphQl().execute(executionInput);
        return executionResult.toSpecification();
    }
}
