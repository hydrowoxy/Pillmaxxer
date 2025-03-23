package com.team27.pillmaxxer.service;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.team27.pillmaxxer.service.ImageScanService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ImageScanServiceTest {
	@Autowired
    private ImageScanService imageScanService;
    
    @Test
    void testExtractText() {
        String testImgUrl = "https://builtin.com/sites/www.builtin.com/files/styles/ckeditor_optimize/public/inline-images/5_python-ocr.jpg";
        String extractedText = imageScanService.extractText(testImgUrl);
        assertEquals("Tesseract sample", extractedText);
    }
}