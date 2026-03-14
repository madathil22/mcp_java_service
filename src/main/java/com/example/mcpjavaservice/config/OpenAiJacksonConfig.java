package com.example.mcpjavaservice.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenAiJacksonConfig {

    @Bean
    Jackson2ObjectMapperBuilderCustomizer openAiRequestCustomizer() {
        return builder -> builder.mixIn(OpenAiApi.ChatCompletionRequest.class, ChatCompletionRequestMixin.class);
    }

    private abstract static class ChatCompletionRequestMixin {

        @JsonIgnore
        abstract Map<String, Object> extraBody();
    }
}
