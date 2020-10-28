package ru.sber.service;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;

public class ReadFileImplTest extends TestCase {
    private Path destPath = Paths.get("./dirFofScan/test.json");
    private Path soursePath = Paths.get("./filesfortest/test.json");
    private Exec readFile = new ReadFileImpl();

    @BeforeClass
    public void setUp() {

        try {
            for (int i = 0; i < 3; i++) {
                Files.copy(soursePath, destPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadFile() {
        boolean exec = readFile.exec("./dirFofScan", "test.json");
        Assert.assertTrue("Expect true: ", exec);
    }
}