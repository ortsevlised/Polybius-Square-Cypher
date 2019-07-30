package ie.gmit.dip;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

public class Cypher {

    StringBuilder encrypted = new StringBuilder();
    StringBuilder decrypted = new StringBuilder();
    String keyword = "JAVA";

    private Map<Integer, Character> polybiusMap = new HashMap<>();

    {
        polybiusMap.put(0, 'A');
        polybiusMap.put(1, 'D');
        polybiusMap.put(2, 'F');
        polybiusMap.put(3, 'G');
        polybiusMap.put(4, 'V');
        polybiusMap.put(5, 'X');
    }

    private char[][] polybiusSquare = {
            {'P', 'H', '0', 'Q', 'G', '6'},
            {'4', 'M', 'E', 'A', '1', 'Y'},
            {'L', '2', 'N', 'O', 'F', 'D'},
            {'X', 'K', 'R', '3', 'C', 'V'},
            {'S', '5', 'Z', 'W', '7', 'B'},
            {'J', '9', 'U', 'T', 'I', '8'}
    };

    public static <K, V> K getKey(Map<K, V> map, V value) {
        return map.entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().get();
    }

    /**
     * Generates the values that will be used in the matrix
     *
     * @param toEncrypt
     * @return
     */
    public StringBuilder generateValuesForMatrix(String toEncrypt) {
        StringBuilder sb = new StringBuilder();

        char[] chars = toEncrypt.trim().toUpperCase().toCharArray();

        for (int i = 0; i <= chars.length - 1; i++) {
            for (int j = 0; j <= polybiusSquare.length - 1; j++) {
                for (int k = 0; k <= polybiusSquare[j].length - 1; k++) {
                    if (chars[i] == polybiusSquare[j][k]) {
                        sb.append(polybiusMap.get(j)).append(polybiusMap.get(k));
                        break;
                    }
                }
            }
        }
        return sb;
    }

    /**
     * Generates the plaintext from the matrix
     *
     * @param encrypted
     */
    public void generateValueFromMatrix(String encrypted) {
        String[] split = encrypted.split("(?<=\\G..)");

        StringBuilder sb = new StringBuilder();
        for (String a : split) {
            if (a.trim().length() > 1) {
                sb.append(polybiusSquare[getKey(polybiusMap, a.charAt(0))][getKey(polybiusMap, a.charAt(1))]);
            }
        }
        System.out.println(sb);


    }

    /**
     * Encrypts the text using the specified keyword
     *
     * @param textToEncrypt
     * @param keyword
     */
    public String encrypt(String textToEncrypt, String keyword) {
        String plainText = generateValuesForMatrix(textToEncrypt).toString();
        int columns = keyword.length();
        int rows = getRows(keyword, plainText.length());
        int increment = 0;

        int position;
        char[] keyToCharArray = keyword.toCharArray();
        char[][] keyMatrix = new char[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (increment < plainText.length()) {
                    keyMatrix[i][j] = plainText.charAt(increment);
                    increment++;
                }
            }
        }

        char[] sortedKey = new char[keyToCharArray.length];
        for (int i = 0; i < keyToCharArray.length; i++) {
            sortedKey[i] = keyToCharArray[i];
        }

        Arrays.sort(sortedKey);
        for (int i = 0; i < columns; i++) {

            position = keyword.indexOf(sortedKey[i]);
            keyToCharArray[position] = '!';
            keyword = new String(keyToCharArray);
            for (int j = 0; j < rows; j++) {
                encrypted.append(keyMatrix[j][position]);
            }
        }
        System.out.println(encrypted);
        return encrypted.toString();
    }

    /**
     * Gets the amount of rows needed for the matrix
     *
     * @param key
     * @param length
     * @return
     */
    private int getRows(String key, int length) {
        int rows = length / key.length();
        if (length % key.length() != 0) {
            rows++;
        }
        return rows;
    }


    public void decrypt(String keyword, String toDecrypt) {
        toDecrypt = toDecrypt.toUpperCase().trim();
        int rows = getRows(keyword, toDecrypt.length());
        int column = keyword.length();
        char[] keyToCharArray = keyword.toCharArray();
        int position;
        int increment = 0;
        char[][] Matrix = new char[rows][column];
        char[] toDecryptArray = toDecrypt.toCharArray();
        char[] sortedKey = keyword.toCharArray();
        Arrays.sort(sortedKey);

        for (int i = 0; i < column; i++) {

            position = keyword.indexOf(sortedKey[i]);
            keyToCharArray[position] = '!';
            keyword = new String(keyToCharArray);

            for (int j = 0; j < rows; j++) {
                if (increment < toDecrypt.length()) {
                    Matrix[j][position] = toDecryptArray[increment];
                    increment++;
                }
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < column; j++) {
                decrypted = decrypted.append(Matrix[i][j]);
            }
        }

        generateValueFromMatrix(decrypted.toString());
    }

    @Test
    public void testDecryption() {
        decrypt(keyword, "AAGDXFGFGGF FAXDFDDXDDG ");
    }

    @Test
    public void testConvertToMatrixValues() {
        Assert.assertThat(generateValuesForMatrix("OBJECT").toString(), is("FGVXXADFGVXG"));
    }


    @Test
    public void testing() {
        String textToEncrypt = "OBJECT";
        Assert.assertThat(encrypt(textToEncrypt, keyword), is("GAVXFGFXGVDX"));
    }

    @Test
    public void testingOtherText() {
        String textToEncrypt = "la puta madre";
        Assert.assertThat(encrypt(textToEncrypt, keyword), is("XFF\u0000FDG\u0000"));
    }
}
