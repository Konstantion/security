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
    setSize(350, 250);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JLabel titleLabel = new JLabel("Login to Application", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    JLabel passwordLabel = new JLabel("Enter Password:");
    passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));

    JPasswordField passwordField = new JPasswordField(15);
    passwordField.setFont(new Font("Arial", Font.PLAIN, 14));

    JButton submitButton = new JButton("Submit");
    submitButton.setFont(new Font("Arial", Font.BOLD, 14));

    JLabel messageLabel = new JLabel("", JLabel.CENTER);
    messageLabel.setFont(new Font("Arial", Font.ITALIC, 12));
    messageLabel.setForeground(Color.RED);

    JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    inputPanel.add(passwordLabel);
    inputPanel.add(passwordField);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(submitButton);

    JPanel messagePanel = new JPanel();
    messagePanel.add(messageLabel);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(titleLabel);
    mainPanel.add(inputPanel);
    mainPanel.add(buttonPanel);
    mainPanel.add(messagePanel);

    submitButton.addActionListener(
        (ActionEvent e) -> {
          String inputPassword = new String(passwordField.getPassword());
          if (encrypt(inputPassword).equals(ENCRYPTED_PASSWORD)) {
            messageLabel.setText("Successfully logged in.");
            messageLabel.setForeground(new Color(0, 128, 0));
          } else {
            messageLabel.setText("Incorrect password.");
            messageLabel.setForeground(Color.RED);
            deleteSelfAndRestart();
          }
        });

    add(mainPanel);
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
    } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
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
