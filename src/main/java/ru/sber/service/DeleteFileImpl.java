package ru.sber.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class DeleteFileImpl implements Exec {
    boolean flag = false;

    @Override
    public boolean exec(String filePath, String fileName) {
        try {
            Path path1 = Paths.get(filePath + "/" + fileName);
            Files.delete(path1);
            log.info("File {} deleted", fileName);
            flag = true;
        } catch (IOException e) {
            log.error("Coud not delete file: {}.", fileName, e);
        }
        return flag;
    }
}
