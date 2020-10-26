import lombok.extern.slf4j.Slf4j;
import service.*;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
public class FileWatchService {
    private final Exec deleteFile = new DeleteFileImpl();
    private final Exec readFile = new ReadFileImpl();

    public void watch(String filePath) {
        try {

            log.info("Scaning process started");
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
                log.info("Directory for scan: " + filePath + " created.");
            } else log.info("Directory for scan: " + filePath);

            WatchService watchService
                    = FileSystems.getDefault().newWatchService();

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            System.out.println("Process started");

            WatchKey key;
            while ((key = watchService.take()) != null) {

                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind().name().equals("ENTRY_CREATE")) log.info("new file " + event.context() + " added");
                    if (event.kind().name().equals("ENTRY_MODIFY"))
                        log.info("new file " + event.context() + " modified");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String fileName = event.context().toString();
                            if (!event.context().toString().endsWith(".json") && !event.context().toString().endsWith(".xml")) {
                                deleteFile.exec(filePath, fileName);
                            } else {
                                readFile.exec(filePath, fileName);
                            }
                        }
                    }).start();
                }
                key.reset();
//            throw new InterruptedException();
            }
        } catch (InterruptedException | IOException e) {
            log.error("Something went wrong couse: " + e.toString());
            e.printStackTrace();
        }
    }
}
