package com.team27.pillmaxxer.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

@Service
public class ImageScanService {
    public String extractText(String imgUrl) {
        ITesseract tesseract = new Tesseract();
        tesseract.setLanguage("eng"); 

        try {
            URI uri = new URI(imgUrl);

            if (!"https".equalsIgnoreCase(uri.getScheme())) {
                throw new IllegalArgumentException("Only HTTPS supported");
            }

            BufferedImage bufferedImg = ImageIO.read(uri.toURL());

            if (bufferedImg == null) {
                throw new IOException("Failed to load image from " + imgUrl);
            }

            String result = tesseract.doOCR(bufferedImg);
            return result.trim();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL: ", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading from URL: ", e);
        } catch (TesseractException e) {
            throw new RuntimeException("Error performing OCR on image: ", e);
        }
    }
}