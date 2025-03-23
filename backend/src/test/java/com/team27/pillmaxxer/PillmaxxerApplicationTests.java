package com.team27.pillmaxxer;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.team27.pillmaxxer.service.ImageScanService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PillmaxxerApplicationTests {
	@Autowired
    private ImageScanService imageScanService;

    @Test
    void contextLoads() {
    }

    @Test
    void testExtractText() {
        String testImgUrl = "https://cdn-images-1.medium.com/max/1200/1*klTODXvF6Zjh3SRpOdIpbA.png";
        String extractedText = imageScanService.extractText(testImgUrl);
        assertEquals("05221859", extractedText);
    }
}