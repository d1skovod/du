import javafx.util.Pair;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

    public static ArrayList<Pair<String, Long>> filesPaths(String[] paths) throws IOException {
        ArrayList<Pair<String, Long>> allFiles = new ArrayList<>();
        for (String name: paths) {
            File file = new File(name);
            if (file.exists()) {
                if (file.isDirectory()) {
                    allFiles.add(new Pair<String, Long>(name, Files.walk(Paths.get(name))
                            .filter(p -> p.toFile().isFile())
                            .mapToLong(p -> p.toFile().length())
                            .sum()));
                }   else {
                    allFiles.add(new Pair<String, Long>(name, file.length()));
                }
            } else {
                allFiles.add(new Pair<String, Long>(name, (long) -1));
            }
        }
        return allFiles;
    }

    public static void printFiles(boolean base, boolean form, boolean sum, String[] paths) throws IOException {
        int b = base ? 1000 : 1024;
        List<Pair<String, Long>> files = filesPaths(paths);
        if (sum) {
            double summary = 0;
            for (Pair<String, Long> file: files) {
                if (file.getValue() != -1)
                    summary += file.getValue();
            }
            if (form) {
                Pair<Double, Enum> formed = based(summary, b);
                System.out.printf("Total size: %.2f %s", (double)formed.getKey(), formed.getValue());
            } else System.out.printf("Total size of files: %.2f", summary / b);
        }
        else {
            for (Pair<String, Long> file: files) {
                if (file.getValue() == -1) System.out.println ("File " + file.getValue() + " do not exist");
                else {
                    if (form) {
                        Pair<Double, Enum> formed = based(file.getValue(), b);
                        System.out.println(file.getKey() + " " + formed.getKey() + formed.getValue());
                    }
                    else {
                        System.out.println(file.getKey() + " " + file.getValue());
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
                System.err.println("Error: wrong arguments");
            }
        } else {
            parser.printUsage(System.err);
            System.exit(0);
        }
    }

    public static void main(final String[] arguments) throws IOException, NullPointerException {
        du d = new du();
        d.fileSize(arguments);
        printFiles(d.siF, d.hF, d.cF, d.arguments);
    }
}
