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
        System.out.println("Enter the keyword >>>");
        cypher.setKeyword(bufferRead.readLine());
        System.out.println("Enter path to file >>>");
        String pathToFile = bufferRead.readLine();
        cypher.setMessageToEncrypt(Utils.parse(pathToFile));
        String encrypt = cypher.encrypt();
        System.out.println("The encrypted message is >>>\n" + encrypt + "\n");
        writeToFile(encrypt);
    }

    private void encryptFromConsole() throws IOException {
        System.out.println("Enter the keyword >>>");
        cypher.setKeyword(bufferRead.readLine());
        System.out.println("Type message to encrypt >>>");
        cypher.setMessageToEncrypt(bufferRead.readLine());
        String encrypt = cypher.encrypt();
        System.out.println("The encrypted message is >>>\n" + encrypt + "\n");
        writeToFile(encrypt);
    }

    private void writeToFile(String text) throws IOException {
        System.out.println("Would you like to save it to a file? [y/n] >>>\n");
        if (bufferRead.readLine().equalsIgnoreCase("y")) {
            String fileName = "cypher_" + Instant.now().toEpochMilli()+".txt";
            System.out.println("Saved to "+fileName+" >>>\n");
            Utils.writeToFile(fileName, text);
        }
    }

    private void decryptFromConsole() throws IOException {
        System.out.println("Enter the keyword >>>");
        String keyword = bufferRead.readLine();

        while (keyword.length() < 1) {
            System.out.println("Keyword cannot be empty, please enter a keyword >>>");
            keyword = bufferRead.readLine();
        }
        cypher.setKeyword(keyword);
        System.out.println("Enter the message to decryptFromConsole >>>");
        cypher.setMessageToDecrypt(bufferRead.readLine());
        String decrypt = cypher.decrypt();
        System.out.println("The decrypted message is >>>\n" + decrypt + "\n");
        writeToFile(decrypt);
    }

    private void decryptFromFile() throws IOException {
        System.out.println("Enter the keyword >>>");
        cypher.setKeyword(bufferRead.readLine());
        System.out.println("Enter path to file >>>");
        String pathToFile = bufferRead.readLine();
        cypher.setMessageToDecrypt(Utils.parse(pathToFile));
        String decrypt = cypher.decrypt();
        System.out.println("The decrypted message is >>>\n" + decrypt + "\n");
        writeToFile(decrypt);

    }

}
