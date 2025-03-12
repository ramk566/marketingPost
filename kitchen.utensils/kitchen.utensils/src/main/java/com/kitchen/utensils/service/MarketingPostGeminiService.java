package com.kitchen.utensils.service;

import com.kitchen.utensils.request.MarketingPost;
import com.kitchen.utensils.request.MarketingPostRequest;
import com.kitchen.utensils.response.ApiResponse;

public interface MarketingPostGeminiService {
    public ApiResponse<MarketingPost> generateContent(MarketingPostRequest prompt);
}
