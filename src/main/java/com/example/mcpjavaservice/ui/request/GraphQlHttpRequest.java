package com.example.mcpjavaservice.ui.request;

import java.util.Map;

public record GraphQlHttpRequest(
    String query,
    String operationName,
    Map<String, Object> variables
) {
}
