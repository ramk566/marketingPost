package com.kitchen.utensils.controller;

import com.kitchen.utensils.request.MarketingPost;
import com.kitchen.utensils.request.PostRequest;
import com.kitchen.utensils.request.PromptRequest;
import com.kitchen.utensils.request.RequestDto;
import com.kitchen.utensils.response.ApiResponse;
import com.kitchen.utensils.message.ResponseMessage;
import com.kitchen.utensils.service.MarketingPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/gpt")
public class MarketingPostController {

    private final MarketingPostService marketingPostService;

    public MarketingPostController(MarketingPostService marketingPostService) {
        this.marketingPostService = marketingPostService;
    }

    @PostMapping("/generate-marketing")
    public ResponseEntity<ApiResponse<MarketingPost>> generate(@RequestBody PostRequest postRequest) {
        try {
            ApiResponse<MarketingPost> response = marketingPostService.generateMarketingPost(postRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(ResponseMessage.ERROR, "An unexpected error occurred", new ArrayList<MarketingPost>()));
        }
    }


    @PostMapping("/generate-prompt")
    public ResponseEntity<ApiResponse<PromptRequest>> generateMarketingPostsPrompt(@RequestBody RequestDto requestDto){
        try {
            ApiResponse<PromptRequest> response = marketingPostService.generateMarketingPostsPrompt(requestDto);
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(ResponseMessage.ERROR, "An unexpected error occurred", new ArrayList<PromptRequest>()));
        }
    }
}
