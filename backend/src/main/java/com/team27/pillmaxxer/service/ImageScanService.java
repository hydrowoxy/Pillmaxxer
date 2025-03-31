package com.team27.pillmaxxer.service;

import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

import org.springframework.stereotype.Service;

import com.team27.pillmaxxer.model.Prescription;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

@Service
public class ImageScanService {
    public Prescription scanImage(MultipartFile file) {
        String rawText = extractText(file);
        Prescription prescription = decipherText(rawText);
        System.out.println("Image scan success");
        return prescription;
    }

    public String extractText(MultipartFile file) {
        ITesseract tesseract = new Tesseract();
        tesseract.setLanguage("eng");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Callable<String> task = () -> {
            try {
                BufferedImage bufferedImg = ImageIO.read(file.getInputStream());
                if (bufferedImg == null) throw new IOException("Failed to load image");
                return tesseract.doOCR(bufferedImg).trim(); 
            } catch (IOException e) {
                throw new RuntimeException("Error loading image: ", e);
            } catch (TesseractException e) {
                throw new RuntimeException("Error performing OCR on image: ", e);
            } catch (Exception e) {
                throw new RuntimeException("Unexpected error: ", e);
            }
        };

        try {
            Future<String> future = scheduler.submit(task);
            return future.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new RuntimeException("OCR process timed out after 5 seconds", e);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error executing OCR task: ", e);
        } finally {
            scheduler.shutdown();
        }
    }

    public Prescription decipherText(String text) {
        text.replaceAll("\n", " ").replaceAll("\\s+", " ").trim();

        String medication = find(text, "([A-Za-z0-9\\\\s]+)");
        String dosage = find(text, "(\\d+)\\s?(mg|ml|g)");
        String quantity = find(text, "(\\d+)\\s?(capsules|tablets|pills)");
        String frequency = find(text, "(once|twice|three times|per day|daily)");

        /*
         * TODO - need to check if medication exists in database, if not, add it to the
         * database
         */
        Prescription prescription = new Prescription();
        prescription.setMedicationName(medication);
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