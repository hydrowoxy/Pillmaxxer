package com.team27.pillmaxxer.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

import org.springframework.stereotype.Service;

import com.team27.pillmaxxer.model.Prescription;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

@Service
public class ImageScanService {
    public Prescription scanImage(String imgUrl) {
        String rawText = extractText(imgUrl);
        Prescription prescription = decipherText(rawText);
        return prescription;
    }

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

    public Prescription decipherText(String text) {
        text.replaceAll("\n", " ").replaceAll("\\s+", " ").trim();

        String medication = find(text, "([A-Za-z0-9\\\\s]+)");
        System.out.println("REGEX:" + medication);
        
        String dosage = find(text, "(\\d+)\\s?(mg|ml|g)");
        System.out.println("REGEX:" + dosage);

        String quantity = find(text, "(\\d+)\\s?(capsules|tablets|pills)");
        System.out.println("REGEX:" + quantity);

        String frequency = find(text, "(once|twice|three times|per day|daily)");
        System.out.println("REGEX:" + frequency);

        Prescription prescription = new Prescription();

        prescription.setMedication(medication);
        prescription.setDosage(dosage);
        prescription.setQuantity(quantity);
        prescription.setFrequency(frequency);
        
        return prescription;
    }

    private String find(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);

        if (m.find()) {
            if (m.groupCount() >= 1) {
                return m.group(1);
            }
        }

        return "";
    }
}