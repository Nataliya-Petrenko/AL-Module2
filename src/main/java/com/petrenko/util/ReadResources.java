package com.petrenko.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadResources {

    public static InputStreamReader input(String file) throws FileNotFoundException {
        InputStream inputStream = inputStream(file);
        if (inputStream == null) {
            throw new FileNotFoundException("File not exist");
        }
        return new InputStreamReader(inputStream);
    }

    private static InputStream inputStream(String file) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream(file);
    }

}
