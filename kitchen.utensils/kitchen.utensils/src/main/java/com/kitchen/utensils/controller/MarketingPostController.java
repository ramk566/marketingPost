package com.kitchen.utensils.controller;

import com.kitchen.utensils.exception.AiApiException;
import com.kitchen.utensils.request.MarketingPost;
import com.kitchen.utensils.request.RequestDto;
import com.kitchen.utensils.request.MarketingPostRequest;
import com.kitchen.utensils.response.ApiResponse;
import com.kitchen.utensils.message.ResponseMessage;
import com.kitchen.utensils.service.MarketingPostPromptGeminiService;
import com.kitchen.utensils.service.MarketingPostGeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/gpt")
public class MarketingPostController {

    private final MarketingPostGeminiService marketingPostService;
    private final MarketingPostPromptGeminiService marketingPostPromptService;

    public MarketingPostController(MarketingPostGeminiService marketingPostService, MarketingPostPromptGeminiService marketingPostPromptService) {
        this.marketingPostService = marketingPostService;
        this.marketingPostPromptService = marketingPostPromptService;
    }

    @PostMapping("/generate-marketing")
    public ResponseEntity<ApiResponse<MarketingPost>> generate(@RequestBody MarketingPostRequest marketingPostRequest) throws AiApiException {
        ApiResponse<MarketingPost> response = marketingPostService.generateContent(marketingPostRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-prompt")
    public ResponseEntity<ApiResponse<MarketingPost>> generatePrompt(@RequestBody RequestDto promptDto) throws AiApiException {
        ApiResponse<MarketingPost> response = marketingPostPromptService.generatePromptContent(promptDto);
        return ResponseEntity.ok(response);
    }
}

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(AiApiException.class)
    public ResponseEntity<ApiResponse<MarketingPost>> handleAiApiException(AiApiException ex) {
        ApiResponse<MarketingPost> response = new ApiResponse<>(ResponseMessage.ERROR, ex.getMessage(), Collections.emptyList());
        return ResponseEntity.internalServerError().body(response);
    }
}
