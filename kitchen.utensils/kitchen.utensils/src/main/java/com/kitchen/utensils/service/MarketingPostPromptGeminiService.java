package com.kitchen.utensils.service;

import com.kitchen.utensils.request.MarketingPost;
import com.kitchen.utensils.request.RequestDto;
import com.kitchen.utensils.response.ApiResponse;

public interface MarketingPostPromptGeminiService {
    public ApiResponse<MarketingPost> generatePromptContent(RequestDto promptDto);
}
