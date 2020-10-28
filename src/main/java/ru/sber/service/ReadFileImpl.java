package ru.sber.service;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class ReadFileImpl implements Exec {
    private boolean flag = false;

    @Override
    public boolean exec(String filePath, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath + "/" + fileName))) {
            long start = System.currentTimeMillis();
            int count = 0;
            while ((reader.readLine()) != null) {
                count++;
            }
            long stop = System.currentTimeMillis() - start;
            log.info("Scaning file: {}. Scaning time: {} mils. The file contains: {} rows.", fileName, stop, count);
            flag = true;
        } catch (IOException e) {
            log.error("Coud not read file {}.", fileName, e);
        }
        return flag;
    }

}
