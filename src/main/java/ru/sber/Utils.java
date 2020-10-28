package ru.sber;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
public class Utils {

    public static String formatFileTime(String filePath, String fileName) {
        LocalDateTime localDateTime = null;
        try {

            Path path = Paths.get(filePath + "/" + fileName);
            BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
            localDateTime = basicFileAttributes.creationTime()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (IOException e) {
            log.error("Error at {}", Utils.class, e);
        }

        return localDateTime.format(
                DateTimeFormatter.ofPattern("MM.dd.yyyy HH:mm:ss"));

    }

}
