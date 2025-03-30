package com.team27.pillmaxxer.controller;

import com.team27.pillmaxxer.service.ImageScanService;
import com.team27.pillmaxxer.dto.ImageScanRequest;
import com.team27.pillmaxxer.model.Prescription;

import org.springframework.web.multipart.MultipartFile;
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
     * Content-Type: multipart/form-data
     * Request body: { "imageFile": (binary image file) }
     */
    @PostMapping("/upload")
    public Prescription scanImageForText(@RequestParam("imageFile") MultipartFile imageFile) {
        System.out.println("Received another request for " + imageFile);
        return imageScanService.scanImage(imageFile);
    }
}