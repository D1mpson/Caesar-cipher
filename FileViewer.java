import javax.swing.*;
import java.awt.*;
import java.io.*;

public class FileViewer extends JFrame {
    private JTextArea jTextArea;
    private JTextField keyField;
    private ProcessFile processFile;
    private File openedFile;

    public FileViewer(ProcessFile processFile) {
        this.processFile = processFile;
        initUI();
        setVisible(true);
    }

    private void initUI() {
        setTitle("File Viewer");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        getContentPane().add(panel);
        panel.setLayout(new BorderLayout());

        jTextArea = new JTextArea(20, 50);
        JScrollPane scrollPane = new JScrollPane(jTextArea);
        panel.add(scrollPane, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton openFile = new JButton("Відкрити файл");
        openFile.addActionListener(e -> openFile());
        buttonPanel.add(openFile);

        keyField = new JTextField(5);
        keyField.setText("5");
        buttonPanel.add(keyField);

        JButton encryptButton = new JButton("Зашифрувати");
        encryptButton.addActionListener(e -> encrypt());
        buttonPanel.add(encryptButton);

        JButton decryptButton = new JButton("Розшифрувати");
        decryptButton.addActionListener(e -> decrypt());
        buttonPanel.add(decryptButton);

        JButton bruteforceButton = new JButton("Brute Force");
        bruteforceButton.addActionListener(e -> bruteforce());
        buttonPanel.add(bruteforceButton);

        JButton saveFile = new JButton("Зберегти файл");
        saveFile.addActionListener(e -> saveFile());
        buttonPanel.add(saveFile);

        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            openedFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(openedFile))) {
                String line;
                jTextArea.setText("");
                while ((line = reader.readLine()) != null) {
                    jTextArea.append(line + "\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void encrypt() {
        if (openedFile != null) {
            int key = Integer.parseInt(keyField.getText());
            String encryptedText = processFile.encrypt(openedFile.getAbsolutePath(), key);
            jTextArea.setText(encryptedText);
        }

    }

    private void decrypt() {
        File selectedFile = getFile();
        if (selectedFile != null) {
            int key = Integer.parseInt(keyField.getText());
            String decryptedText = processFile.decrypt(selectedFile.getAbsolutePath(), key);
            jTextArea.setText(decryptedText);
        }
    }

    private void bruteforce() {
        File selectedFile = getFile();
        if (selectedFile != null) {
            int bestKey = processFile.bruteforce(selectedFile.getAbsolutePath());
            jTextArea.setText("Спробуй ключ шифрування : " + bestKey);
        }
    }

    private void saveFile() {
        if (openedFile != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(openedFile);
            int returnValue = fileChooser.showSaveDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(selectedFile)) {
                    writer.write(jTextArea.getText());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private File getFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public static void main(String[] args) {
        new FileViewer(new ProcessFile());
    }
}
