import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class duTest {

    @Test
    void based() {
        assertEquals(new Pair(1.0, du.format.KB), du.based(1024,1024));
        assertEquals(new Pair(2.5, du.format.KB), du.based(2560,1024));
        assertEquals(new Pair(1.0, du.format.MB), du.based(1048576,1024));
        assertEquals(new Pair(1.5, du.format.MB), du.based(1500000,1000));
    }

    /*
    @Test
    void filesPaths() throws IOException {
        String[][] files = {{"src/test/files/test1"}, {"src/test/files/test2"},
                {"src/test/files/test3"}, {"src/test/files/"}};
        Integer[] excepted = {11, 12, 17, 40};

        for (int i = 0; i < excepted.length; i++) {
            Map<String, Long> mp = du.filesPaths(files[i]);
            if (mp != null) {
                for (Map.Entry<String, Long> entry : mp.entrySet()) {
                    assertEquals(excepted[i], entry.getValue());
                }
            }
        }
    }

     */
}