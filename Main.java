package org.example;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends JFrame {
    private JButton selectImageButton; // Button to select an image
    private JButton encodeButton;
    private JButton decodeButton;
    private JLabel imageLabel; // To display selected image
    private String selectedImagePath; // To store the selected image path

    public Main() {
        setTitle("Steganography Tool");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));

        selectImageButton = new JButton("Select Image");
        encodeButton = new JButton("Encode");
        decodeButton = new JButton("Decode");

        encodeButton.setEnabled(false); // Initially disabled
        decodeButton.setEnabled(false); // Initially disabled

        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImage();
            }
        });

        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encodeMessage();
            }
        });

        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decodeMessage();
            }
        });

        buttonPanel.add(selectImageButton);
        buttonPanel.add(encodeButton);
        buttonPanel.add(decodeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        imageLabel = new JLabel();
        panel.add(imageLabel, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }

    private void selectImage() {
        // Prompt user to choose an image file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Image File");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();

            try {
                BufferedImage image = ImageIO.read(new File(selectedImagePath));
                displayImage(image);
                encodeButton.setEnabled(true); // Enable encode button
                decodeButton.setEnabled(true); // Enable decode button
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error occurred while loading the image.");
            }
        }
    }

    private void encodeMessage() {
        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
            String message = JOptionPane.showInputDialog(this, "Enter the message to encode:");
            SteganographyApp.encodeAndSave(selectedImagePath, message);
            JOptionPane.showMessageDialog(this,"Message encoded successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select an image first.");
        }
    }

    private void decodeMessage() {
        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
            String messgae = SteganographyApp.decodeAndPrint(selectedImagePath);
            if (messgae != null) {
                JOptionPane.showMessageDialog(this, "Decoded message: " + messgae);
            } else {
                JOptionPane.showMessageDialog(this, "No message found in the image.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an image first.");
        }
    }

    private void displayImage(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}
