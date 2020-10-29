package ru.sber;

import lombok.extern.slf4j.Slf4j;
import ru.sber.service.*;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
public class FileWatchService {

    private void watch(final String filePath) {
        try {

            log.info("Scaning process started");
            Path path = Paths.get(filePath);
            checkDirectory(path);

            WatchService watchService = FileSystems.getDefault().newWatchService();

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (final WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind().name().equals("ENTRY_CREATE")) {
                        log.info("Add new file {}, file create at: {}", event.context(), Utils.formatFileTime(filePath, event.context().toString()));
                    }
                    if (event.kind().name().equals("ENTRY_MODIFY"))
                        log.info("Modify file {}, file create at: {}", event.context(), Utils.formatFileTime(filePath, event.context().toString()));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String fileName = event.context().toString();
                            if (!event.context().toString().endsWith(".json") && !event.context().toString().endsWith(".xml")) {
                                new DeleteFileImpl().exec(filePath, fileName);
                            } else {
                                new ReadFileImpl().exec(filePath, fileName);
                            }
                        }
                    }).start();
                }
                key.reset();
            }
        } catch (InterruptedException | IOException e) {
            log.error("Something went wrong ", e);
        }
    }

    private void checkDirectory(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectory(path);
            log.info("Create new directory for scan: {}.", path.toAbsolutePath());
        } else log.info("Directory for scan: {}", path.toAbsolutePath());

    }

    public void startScaning(final String filePath) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                watch(filePath);
            }
        });
        thread.setDaemon(true);
        thread.start();
        System.out.println("Scaning process started. Hit enter to stop it.");
        try {
            System.in.read();
        } catch (IOException e) {
            log.error("Process start error.", e);
        }
        System.out.println("Scaning process stoped.");
    }
}
