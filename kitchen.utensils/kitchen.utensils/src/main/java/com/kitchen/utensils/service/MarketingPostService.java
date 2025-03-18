package com.kitchen.utensils.service;

import com.kitchen.utensils.request.MarketingPost;
import com.kitchen.utensils.request.PostRequest;
import com.kitchen.utensils.request.PromptRequest;
import com.kitchen.utensils.request.RequestDto;
import com.kitchen.utensils.response.ApiResponse;

public interface MarketingPostService {
    public ApiResponse<PromptRequest> generateMarketingPostsPrompt(RequestDto requestDto);
    public ApiResponse<MarketingPost> generateMarketingPost(PostRequest postRequest);
}