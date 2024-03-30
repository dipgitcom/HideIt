package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SteganographyApp {
    public static void encodeAndSave(String inputImagePath, String message) {
        try {
            BufferedImage image = ImageIO.read(new File(inputImagePath));
            encodeMessage(image, message);
            String directoryPath = new File(inputImagePath).getParent();
            String outputImagePath = directoryPath + File.separator + "encoded_" + new File(inputImagePath).getName();
            ImageIO.write(image, "png", new File(outputImagePath));
            System.out.println("Message encoded successfully in the image: " + outputImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String decodeAndPrint(String imagePath) {
        String decodedMessage="";
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            decodedMessage = decodeMessage(image);
            System.out.println("Decoded message: " + decodedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return decodedMessage;
    }

    public static void encodeMessage(BufferedImage image, String message) {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        int messageLength = messageBytes.length;
        int imageWidth = image.getWidth();

        int pixelIndex = 0;

        for (int i = 0; i < 32; i++) {
            int bit = (messageLength >> i) & 1;
            int rgb = image.getRGB(pixelIndex % imageWidth, pixelIndex / imageWidth);

            rgb &= 0xFFFFFFFE; // Clear the least significant bit
            rgb |= bit;       // Set the bit

            image.setRGB(pixelIndex % imageWidth, pixelIndex / imageWidth, rgb);
            pixelIndex++;
        }

        for (byte b : messageBytes) {
            for (int i = 0; i < 8; i++) {
                int bit = (b >> i) & 1;
                int rgb = image.getRGB(pixelIndex % imageWidth, pixelIndex / imageWidth);

                rgb &= 0xFFFFFFFE; // Clear the least significant bit
                rgb |= bit;       // Set the bit

                image.setRGB(pixelIndex % imageWidth, pixelIndex / imageWidth, rgb);
                pixelIndex++;
            }
        }
    }

    public static String decodeMessage(BufferedImage image) {
        int imageWidth = image.getWidth();
        int pixelIndex = 0;
        int messageLength = 0;

        for (int i = 0; i < 32; i++) {
            int bit = image.getRGB(pixelIndex % imageWidth, pixelIndex / imageWidth) & 1;
            messageLength |= bit << i;
            pixelIndex++;
        }

        byte[] messageBytes = new byte[messageLength];

        for (int i = 0; i < messageLength; i++) {
            byte b = 0;
            for (int j = 0; j < 8; j++) {
                int bit = image.getRGB(pixelIndex % imageWidth, pixelIndex / imageWidth) & 1;
                b |= bit << j;
                pixelIndex++;
            }
            messageBytes[i] = b;
        }

        return new String(messageBytes, StandardCharsets.UTF_8);
    }
}