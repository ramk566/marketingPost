package com.kitchen.utensils.service;

import com.kitchen.utensils.request.MarketingPost;
import com.kitchen.utensils.request.PostRequest;
import com.kitchen.utensils.request.RequestDto;
import com.kitchen.utensils.exception.AiApiException;
import com.kitchen.utensils.response.ApiResponse;

public interface MarketingPostService {
    public ApiResponse<MarketingPost> generateMarketingPostsPrompt(RequestDto requestDto) throws AiApiException;
    public ApiResponse<MarketingPost> generateMarketingPost(PostRequest postRequest) throws AiApiException;
}