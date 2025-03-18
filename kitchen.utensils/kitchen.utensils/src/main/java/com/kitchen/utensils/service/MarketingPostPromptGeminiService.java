package com.kitchen.utensils.service;

import com.kitchen.utensils.request.PromptRequest;
import com.kitchen.utensils.request.RequestDto;
import com.kitchen.utensils.response.ApiResponse;

public interface MarketingPostPromptGeminiService {
    public ApiResponse<PromptRequest> generatePromptContent(RequestDto promptDto);
}
