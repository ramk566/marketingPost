package com.kitchen.utensils.controller;

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

@RestController
@RequestMapping("/api/kitchen-utensils")
public class MarketingPostGeminiController {

    private final MarketingPostGeminiService marketingPostService;
    private final MarketingPostPromptGeminiService marketingPostPromptService;

    public MarketingPostGeminiController(MarketingPostGeminiService marketingPostService, MarketingPostPromptGeminiService marketingPostPromptService) {
        this.marketingPostService = marketingPostService;
        this.marketingPostPromptService = marketingPostPromptService;
    }

    @PostMapping("/generate-marketing")
    public ResponseEntity<ApiResponse<MarketingPost>> generate(@RequestBody MarketingPostRequest marketingPostRequest) {
        try {
            ApiResponse<MarketingPost> response = marketingPostService.generateContent(marketingPostRequest);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(ResponseMessage.ERROR, "An unexpected error occurred", Collections.emptyList()));
        }
    }

    @PostMapping("/generate-prompt")
    public ResponseEntity<ApiResponse<MarketingPost>> generatePrompt(@RequestBody RequestDto promptDto) {
        try {
            ApiResponse<MarketingPost> response = marketingPostPromptService.generatePromptContent(promptDto);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(ResponseMessage.ERROR, "An unexpected error occurred", Collections.emptyList()));
        }
    }
}
