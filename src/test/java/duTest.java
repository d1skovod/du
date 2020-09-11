import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class duTest {

    @Test
    void based() {
        assertEquals(new Pair(1.0, 1), du.based(1024,1024));
        assertEquals(new Pair(2.5, 1), du.based(2560,1024));
        assertEquals(new Pair(1.0, 2), du.based(1048576,1024));
        assertEquals(new Pair(1.5, 2), du.based(1500000,1000));
    }

    @Test
    void filesPaths() throws IOException {
        assertEquals(du.filesPaths(new String[]{"src/test/files/test1"}).get(0).getValue(), 11);
        assertEquals(du.filesPaths(new String[]{"src/test/files/test2"}).get(0).getValue(), 12);
        assertEquals(du.filesPaths(new String[]{"src/test/files/test3"}).get(0).getValue(), 17);
        assertEquals(du.filesPaths(new String[]{"src/test/files/"}).get(0).getValue(), 40);
    }

}