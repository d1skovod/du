import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class duTest {

    @Test
    void based() {
        assertEquals(new Pair(1.0, du.format.KB), du.based(1024,1024));
        assertEquals(new Pair(2.5, du.format.KB), du.based(2560,1024));
        assertEquals(new Pair(1.0, du.format.MB), du.based(1048576,1024));
        assertEquals(new Pair(1.5, du.format.MB), du.based(1500000,1000));
    }

    @Test
    void filesPaths() throws IOException {
        String[][] files = {{"src/test/files/test1"}, {"src/test/files/test2"},
                {"src/test/files/test3"}, {"src/test/files/"}};
        int[] excepted = {11, 12, 17, 40};

        for (int i = 0; i < excepted.length; i++) {
            assertEquals(excepted[i], du.filesPaths(files[i]).get(0).getValue());
        }
    }

    @Test
    void fileSizeTest(){
        testFunc("-h cogito", false, true, false, new String[]{"cogito"});
        testFunc("-h --si -c", true, true, true, new String[]{""});
        testFunc("-h --si -c cogito ergo sum",
                true, true, true, new String[]{"cogito", "ergo", "sum"});
        testFunc("",false, false, false, new String[]{""});
    }

    public void testFunc(String input, boolean base, boolean form, boolean sum, String[] paths) {
        du d = new du();
        d.fileSize(input.split(" "));
        assertEquals(base, d.siF);
        assertEquals(form, d.hF);
        assertEquals(sum, d.cF);
        if (d.arguments != null) {
            assertArrayEquals(paths, d.arguments);
        } else {
            assert(paths.length == 1);
        }
    }
}