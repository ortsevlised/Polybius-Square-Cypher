package ie.gmit.dip;

import java.io.*;
import java.util.Map;

public class Utils {

    /**
     * Gets the amount of rows needed for the matrix
     *
     * @param key
     * @param length
     * @return
     */
    public static int getRows(String key, int length) {
        int rows = length / key.length();
        if (length % key.length() != 0) {
            rows++;
        }
        return rows;
    }

    /**
     * Gets the key by specifying the value in the map
     *
     * @param map
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> K getKey(Map<K, V> map, V value) {
        return map.entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().get();
    }

    /**
     * Parses text from file
     *
     * @param file
     * @return
     */
    public static String parse(String file) {
        BufferedReader reader;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return sb.toString();
    }

    /**
     * Writes the text to a file
     * @param fileName name of the file to create
     * @param text text to write to the file
     */
    public static void writeToFile(String fileName,String text) {
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(text);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            System.out.println("Oops! something is wrong");
            e.printStackTrace();
        }
    }

    /**
     * Check whether an input is a number
     *
     * @param data
     * @return
     */
    public static boolean isANumber(String data) {
        try {
            Integer.parseInt(data);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Makes sure the text is not empty
     * @param text
     * @param bufferedReader
     * @return
     * @throws IOException
     */
    public static String checkIsNotEmpty(String text, BufferedReader bufferedReader) throws IOException {
        while (text.length() < 1) {
            System.out.println("Keyword/Message cannot be empty, please enter a valid one >>>");
            text = bufferedReader.readLine();
        }
        return text;
    }
}

