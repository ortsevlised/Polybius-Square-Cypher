package ie.gmit.dip;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static ie.gmit.dip.Utils.getKey;
import static ie.gmit.dip.Utils.getRows;

public class Cypher {
    private String messageToEncrypt;
    private String messageToDecrypt;
    private String keyword;

    public String getMessageToEncrypt() {
        return messageToEncrypt;
    }

    public void setMessageToEncrypt(String messageToEncrypt) {
        this.messageToEncrypt = messageToEncrypt;
    }

    public String getMessageToDecrypt() {
        return messageToDecrypt;
    }

    public void setMessageToDecrypt(String messageToDecrypt) {
        this.messageToDecrypt = messageToDecrypt;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    private Map<Integer, Character> polybiusMap = new HashMap<>();

    {
        polybiusMap.put(0, 'A');
        polybiusMap.put(1, 'D');
        polybiusMap.put(2, 'F');
        polybiusMap.put(3, 'G');
        polybiusMap.put(4, 'V');
        polybiusMap.put(5, 'X');
        polybiusMap.put(6, 'Z');
    }

    private char[][] polybiusSquare = {
            {'P', 'H', '0', 'Q', 'G', '6'},
            {'4', 'M', 'E', 'A', '1', 'Y'},
            {'L', '2', 'N', 'O', 'F', 'D'},
            {'X', 'K', 'R', '3', 'C', 'V'},
            {'S', '5', 'Z', 'W', '7', 'B'},
            {'J', '9', 'U', 'T', 'I', '8'},
            {' ', '\n', '!', '"', '-', '.'}
    };

    /**
     * Encrypts the text using the specified keyword
     */
    public String encrypt() {
        StringBuilder encrypted = new StringBuilder();
        String plainText = generateValuesForMatrix(messageToEncrypt).toString();
        int columns = keyword.length();
        int rows = getRows(keyword, plainText.length());
        int increment = 0;
        char[][] keyMatrix = new char[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (increment < plainText.length()) {
                    keyMatrix[i][j] = plainText.charAt(increment);
                    increment++;
                } else {
                    keyMatrix[i][j] = ' ';
                }
            }
        }
        columnarTransposition(encrypted, columns, rows, keyMatrix);
        return encrypted.toString();
    }

    /**
     * Performs the columnar transposition according to the keyword in alphabetic order
     *
     * @param columns
     * @param rows
     * @param keyMatrix
     */
    private void columnarTransposition(StringBuilder encrypted, int columns, int rows, char[][] keyMatrix) {
        int position;
        char[] keyToCharArray = keyword.toCharArray();

        char[] sortedKey = new char[keyToCharArray.length];
        for (int i = 0; i < keyToCharArray.length; i++) {
            sortedKey[i] = keyToCharArray[i];
        }

        Arrays.sort(sortedKey);
        for (int i = 0; i < columns; i++) {

            position = keyword.indexOf(sortedKey[i]);
            keyToCharArray[position] = '\u0000';//setting the indexes already used to unicode null character, in case there's a duplicate value not to use the wrong one.
            keyword = new String(keyToCharArray);
            for (int j = 0; j < rows; j++) {
                encrypted.append(keyMatrix[j][position]);
            }
        }
    }

    /**
     * Decrypts the message using the key provided
     *
     * @return
     */
    public String decrypt() {
        StringBuilder decrypted = new StringBuilder();
        messageToDecrypt = messageToDecrypt.toUpperCase().trim();
        int rows = getRows(keyword, messageToDecrypt.length());
        int column = keyword.length();
        char[] keyToCharArray = keyword.toCharArray();
        int position;
        int increment = 0;
        char[][] Matrix = new char[rows][column];
        char[] toDecryptArray = messageToDecrypt.toCharArray();
        char[] sortedKey = keyword.toCharArray();
        Arrays.sort(sortedKey);

        for (int i = 0; i < column; i++) {

            position = keyword.indexOf(sortedKey[i]);
            keyToCharArray[position] = '!';
            keyword = new String(keyToCharArray);

            for (int j = 0; j < rows; j++) {
                if (increment < messageToDecrypt.length()) {
                    Matrix[j][position] = toDecryptArray[increment];
                    increment++;
                }
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < column; j++) {
                decrypted.append(Matrix[i][j]);
            }
        }
        return generateValueFromMatrix(decrypted.toString());

    }


    /**
     * Generates the plaintext from the matrix
     *
     * @param encrypted
     */
    public String generateValueFromMatrix(String encrypted) {
        String[] split = encrypted.split("(?<=\\G..)");

        StringBuilder sb = new StringBuilder();
        for (String a : split) {
            if (a.trim().length() > 1) {
                try {
                    sb.append(polybiusSquare[getKey(polybiusMap, a.charAt(0))][getKey(polybiusMap, a.charAt(1))]);
                } catch (NoSuchElementException e) {
                    sb= new StringBuilder();
                    System.out.println("\n It is not following valid encryption format >>>\n");
                    break;
                }
                catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("\n The message is too long please decrypt it from a file >>>\n");
                    sb= new StringBuilder();
                    break;
                }
            }
        }
        return sb.toString();
    }

    /**
     * Generates the values that will be used in the matrix
     *
     * @param message
     * @return
     */
    public StringBuilder generateValuesForMatrix(String message) {
        StringBuilder sb = new StringBuilder();

        char[] chars = message.trim().toUpperCase().toCharArray();

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

}
