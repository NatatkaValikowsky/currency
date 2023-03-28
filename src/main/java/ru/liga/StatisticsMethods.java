package ru.liga;

import java.text.ParseException;

public class StatisticsMethods {
    public static Number middleValue(String[] values) throws ParseException {
        float sum = 0;
        for(int i = 0; i < values.length; i++){
            float value = Float.parseFloat(values[i].replaceAll(",", "."));
            sum = sum + value;
        }

        return sum / values.length;
    }
}
