package com.team27.pillmaxxer.controller;

import com.team27.pillmaxxer.service.ImageScanService;
import com.team27.pillmaxxer.dto.ImageScanRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/image-scan")
public class ImageScanController {
    private final ImageScanService imageScanService;

    public ImageScanController(ImageScanService imageScanService) {
        this.imageScanService = imageScanService;
    }

    /**
     * API to scan an image for text.
     * Example Request: POST /api/image-scan/upload
     * Request body: { "imageUrl": "http://example.com/image.jpg" }
     */
    @PostMapping("/upload")
    public String scanImageForText(@RequestBody ImageScanRequest req) {
        return imageScanService.extractText(req.getImageUrl());
    }
}