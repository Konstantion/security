package org.example;

import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AutoKeyCipherGUI extends JFrame {
  private final JTextField alphabetField;
  private final JTextField keyField;
  private final JTextArea inputTextArea;
  private final JTextArea outputTextArea;
  private final JComboBox<String> cipherTypeComboBox;

  public AutoKeyCipherGUI() {
    setTitle("Шифр з автоключем");
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(null);

    JLabel alphabetLabel = new JLabel("Алфавіт:");
    alphabetLabel.setBounds(20, 20, 80, 25);
    add(alphabetLabel);

    alphabetField = new JTextField("АБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ");
    alphabetField.setBounds(100, 20, 460, 25);
    add(alphabetField);

    JLabel keyLabel = new JLabel("Ключ:");
    keyLabel.setBounds(20, 60, 80, 25);
    add(keyLabel);

    keyField = new JTextField();
    keyField.setBounds(100, 60, 200, 25);
    add(keyField);

    JLabel cipherTypeLabel = new JLabel("Тип шифру:");
    cipherTypeLabel.setBounds(320, 60, 80, 25);
    add(cipherTypeLabel);

    String[] cipherTypes = {"Шифрування", "Розшифрування"};
    cipherTypeComboBox = new JComboBox<>(cipherTypes);
    cipherTypeComboBox.setBounds(400, 60, 160, 25);
    add(cipherTypeComboBox);

    JLabel inputTextLabel = new JLabel("Вхідний текст:");
    inputTextLabel.setBounds(20, 100, 100, 25);
    add(inputTextLabel);

    inputTextArea = new JTextArea();
    JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
    inputScrollPane.setBounds(20, 130, 260, 100);
    add(inputScrollPane);

    JLabel outputTextLabel = new JLabel("Результат:");
    outputTextLabel.setBounds(320, 100, 80, 25);
    add(outputTextLabel);

    outputTextArea = new JTextArea();
    outputTextArea.setEditable(false);
    JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
    outputScrollPane.setBounds(320, 130, 260, 100);
    add(outputScrollPane);

    JButton encryptButton = new JButton("Виконати");
    encryptButton.setBounds(250, 250, 100, 30);
    add(encryptButton);

    encryptButton.addActionListener(
        e -> {
          String alphabet = alphabetField.getText();
          String key = keyField.getText();
          String inputText = inputTextArea.getText();
          String result = "";

          if (Objects.equals(cipherTypeComboBox.getSelectedItem(), "Шифрування")) {
            result = encrypt(inputText, key, alphabet);
          } else {
            result = decrypt(inputText, key, alphabet);
          }

          outputTextArea.setText(result);
        });
  }

  private String encrypt(String text, String key, String alphabet) {
    StringBuilder fullKey = new StringBuilder(key);
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < text.length(); i++) {
      if (fullKey.length() < text.length()) {
        fullKey.append(text.charAt(i));
      }
      int textIndex = alphabet.indexOf(text.charAt(i));
      int keyIndex = alphabet.indexOf(fullKey.charAt(i));
      int cipherIndex = (textIndex + keyIndex) % alphabet.length();
      result.append(alphabet.charAt(cipherIndex));
    }
    return result.toString();
  }

  private String decrypt(String text, String key, String alphabet) {
    StringBuilder fullKey = new StringBuilder(key);
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < text.length(); i++) {
      int textIndex = alphabet.indexOf(text.charAt(i));
      int keyIndex = alphabet.indexOf(fullKey.charAt(i));
      int plainIndex = (textIndex - keyIndex + alphabet.length()) % alphabet.length();
      char plainChar = alphabet.charAt(plainIndex);
      result.append(plainChar);
      fullKey.append(plainChar);
    }
    return result.toString();
  }

  public static void main(String[] args) {
    AutoKeyCipherGUI gui = new AutoKeyCipherGUI();
    gui.setVisible(true);
  }
}
