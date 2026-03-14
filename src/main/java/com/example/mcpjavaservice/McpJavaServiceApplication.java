package com.example.mcpjavaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.mcpjavaservice", "com.example.mcp"})
public class McpJavaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpJavaServiceApplication.class, args);
    }
}
