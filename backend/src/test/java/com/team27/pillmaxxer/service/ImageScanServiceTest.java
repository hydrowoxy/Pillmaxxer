package com.team27.pillmaxxer.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import com.team27.pillmaxxer.model.Prescription;
import com.team27.pillmaxxer.service.ImageScanService;

@SpringBootTest
class ImageScanServiceTest {
    @Autowired
    private ImageScanService imageScanService;

    @Test
    void testExtractText() throws IOException {
        InputStream imageStream = new FileInputStream(new File("/service/ocr-test.jpg"));
        MockMultipartFile mockImageUpload = new MockMultipartFile("imageFile", "ocr-test.jpg", "image/jpeg", imageStream.readAllBytes());

        String extractedText = imageScanService.extractText(mockImageUpload);
        assertEquals("Tesseract sample", extractedText);
    }

    @Test
    void testDecipherText() {
        String rawText = "Aspirin 500mg 1 capsules once daily";
        Prescription prescription = imageScanService.decipherText(rawText);

        assertEquals("Aspirin", prescription.getMedicationName());
        assertEquals("500", prescription.getDosage());
        assertEquals("1", prescription.getQuantity());
        assertEquals("once", prescription.getFrequency());
    }
}