package com.email.writer.Service;

import com.email.writer.Model.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient webClient;


    @Value("${gemini.api.url}")
    private String geminiApiurl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailGeneratorService(WebClient.Builder webClientBuilder) {
        this.webClient = WebClient.builder().build();
    }

    public String emailGenerator(EmailRequest emailRequest) {
        //Build Prompt
        String prompt = buildPrompt(emailRequest);

        //craft Request

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        //Requesting to Gemini And Getting response

        String response = webClient.post()
                .uri(geminiApiurl + geminiApiKey)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //Return Response

        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {

        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);

            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        }
        catch (Exception e){
            return e.getMessage();
        }


    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a email reply for the following content . Please don't generate subject line ");

        if (emailRequest.getTone() != null || !emailRequest.getTone().isEmpty())
            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone");
        else {
            prompt.append("Use a ").append("professional").append(" tone");
        }
        prompt.append("\nOrignal email: \n").append(emailRequest.getEmailContent());

        return prompt.toString();
    }
}
