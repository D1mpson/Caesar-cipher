import java.io.*;

public class ProcessFile {
    private static final String UPPER_CASE = "АБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ";
    private static final String LOWER_CASE = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя";
    private static final String SYMBOL = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    public static final int ALPHABET_SIZE = 33;

    public String encrypt(String inputFile, int key) {
        return readerWriter(inputFile, key, true);
    }

    public String decrypt(String inputFile, int key) {
        return readerWriter(inputFile, key, false);
    }

    private String readerWriter(String inputFile, int key, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        try (FileReader reader = new FileReader(inputFile)) {
            int chars;
            while ((chars = reader.read()) != -1) {
                char character = (char) chars;
                if (UPPER_CASE.indexOf(character) != -1 || LOWER_CASE.indexOf(character) != -1 || SYMBOL.indexOf(character) != -1) {
                    character = processChar(character, key, encrypt);
                }
                result.append(character);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private char processChar(char character, int key, boolean encrypt) {
        int offset = (key % ALPHABET_SIZE) * (encrypt ? 1 : -1);
        int oldPosition = Character.isUpperCase(character) ? UPPER_CASE.indexOf(character) : (Character.isLowerCase(character) ? LOWER_CASE.indexOf(character) : SYMBOL.indexOf(character));
        int newPosition = (oldPosition + offset + ALPHABET_SIZE) % ALPHABET_SIZE;
        return Character.isUpperCase(character) ? UPPER_CASE.charAt(newPosition) : (Character.isLowerCase(character) ? LOWER_CASE.charAt(newPosition) : SYMBOL.charAt(newPosition));
    }

    public int bruteforce(String encryptedFile) {
        int bestKey = 0;
        int maxSpaces = 0;

        for (int key = 0; key < ALPHABET_SIZE; key++) {
            String decryptedText = decrypt(encryptedFile, key);
            int spaces = decryptedText.split(" ").length - 1;
            if (spaces > maxSpaces) {
                maxSpaces = spaces;
                bestKey = key;
            }
        }

        return bestKey;
    }
}
