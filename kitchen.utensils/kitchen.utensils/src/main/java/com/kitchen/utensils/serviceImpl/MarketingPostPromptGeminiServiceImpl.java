package com.kitchen.utensils.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitchen.utensils.request.MarketingPost;
import com.kitchen.utensils.request.RequestDto;
import com.kitchen.utensils.exception.AiApiException;
import com.kitchen.utensils.message.ResponseMessage;
import com.kitchen.utensils.response.ApiResponse;
import com.kitchen.utensils.service.MarketingPostPromptGeminiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import java.util.*;

@Service
public class MarketingPostPromptGeminiServiceImpl implements MarketingPostPromptGeminiService {

    private static final Logger logger = LoggerFactory.getLogger(MarketingPostPromptGeminiServiceImpl.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String API_KEY = "AIzaSyADDv36cfbNwvbHTSODWJFIa69eJxuz6fo";
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    @Override
    public ApiResponse<MarketingPost> generatePromptContent(RequestDto promptDto) {
        logger.info("Generating marketing post prompts for: {}", promptDto);
        try {
            List<MarketingPost> posts = fetchAndParseAiResponse(promptDto);
            logger.info("Successfully generated {} prompts.", posts.size());
            return new ApiResponse<>(ResponseMessage.SUCCESS, ResponseMessage.PROMPT_SUCCESS, posts);
        } catch (AiApiException e) {
            logger.error("Error generating prompts: {}", e.getMessage());
            return new ApiResponse<>(ResponseMessage.ERROR, e.getMessage(), Collections.emptyList());
        }
    }

    private List<MarketingPost> fetchAndParseAiResponse(RequestDto promptDto) throws AiApiException {
        logger.debug("Preparing AI request body.");
        Map<String, Object> requestBody = createRequestBody(promptDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            logger.info("Sending request to AI API.");
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, request, String.class);
            logger.info("Received response from AI API with status: {}", response.getStatusCode());

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new AiApiException(response.getStatusCode().value(), "Failed to generate prompts from AI API.");
            }
            return parseJsonArrayResponse(response.getBody());
        } catch (HttpStatusCodeException e) {
            logger.error("AI API returned error: {}", e.getResponseBodyAsString());
            throw new AiApiException(e.getStatusCode().value(), "AI API returned error: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Unexpected error while processing AI API response.", e);
            throw new AiApiException(500, "Unexpected error while processing AI API response.");
        }
    }


    private Map<String, Object> createRequestBody(RequestDto promptDto) {
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, String>> parts = new ArrayList<>();
        Map<String, String> part = getStringStringMap(promptDto);
        parts.add(part);
        content.put("parts", parts);
        contents.add(content);
        requestBody.put("contents", contents);
        logger.debug("AI request body created successfully.");
        return requestBody;
    }

    private static Map<String, String> getStringStringMap(RequestDto promptDto) {
        Map<String, String> part = new HashMap<>();

        part.put("text", promptDto + " Generate 10 Prompts in JSON array format like:\n" +
                "[{\n" +
                "\"No\": 1,\n" +
                "\"content\": \"...\"\n" +
                "},\n" +
                "{\n" +
                "\"No\": 2,\n" +
                "\"content\": \"...\"\n" +
                "}]\n" +
                "Return only a valid JSON array, no extra text, no JSON.stringify. Prompt starts with generate/create keyword. Each prompt is at least 3 lines.");
        return part;
    }

    private List<MarketingPost> parseJsonArrayResponse(String responseBody) throws AiApiException {
        try {
            logger.debug("Parsing AI API response.");
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode candidates = rootNode.path("candidates");

            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode firstCandidate = candidates.get(0);
                JsonNode contentNode = firstCandidate.path("content");

                if (contentNode.has("parts") && contentNode.get("parts").isArray()) {
                    JsonNode partsArray = contentNode.get("parts");

                    if (!partsArray.isEmpty()) {
                        String responseText = partsArray.get(0).path("text").asText();
                        return extractJsonArray(responseText);
                    }
                }
            }
            throw new AiApiException(500, "AI API response format invalid.");
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse AI API response.", e);
            throw new AiApiException(500, "Failed to parse AI API response.");
        }
    }

    private List<MarketingPost> extractJsonArray(String text) throws JsonProcessingException {
        logger.debug("Extracting JSON array from response text.");
        text = text.trim();
        if (text.startsWith("```json")) {
            text = text.substring(7);
        }
        if (text.endsWith("```")) {
            text = text.substring(0, text.length() - 3);
        }

        text = text.trim();
        if (!text.startsWith("[") || !text.endsWith("]")) {
            return Collections.emptyList();
        }

        return objectMapper.readValue(text, new TypeReference<List<MarketingPost>>() {});
    }
}
