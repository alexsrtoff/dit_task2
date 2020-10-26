import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        new FileWatchService().watch("/Users/user1234/Projects/dit_task2/dirFofScan");
    }
}
