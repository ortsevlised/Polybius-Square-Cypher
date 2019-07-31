package ie.gmit.dip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;

public class Menu {
    private boolean keepRunning = true;
    private int selection;
    private Cypher cypher = new Cypher();
    private BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));


    public void start() throws IOException {
        while (keepRunning) {
            showOptions();
            String input = bufferRead.readLine();

            if (!Utils.isANumber(input)) {
                System.out.println("\nPlease enter a number!\n");
            } else {
                selection = Integer.parseInt(input);
                switch (selection) {
                    case 1:
                        encryptFromConsole();
                        break;
                    case 2:
                        encryptFromFile();
                        break;
                    case 3:
                        decryptFromConsole();
                        break;
                    case 4:
                        decryptFromFile();
                        break;
                    case 5:
                        keepRunning = false;
                        break;
                    default:
                        System.out.println("\nThat is not a valid option\n");
                }
            }
        }
    }

    private void showOptions() {
        System.out.println("######################################");
        System.out.println("#   Polybius Square Cypher V 0.0.1   #");
        System.out.println("######################################");
        System.out.println("(1) Enter a message to encrypt");
        System.out.println("(2) Encrypt a message from file");
        System.out.println("(3) Enter a message to decrypt");
        System.out.println("(4) Decrypt message from file");
        System.out.println("(5) Exit");
        System.out.println("Select an option [1-5] >>>\n");
    }

    private void encryptFromFile() throws IOException {
        cypher.setMessageToEncrypt(Utils.parse(enterKeyAndPath()));
        String encrypt = cypher.encrypt();
        System.out.println("The encrypted message is >>>\n" + encrypt + "\n");
        writeToFile(encrypt);
    }

    private String enterKeyAndPath() throws IOException {
        System.out.println("Enter the keyword >>>");
        cypher.setKeyword(Utils.checkIsNotEmpty(bufferRead.readLine(), bufferRead));
        System.out.println("Enter path to file >>>");
        return bufferRead.readLine();
    }

    private void encryptFromConsole() throws IOException {
        System.out.println("Enter the keyword >>>");
        cypher.setKeyword(Utils.checkIsNotEmpty(bufferRead.readLine(), bufferRead));
        System.out.println("Type message to encrypt >>>");
        cypher.setMessageToEncrypt(Utils.checkIsNotEmpty(bufferRead.readLine(), bufferRead));
        String encrypt = cypher.encrypt();
        System.out.println("The encrypted message is >>>\n" + encrypt + "\n");
        writeToFile(encrypt);
    }

    private void writeToFile(String text) throws IOException {
        System.out.println("Would you like to save it to a file? [y/n] >>>\n");
        if (bufferRead.readLine().equalsIgnoreCase("y")) {
            String fileName = "cypher_" + Instant.now().toEpochMilli() + ".txt";
            System.out.println("Saved to " + fileName + " >>>\n");
            Utils.writeToFile(fileName, text);
        } else {
            System.out.println("File not saved >>>\n");
        }
    }

    private void decryptFromConsole() throws IOException {
        System.out.println("Enter the keyword >>>");
        cypher.setKeyword(Utils.checkIsNotEmpty(bufferRead.readLine(), bufferRead));
        System.out.println("Enter the message to decryptFromConsole >>>");
        cypher.setMessageToDecrypt(Utils.checkIsNotEmpty(bufferRead.readLine(), bufferRead));
        String decrypted = cypher.decrypt();
        if (decrypted.length() > 0) {
            System.out.println("The decrypted message is >>>\n" + decrypted + "\n");
            writeToFile(decrypted);
        }
    }

    private void decryptFromFile() throws IOException {
        cypher.setMessageToDecrypt(Utils.parse(enterKeyAndPath()));
        String decrypted = cypher.decrypt();
        if (decrypted.length() > 0) {
            System.out.println("The decrypted message is >>>\n" + decrypted + "\n");
            writeToFile(decrypted);
        }
    }

}
