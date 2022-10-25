package com.example.lock_practice.com.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonReaderUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T getObject(String filePath, String fileName, Class<T> type) {
        Path currentFile = Paths.get(filePath + fileName);
        try {
            return objectMapper.readValue(currentFile.toFile(), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

