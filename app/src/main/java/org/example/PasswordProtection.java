package org.example;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class PasswordProtection extends JFrame {
  private static final Random RANDOM = new Random();
  private static final String VOWELS = "АаЕеИиІіОоУуЮюЯяЄєЇї";
  private static final String SYMBOL_REGEX = ".*[!@#$%^&*()].*";
  private static final String DIGIT_REGEX = ".*\\d.*";
  private static final Duration LOCK_AFTER = Duration.ofSeconds(10);

  /** Gui elements */
  private final JTextField passwordField;

  private final JLabel messageLabel;

  private volatile long startTime = 0;

  private Timer lockTimer;

  private boolean isLocked = false;

  public PasswordProtection() {
    setTitle("Парольний захист");
    setSize(400, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(null);

    JLabel passwordLabel = new JLabel("Введіть пароль:");
    passwordLabel.setBounds(50, 30, 100, 25);
    add(passwordLabel);

    passwordField = new JTextField();
    passwordField.setBounds(160, 30, 180, 25);
    add(passwordField);

    JButton submitButton = new JButton("Підтвердити");
    submitButton.setBounds(140, 70, 120, 30);
    add(submitButton);

    messageLabel = new JLabel("");
    messageLabel.setBounds(50, 110, 300, 25);
    add(messageLabel);

    passwordField.addKeyListener(
        new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
            if (startTime == 0) {
              startTime = System.nanoTime();
              System.out.println("Set startTime: " + startTime);
            }
          }
        });

    submitButton.addActionListener(
        e -> {
          if (isLocked) {
            messageLabel.setText("Поле заблоковано. Зачекайте 2 хвилини.");
            return;
          }

          if (startTime != 0) {
            Duration elapsedTime = Duration.ofNanos(System.nanoTime() - startTime);
            if (elapsedTime.compareTo(LOCK_AFTER) > 0) {
              lockField();
              return;
            }
          }

          String password = passwordField.getText();
          if (isPasswordStrong(password)) {
            String securePassword = replaceVowels(password);
            messageLabel.setText("Ваш пароль: " + securePassword);
          } else {
            messageLabel.setText("Пароль недостатньо надійний.");
          }

          startTime = 0;
        });
  }

  private boolean isPasswordStrong(String password) {
    if (password.length() < 8) { // length
      return false;
    }
    if (!password.matches(DIGIT_REGEX)) { // one digit
      return false;
    }

    return password.matches(SYMBOL_REGEX); // one symbol
  }

  private String replaceVowels(String password) {
    StringBuilder result = new StringBuilder();
    for (char c : password.toCharArray()) {
      if (VOWELS.indexOf(c) != -1) {
        if (RANDOM.nextBoolean()) {
          char randomChar = (char) (RANDOM.nextInt(33) + 'А');
          result.append(randomChar);
        } else {
          int randomDigit = RANDOM.nextInt(10);
          result.append(randomDigit);
        }
      } else {
        result.append(c);
      }
    }
    return result.toString();
  }

  private void lockField() {
    isLocked = true;
    passwordField.setEnabled(false);
    messageLabel.setText("Поле заблоковано на 2 хвилини.");
    lockTimer =
        new Timer(
            (int) LOCK_AFTER.toMillis(),
            e -> {
              isLocked = false;
              passwordField.setEnabled(true);
              messageLabel.setText("Можете вводити пароль знову.");
              startTime = System.nanoTime();
              System.out.println("Unlock with startTime: " + startTime);
              lockTimer.stop();
            });
    lockTimer.start();
  }

  public static void main(String[] args) {
    PasswordProtection app = new PasswordProtection();
    app.setVisible(true);
  }
}
