package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class AutoDeleteApplication extends JFrame {
  private static final String ALPHABET = "АБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ";
  private static final String KEY = "КЛЮЧ";
  private static final String ENCRYPTED_PASSWORD = "АЛОЇБЬ";

  public AutoDeleteApplication() {
    setTitle("Login");
    setSize(300, 150);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JLabel label = new JLabel("Enter Password:");
    JPasswordField passwordField = new JPasswordField(15);
    JButton submitButton = new JButton("Submit");
    JLabel messageLabel = new JLabel();

    submitButton.addActionListener(
        (ActionEvent e) -> {
          String inputPassword = new String(passwordField.getPassword());
          if (encrypt(inputPassword).equals(ENCRYPTED_PASSWORD)) {
            messageLabel.setText("Successfully logged in.");
          } else {
            messageLabel.setText("Incorrect password.");
            deleteSelfAndRestart();
          }
        });

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 1));
    panel.add(label);
    panel.add(passwordField);
    panel.add(submitButton);

    add(panel, BorderLayout.CENTER);
    add(messageLabel, BorderLayout.SOUTH);
  }

  private void deleteSelfAndRestart() {
    String pathToJar =
        new File(
                AutoDeleteApplication.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
            .getAbsolutePath();

    File jarFile = new File(pathToJar);

    if (jarFile.delete()) {
      System.out.println("Application deleted successfully.");
    } else {
      System.out.println("Failed to delete the application. Ensure the application is not in use.");
    }

    try {
      restartSystem();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Failed to restart the system.");
    }
  }

  private void restartSystem() throws IOException {
    String os = System.getProperty("os.name").toLowerCase();
    Runtime runtime = Runtime.getRuntime();

    if (os.contains("win")) {
      runtime.exec(new String[] {"shutdown", "-r", "-t", "0"});
    } else if (os.contains("mac")) {
      runtime.exec(new String[] {"sudo", "shutdown", "-r", "now"});
    } else if (os.contains("nix") || os.contains("nux")) {
      runtime.exec(new String[] {"sudo", "shutdown", "-r", "now"});
    } else {
      System.out.println("Unsupported operating system for restart command.");
    }
  }

  private String encrypt(String text) {
    StringBuilder fullKey = new StringBuilder(KEY);
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < text.length(); i++) {
      if (fullKey.length() < text.length()) {
        fullKey.append(text.charAt(i));
      }
      int textIndex = ALPHABET.indexOf(text.charAt(i));
      int keyIndex = ALPHABET.indexOf(fullKey.charAt(i));
      int cipherIndex = (textIndex + keyIndex) % ALPHABET.length();
      result.append(ALPHABET.charAt(cipherIndex));
    }
    return result.toString();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          AutoDeleteApplication app = new AutoDeleteApplication();
          app.setVisible(true);
        });
  }
}
