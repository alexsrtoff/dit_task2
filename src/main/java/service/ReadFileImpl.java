package service;

import lombok.extern.slf4j.Slf4j;
import sun.util.calendar.BaseCalendar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class ReadFileImpl implements Exec {
    @Override
    public void exec(String filePath, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath + "/" + fileName))) {
            Date date = new Date();
            long start = System.currentTimeMillis();
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                count++;
            }
            long stop = System.currentTimeMillis() - start;
            log.info("Scaning file: " + fileName + ". Scaning started at: " + date.toString() + ". Scaning time: " + stop + " mils. The file contains: " + count + " rows");
        } catch (IOException e) {
            log.error("Coud not read file " + fileName + ". Couse: " + e.toString());
        }
    }
}
