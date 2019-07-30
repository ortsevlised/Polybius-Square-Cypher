package ie.gmit.dip;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Cypher {
    StringBuilder encrypted = new StringBuilder();

    String key = "JAVA";
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


    /**
     * Generates the values that will be used in the matrix
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
     * Encrypts the text using the specified key
     * @param textToEncrypt
     * @param key
     */
    public void encrypt(String textToEncrypt, String key) {
        String plainText = generateValuesForMatrix(textToEncrypt).toString();
        int columns = key.length();
        int rows = getRows(key,plainText.length());
        int increment=0;
        int position;
        char[] keyToArray = key.toCharArray();
        char[][] keyMatrix = new char[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (increment < plainText.length()) {
                    keyMatrix[i][j] = plainText.charAt(increment);
                    increment++;
                }
            }
        }

        char[] Sorted_key = new char[keyToArray.length];
        for (int i = 0; i < keyToArray.length; i++) {
            Sorted_key[i] = keyToArray[i];
        }

        Arrays.sort(Sorted_key);
        for (int i = 0; i < columns; i++) {
            position = key.indexOf(Sorted_key[i]);
            for (int j = 0; j < rows; j++) {
                encrypted=encrypted.append(keyMatrix[j][position]);
            }
        }
        System.out.println(encrypted);
    }

    /**
     * Gets the amount of rows needed for the matrix
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


    @Test
    public void testConvertToMatrixValues() {
        Assert.assertThat(generateValuesForMatrix("TEXT TO ENCRYPT").toString(), CoreMatchers.is("XGDFGAXGXGFGDFFFGVGFDXAAXG"));
    }

    @Test
    public void testing() {
        String textToEncrypt = "Text TO ENCRYPT";
        encrypt(textToEncrypt, key);
    }

}
