import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.kohsuke.args4j.Option;

import java.util.LinkedList;

public class du {
    private LinkedList<String> files = new LinkedList<>();

    @Option(name = "du", usage = "Get size")
    private boolean duF = true;

    @Option(name = "-h", usage = "Usable format")
    private boolean hF = true;

    @Option(name = "-c", usage = "Summary size")
    private boolean cF = true;

    @Option(name = "--si", usage = "1000 base")
    private boolean siF = true;

    //@Option(name = ; usage = "File name")
    //files.add();


    public static void main(String[] args) {

    }
}
