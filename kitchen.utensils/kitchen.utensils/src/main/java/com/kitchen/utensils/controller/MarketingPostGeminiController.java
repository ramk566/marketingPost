package com.kitchen.utensils.controller;

import com.kitchen.utensils.request.MarketingPost;
import com.kitchen.utensils.request.PromptRequest;
import com.kitchen.utensils.request.RequestDto;
import com.kitchen.utensils.request.MarketingPostRequest;
import com.kitchen.utensils.response.ApiResponse;
import com.kitchen.utensils.message.ResponseMessage;
import com.kitchen.utensils.service.MarketingPostPromptGeminiService;
import com.kitchen.utensils.service.MarketingPostGeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/gemini")
public class MarketingPostGeminiController {

    private final MarketingPostPromptGeminiService marketingPostPromptService;

    private final MarketingPostGeminiService marketingPostGeminiService;

    public MarketingPostGeminiController(MarketingPostPromptGeminiService marketingPostPromptService, MarketingPostGeminiService marketingPostGeminiService) {
        this.marketingPostPromptService = marketingPostPromptService;
        this.marketingPostGeminiService = marketingPostGeminiService;
    }

    @PostMapping("/generate-marketing")
    public ResponseEntity<ApiResponse<MarketingPost>> generate(@RequestBody MarketingPostRequest marketingPostRequest) {
        try {
            ApiResponse<MarketingPost> response = marketingPostGeminiService.generateMarketingPostContent(marketingPostRequest);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(ResponseMessage.ERROR, "An unexpected error occurred", new ArrayList<MarketingPost>()));
        }
    }


    @PostMapping("/generate-prompt")
    public ResponseEntity<ApiResponse<PromptRequest>> generatePrompt(@RequestBody RequestDto promptDto) {
        try {
            ApiResponse<PromptRequest> response = marketingPostPromptService.generatePromptContent(promptDto);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(ResponseMessage.ERROR, "An unexpected error occurred", Collections.emptyList()));
        }
    }


}
