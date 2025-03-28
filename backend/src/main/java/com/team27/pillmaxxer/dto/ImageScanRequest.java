package com.team27.pillmaxxer.dto;
import org.springframework.web.multipart.MultipartFile;

public class ImageScanRequest {
    private MultipartFile imageFile;

    public MultipartFile getImageFile() {
        return imageFile;
    }
}
