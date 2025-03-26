package com.team27.pillmaxxer.service;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.team27.pillmaxxer.model.Prescription;
import com.team27.pillmaxxer.service.ImageScanService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ImageScanServiceTest {
    @Autowired
    private ImageScanService imageScanService;

    @Test
    void testExtractText() {
        String imgUrl = "https://builtin.com/sites/www.builtin.com/files/styles/ckeditor_optimize/public/inline-images/5_python-ocr.jpg";
        String extractedText = imageScanService.extractText(imgUrl);

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