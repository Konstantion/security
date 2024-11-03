package org.example;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class PasswordProtection extends JFrame {
    private final JTextField passwordField;
    private final JLabel messageLabel;
    private long startTime;
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

        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
            }
        });

        submitButton.addActionListener(e -> {
            if (isLocked) {
                messageLabel.setText("Поле заблоковано. Зачекайте 2 хвилини.");
                return;
            }

            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            if (elapsedTime > 120) {
                lockField();
                return;
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
        // Перевірка надійності паролю (мінімальна довжина, наявність цифр та символів)
        if (password.length() < 8) return false;
        if (!password.matches(".*\\d.*")) return false;
        return password.matches(".*[!@#$%^&*()].*");
    }

    private String replaceVowels(String password) {
        String vowels = "АаЕеИиІіОоУуЮюЯяЄєЇї";
        StringBuilder result = new StringBuilder();
        Random rand = new Random();

        for (char c : password.toCharArray()) {
            if (vowels.indexOf(c) != -1) {
                // Замінюємо голосну на випадкову літеру або цифру
                if (rand.nextBoolean()) {
                    // Випадкова літера
                    char randomChar = (char) (rand.nextInt(33) + 'А');
                    result.append(randomChar);
                } else {
                    // Випадкова цифра
                    int randomDigit = rand.nextInt(10);
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
        lockTimer = new Timer(10_000, e -> {
            isLocked = false;
            passwordField.setEnabled(true);
            messageLabel.setText("Можете вводити пароль знову.");
            lockTimer.stop();
        });
        lockTimer.start();
    }

    public static void main(String[] args) {
        PasswordProtection app = new PasswordProtection();
        app.setVisible(true);
    }
}
