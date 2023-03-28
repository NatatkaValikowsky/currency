package ru.liga;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class Currency {

    private String getResultString(Calendar c, String result) {
        String month = c.get(Calendar.MONTH) + 1 < 10 ? "0" + (c.get(Calendar.MONTH) + 1) : Integer.toString(c.get(Calendar.MONTH) + 1);
        return getDayOfWeek(c.get(Calendar.DAY_OF_WEEK)) + " " + c.get(Calendar.DATE) + "." + month + "." + c.get(Calendar.YEAR) + " - " + result;
    }

    private String getDayOfWeek(int number) {
        return switch (number) {
            case 1 -> "Пн";
            case 2 -> "Вт";
            case 3 -> "Ср";
            case 4 -> "Чт";
            case 5 -> "Пт";
            case 6 -> "Сб";
            default -> "Вс";
        };
    }

    private Calendar getNextDay(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(dateFormat.parse(date));
        c.add(Calendar.DATE, 1);

        return c;
    }

    private String getDailyForecast(ArrayList<String[]> data) throws ParseException {
        try {
            String lastDayFromData = data.get(0)[0];
            Calendar c = getNextDay(lastDayFromData);

            String[] dataForCount = new String[7];
            for (int dayNum = 0; dayNum < 7; dayNum++) {
                dataForCount[dayNum] = data.get(dayNum)[1];
            }

            float result = (float) StatisticsMethods.middleValue(dataForCount);
            return this.getResultString(c, Float.toString(result));

        } catch (Error error) {
            throw new ParseException("Ошибка метода", 0);
        }
    }

    private String getWeeklyForecast(ArrayList<String[]> data) throws ParseException {
        try {
            String[][] dataForResult = new String[14][2];
            for (int dayNum = 7; dayNum < 14; dayNum++) {
                dataForResult[dayNum] = data.get(dayNum - 7);
            }

            for (int index = 7; index >= 1; index--) {
                String[] dataForCalc = new String[7];
                for (int dayNum = 0; dayNum < 7; dayNum++) {
                    dataForCalc[dayNum] = dataForResult[index + dayNum][1];
                }

                String[] newLine = new String[3];
                String lastDayFromData = dataForResult[index][0];
                Calendar c = getNextDay(lastDayFromData);

                String month = c.get(Calendar.MONTH) + 1 < 10 ?
                        "0" + (c.get(Calendar.MONTH) + 1)
                        : Integer.toString(c.get(Calendar.MONTH) + 1);
                newLine[0] = c.get(Calendar.DAY_OF_MONTH) + "." + month + "." + c.get(Calendar.YEAR);
                newLine[1] = Float.toString((float) StatisticsMethods.middleValue(dataForCalc));
                newLine[2] = getDayOfWeek(c.get(Calendar.DAY_OF_WEEK)) + " ";

                dataForResult[index - 1] = newLine;
            }

            StringBuilder result = new StringBuilder();

            for (int index = 0; index < 7; index++) {
                result.append("\n").append(dataForResult[index][2]).append(dataForResult[index][0]).append(" - ")
                        .append(dataForResult[index][1]);
            }

            return result.toString();
        } catch (Error error) {
            throw new ParseException("Ошибка метода", 0);
        }
    }

    public static void main(String[] args) throws ParseException {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите команду: ");
        String command = in.nextLine();
        String[] commandPattern = command.split("\\s*(\\s|,|!|\\.)\\s*");

        if (!commandPattern[0].equals("rate")) {
            throw new UnsupportedOperationException("Такая операция не поддерживается");
        }

        CSVReader reader = new CSVReader();
        Currency currency = new Currency();

        ArrayList<String[]> data = reader.getFileData(commandPattern[1], 1, 3);

        if (commandPattern[2].equals("tomorrow")) {
            System.out.println(currency.getDailyForecast(data));
        } else if (commandPattern[2].equals("week")) {
            System.out.println(currency.getWeeklyForecast(data));
        }
    }
}
