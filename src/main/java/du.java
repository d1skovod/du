import javafx.util.Pair;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class du {

    @Option(name = "-h", usage = "Usable format")
    private boolean hF;

    @Option(name = "-c", usage = "Summary size")
    private boolean cF;

    @Option(name = "--si", usage = "1000 base")
    private boolean siF;

    @Argument(required = true)
    private String[] arguments;

    enum format {
        B,
        KB,
        MB,
        GB
    }

    public static Pair<Double, Enum> based(double size, int base) {
        int c = 0;
        while (c < 3  && size >= base) {
            size /= base;
            c++;
        }
        switch (c) {
            case 0 : return new Pair(size, format.B);
            case 1 : return new Pair(size, format.KB);
            case 2 : return new Pair(size, format.MB);
            case 3 : return new Pair(size, format.GB);
        }
        return null;
    }

    public static Map<String, Long> filesPaths(String[] paths) throws IOException {
        Map<String, Long> allFiles = null;
        for (String name: paths) {
            File file = new File(name);
            if (file.exists()) {
                if (file.isDirectory()) {
                    allFiles.put(name, Files.walk(Paths.get(name))
                            .filter(p -> p.toFile().isFile())
                            .mapToLong(p -> p.toFile().length())
                            .sum());
                }   else {
                    allFiles.put(name, file.length());
                }
            } else {
                allFiles.put(name, (long) -1);
            }
        }
        return allFiles;
    }

    public static void printFiles(int base, boolean form, boolean sum, String[] paths) throws IOException {
        Map<String, Long> files = filesPaths(paths);
        if (sum) {
            double summary = 0;
            for (Map.Entry<String, Long> entry: files.entrySet()) {
                if (entry.getValue() != -1)
                    summary += entry.getValue();
            }
            if (form) {
                Pair<Double, Enum> formed = based(summary, base);
                System.out.printf("Total size: %.2f %s", (double)formed.getKey(), formed.getValue());
            } else System.out.printf("Total size of files: %.2f", summary / base);
        }
        else {
            for (Map.Entry<String, Long> entry: files.entrySet()){
                if (entry.getValue() == -1) System.out.println ("File " + entry.getValue() + " do not exist");
                else {
                    if (form) {
                        Pair<Double, Enum> formed = based(entry.getValue(), base);
                        System.out.println(entry.getKey() + " " + formed.getKey() + formed.getValue());
                    }
                    else {
                        System.out.println(entry.getKey() + " " + entry.getValue());
                    }
                }
            }
        }
    }

    void fileSize(String[] args) {
        final CmdLineParser parser = new CmdLineParser(this);
        if (args.length != 0) {
            try {
                parser.parseArgument(args);
            }
            catch (CmdLineException e) {
                System.err.println(e.getMessage());
                System.err.println("java du [options...] arguments...");
            }
        } else {
            parser.printUsage(System.err);
            System.exit(0);
        }
    }

    public static void main(final String[] arguments) throws IOException, NullPointerException {
        du d = new du();
        d.fileSize(arguments);
        printFiles(d.siF ? 1000 : 1024, d.hF, d.cF, d.arguments);
    }
}
