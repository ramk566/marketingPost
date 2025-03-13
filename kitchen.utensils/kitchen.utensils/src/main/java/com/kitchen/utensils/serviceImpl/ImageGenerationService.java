package com.kitchen.utensils.serviceImpl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Service
public class ImageGenerationService {
    private static final String API_URL = "https://api.imagepig.com/";
    private static final String API_KEY = "4c357af9-3dba-47d6-90a5-1223f260af13";

    private final RestTemplate restTemplate;

    public ImageGenerationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateImage(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt+"Generate kitchen Utensils images");

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, requestEntity, String.class);

        return response.getBody();
    }
}
