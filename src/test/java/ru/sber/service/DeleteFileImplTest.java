package ru.sber.service;

import junit.framework.TestCase;
import org.junit.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeleteFileImplTest extends TestCase {
    private final Path destPath = Paths.get("./dirFofScan/test_delete.txt");
    private final Exec deleteFile = new DeleteFileImpl();

    public void testExec() throws IOException {
        Files.createFile(destPath);
        Assert.assertTrue("File exist", Files.exists(destPath));
        boolean exec = deleteFile.exec("./dirFofScan", "test_delete.txt");
        Assert.assertTrue("Expect true: ", exec);
        Files.exists(destPath);
        Assert.assertFalse("File deleted", Files.exists(destPath));
    }
}