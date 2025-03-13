package com.kitchen.utensils.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitchen.utensils.request.MarketingPost;
import com.kitchen.utensils.message.ResponseMessage;
import com.kitchen.utensils.request.MarketingPostRequest;
import com.kitchen.utensils.response.ApiResponse;
import com.kitchen.utensils.service.MarketingPostGeminiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class MarketingPostGeminiServiceImpl implements MarketingPostGeminiService {

    private static final Logger logger = LoggerFactory.getLogger(MarketingPostGeminiServiceImpl.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ImageGenerationService imageGenerationService;
    private static final String API_KEY = "AIzaSyADDv36cfbNwvbHTSODWJFIa69eJxuz6fo";
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    public MarketingPostGeminiServiceImpl(ImageGenerationService imageGenerationService) {
        this.imageGenerationService = imageGenerationService;
    }

    @Override
    public ApiResponse<MarketingPost> generateContent(MarketingPostRequest prompt) {
        logger.info("Generating marketing post content for request: {}", prompt);

        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, String>> parts = new ArrayList<>();
        Map<String, String> part = new HashMap<>();

        part.put("text", prompt + " Generate 10 Marketing Posts in JSON array format...");
        parts.add(part);
        content.put("parts", parts);
        contents.add(content);
        requestBody.put("contents", contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            logger.info("Sending request to AI API...");
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, request, String.class);
            logger.info("Received response from AI API with status: {}", response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<MarketingPost> posts = parseJsonArrayResponse(response.getBody());
                if (!posts.isEmpty()) {
                    logger.info("Successfully generated {} marketing posts.", posts.size());

                    for (MarketingPost post : posts) {
                        String imageUrl = imageGenerationService.generateImage(post.getContent());
                        post.setImageUrl(imageUrl);
                    }

                    return new ApiResponse<>(ResponseMessage.SUCCESS, ResponseMessage.MARKETING_POST_SUCCESS, posts);
                } else {
                    logger.warn("Failed to extract marketing posts from response.");
                    return new ApiResponse<>(ResponseMessage.ERROR, ResponseMessage.MARKETING_POST_EXTRACTION_ERROR, Collections.emptyList());
                }
            } else {
                logger.error("AI API response error: {}", response.getStatusCode());
                return new ApiResponse<>(ResponseMessage.ERROR, ResponseMessage.MARKETING_POST_GENERATION_ERROR, Collections.emptyList());
            }
        } catch (Exception e) {
            logger.error("Error while communicating with AI API", e);
            return new ApiResponse<>(ResponseMessage.ERROR, ResponseMessage.MARKETING_POST_GENERATION_ERROR, Collections.emptyList());
        }
    }

    private List<MarketingPost> parseJsonArrayResponse(String responseBody) {
        try {
            logger.debug("Parsing AI API response...");
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
        } catch (Exception e) {
            logger.error("Failed to parse AI API response", e);
        }
        return Collections.emptyList();
    }

    private List<MarketingPost> extractJsonArray(String text) throws JsonProcessingException {
        text = text.trim();
        if (text.startsWith("```json")) {
            text = text.substring(7);
        }
        if (text.endsWith("```")) {
            text = text.substring(0, text.length() - 3);
        }

        text = text.trim();
        if (!text.startsWith("[") || !text.endsWith("]")) {
            logger.warn("Response does not contain a valid JSON array.");
            return Collections.emptyList();
        }

        logger.debug("Extracting JSON array from response text.");
        return objectMapper.readValue(text, new TypeReference<List<MarketingPost>>() {});
    }
}
