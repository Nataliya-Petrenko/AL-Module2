package com.petrenko.util;

import com.petrenko.exceptions.ProductDescriptionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
public class ConverterCsvToListOfMap {

    public static List<Map<String, String>> csvToListOfMap(InputStreamReader input, String separator) {
        List<Map<String, String>> listOfMap = new LinkedList<>();
        try {
            putPairsFromInputToListOfMaps(input, separator,listOfMap);
        } catch (IOException | ProductDescriptionException e) {
            System.out.println(e.getMessage());
        }
        return listOfMap;
    }

    private static void putPairsFromInputToListOfMaps(InputStreamReader input,
                                                      String separator,
                                                      List<Map<String, String>> listOfMap) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(input);
        String firstString = bufferedReader.readLine();
        String[] typeOfFields = firstString.split(separator);
        String string;
        while ((string = bufferedReader.readLine()) != null) {
            String[] fields = string.split(separator);

            try {
                checkFieldsNumber(typeOfFields, fields);
            } catch (ProductDescriptionException e) {
                System.out.println(e.getMessage());
                continue;
            }

            Map<String, String> map = putPairsFromArraysToMap(typeOfFields, fields);
            listOfMap.add(map);
        }
    }

    private static void checkFieldsNumber(String[] typeOfFields, String[] fields){
        if (typeOfFields.length != fields.length) {
            throw new ProductDescriptionException("The number of fields in the first line and " +
                    "the current line are different. " +
                    "This line will not convert to map-product: " + Arrays.deepToString(fields));
        }
    }

    private static Map<String, String> putPairsFromArraysToMap(String[] typeOfFields, String[] fields) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < typeOfFields.length; i++) {
            map.put(typeOfFields[i], fields[i]);
        }
        return map;
    }

}
