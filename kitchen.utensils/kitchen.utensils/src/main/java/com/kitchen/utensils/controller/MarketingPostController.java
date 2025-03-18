//package com.kitchen.utensils.controller;
//
//import com.kitchen.utensils.exception.AiApiException;
//import com.kitchen.utensils.request.MarketingPost;
//import com.kitchen.utensils.request.RequestDto;
//import com.kitchen.utensils.request.MarketingPostRequest;
//import com.kitchen.utensils.response.ApiResponse;
//import com.kitchen.utensils.message.ResponseMessage;
//import com.kitchen.utensils.service.MarketingPostPromptGeminiService;
//import com.kitchen.utensils.service.MarketingPostGeminiService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Collections;
//
//@CrossOrigin(origins = "http://127.0.0.1:5500")
//@RestController
//@RequestMapping("/api/gpt")
//public class MarketingPostController {
//
//    private final MarketingPostGeminiService marketingPostService;
//    private final MarketingPostPromptGeminiService marketingPostPromptService;
//
//    public MarketingPostController(MarketingPostGeminiService marketingPostService,
//                                   MarketingPostPromptGeminiService marketingPostPromptService) {
//        this.marketingPostService = marketingPostService;
//        this.marketingPostPromptService = marketingPostPromptService;
//    }
//
//    @PostMapping("/generate-marketing")
//    public ResponseEntity<ApiResponse<MarketingPost>> generate(@RequestBody MarketingPostRequest marketingPostRequest) {
//        try {
//            ApiResponse<MarketingPost> response = marketingPostService.generateContent(marketingPostRequest);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return handleGenericException(e);
//        }
//    }
//
//    @PostMapping("/generate-prompt")
//    public ResponseEntity<ApiResponse<MarketingPost>> generatePrompt(@RequestBody RequestDto promptDto) {
//        try {
//            ApiResponse<MarketingPost> response = marketingPostPromptService.generatePromptContent(promptDto);
//            return ResponseEntity.ok(response);
//        }catch (Exception e) {
//            return handleGenericException(e);
//        }
//    }
//
//
//    private ResponseEntity<ApiResponse<MarketingPost>> handleGenericException(Exception e) {
//        ApiResponse<MarketingPost> errorResponse = new ApiResponse<>(
//                ResponseMessage.ERROR,
//                "An unexpected error occurred.",
//                Collections.emptyList()
//        );
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//    }
//}
