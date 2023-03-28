package ru.liga;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

interface ConcurrencyFiles {
    String EURO = System.getProperty("user.dir").concat("\\lib\\EUR.csv");
    String USD = System.getProperty("user.dir").concat("\\lib\\USD.csv");
    String TRY = System.getProperty("user.dir").concat("\\lib\\TRY.csv");
}

public class CSVReader {

    private String getFileName(String type){
        if (type.equals("EUR")) return ConcurrencyFiles.EURO;
        if (type.equals("TRY")) return ConcurrencyFiles.TRY;
        if(type.equals("USD")) return ConcurrencyFiles.USD;

        return "";
    }

    public ArrayList<String[]> getFileData (String fileMemo, int startColumn, int endColumn) {
        final String csvFile = this.getFileName(fileMemo);
        final String csvSplitBy = ";";
        String line = "";
        BufferedReader br = null;

        ArrayList<String[]> result = new ArrayList<String[]>();

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] dataLine = line.split(csvSplitBy);
                String[] dataForResult = Arrays.copyOfRange(dataLine, startColumn, endColumn);
                result.add(dataForResult);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        result.remove(0);

        return result;
    }

}
