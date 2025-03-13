package com.kitchen.utensils.controller;

import com.kitchen.utensils.serviceImpl.ImageGenerationService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/images")
public class ImageGenerationController {

    private final ImageGenerationService imageGenerationService;

    public ImageGenerationController(ImageGenerationService imageGenerationService) {
        this.imageGenerationService = imageGenerationService;
    }

    @PostMapping("/generate")
    public String generateImage(@RequestParam String prompt) {
        return imageGenerationService.generateImage(prompt);
    }
}
