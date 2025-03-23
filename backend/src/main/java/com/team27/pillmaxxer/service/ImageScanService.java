package com.team27.pillmaxxer.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.ImageHelper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

@Service
public class ImageScanService {
    public String extractText(String imgUrl) {
        ITesseract tesseract = new Tesseract();
        tesseract.setLanguage("eng"); 

        try {
            URI uri = new URI(imgUrl);
            URL url = uri.toURL();
            BufferedImage bufferedImg = ImageIO.read(url);

            if (bufferedImg != null) {
                String result = tesseract.doOCR(bufferedImg);
                return result;
            } else {
                throw new RuntimeException("Image not available at specified URL");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during image scan: ", e);
        }
    }
}