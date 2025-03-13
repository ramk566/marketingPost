package com.kitchen.utensils.controller;

import com.kitchen.utensils.request.MarketingPost;
import com.kitchen.utensils.request.PostRequest;
import com.kitchen.utensils.request.RequestDto;
import com.kitchen.utensils.message.ResponseMessage;
import com.kitchen.utensils.response.ApiResponse;
import com.kitchen.utensils.service.MarketingPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;

@CrossOrigin(origins = "http://127.0.0.1:5500") // Allow frontend access
@RestController
@RequestMapping("/kitchen-utensils")
public class MarketingPostController {

    private final MarketingPostService kitchenService;

    public MarketingPostController(MarketingPostService kitchenService) {
        this.kitchenService = kitchenService;
    }

    @PostMapping("/marketing-posts")
    public ResponseEntity<ApiResponse<MarketingPost>> generateMarketingPost(@RequestBody PostRequest postRequest) {
        try {
            ApiResponse<MarketingPost> response = kitchenService.generateMarketingPost(postRequest);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(ResponseMessage.ERROR, "An unexpected error occurred", Collections.emptyList()));
        }
    }

    @PostMapping("/prompt")
    public ResponseEntity<ApiResponse<MarketingPost>> generatePrompt(@RequestBody RequestDto requestDto) {
        try {
            ApiResponse<MarketingPost> response = kitchenService.generateMarketingPostsPrompt(requestDto);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(ResponseMessage.ERROR, "An unexpected error occurred", Collections.emptyList()));
        }
    }
}
