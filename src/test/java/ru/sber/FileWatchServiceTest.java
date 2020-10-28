package ru.sber;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;


public class FileWatchServiceTest extends TestCase {
    private Path destPath = Paths.get("./dirFofScan");
    private Path soursePath = Paths.get("./filesfortest");
    private WatchService watchService;
    private List<File> filesToCopySourse = new ArrayList<>();
    private List<File> filesToCopyDest = new ArrayList<>();

    @BeforeClass
    public void setUp() {
        filesToCopySourse.add(new File(soursePath + "/test.json"));
        filesToCopySourse.add(new File(soursePath + "/test.xml"));
        filesToCopySourse.add(new File(soursePath + "/test.txt"));
        filesToCopyDest.add(new File(destPath + "/test.json"));
        filesToCopyDest.add(new File(destPath + "/test.xml"));
        filesToCopyDest.add(new File(destPath + "/test.txt"));
        if (!filesToCopyDest.isEmpty()) {
            for (File f : filesToCopyDest) {
                f.delete();
            }
        }
    }


    @Test
    public void testWatchCreateFile() throws IOException, InterruptedException {

        watchService = FileSystems.getDefault().newWatchService();
        Assert.assertNotNull(watchService);

        destPath.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        try {
            for (int i = 0; i < 3; i++) {
                Files.copy(filesToCopySourse.get(i).toPath(), filesToCopyDest.get(i).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        WatchKey key;
        key = watchService.take();
        for (final WatchEvent<?> event : key.pollEvents()) {
            Assert.assertEquals("Expect true:", "ENTRY_CREATE", event.kind().name());
            System.out.println(2);
        }

        Files.write(filesToCopyDest.get(0).toPath(), Files.readAllBytes(filesToCopyDest.get(1).toPath()), StandardOpenOption.APPEND);
        for (final WatchEvent<?> event : key.pollEvents()) {
            Assert.assertEquals("Expect true:", "ENTRY_MODIFY", event.kind().name());

        }
        key.reset();
    }

    @Test
    public void testWatchModifyFile() throws IOException, InterruptedException {

        watchService = FileSystems.getDefault().newWatchService();
        Assert.assertNotNull(watchService);

        soursePath.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        try {
            Files.write(filesToCopySourse.get(0).toPath(), Files.readAllBytes(filesToCopySourse.get(1).toPath()), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WatchKey key;
        key = watchService.take();

        for (final WatchEvent<?> event : key.pollEvents()) {
            Assert.assertEquals("Expect true:", "ENTRY_MODIFY", event.kind().name());

        }
        key.reset();
    }

}