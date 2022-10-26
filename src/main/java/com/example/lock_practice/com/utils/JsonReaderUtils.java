package com.example.lock_practice.com.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonReaderUtils {

    private final String FILE_PATH;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JsonReaderUtils(String FILE_PATH){
        this.FILE_PATH = FILE_PATH;
    }

    public <T> T getObject(String fileName, Class<T> type) {
        Path currentFile = Paths.get(FILE_PATH + fileName);
        try {
            return objectMapper.readValue(currentFile.toFile(), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

