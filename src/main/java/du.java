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
import java.util.LinkedList;

public class du {
    private LinkedList<String> files = new LinkedList<>();

    @Option(name = "-h", usage = "Usable format")
    private boolean hF;

    @Option(name = "-c", usage = "Summary size")
    private boolean cF;

    @Option(name = "--si", usage = "1000 base")
    private boolean siF;

    @Argument
    private String[] arguments;

    private static String[] bases = new String[]{"B", "KB", "MB", "GB"};

    public static Pair<Double, Integer> based(double size, int base) {
        int c = 0;
        while (c < 3 && size >= base) {
            size /= base;
            c++;
        }
        Pair<Double, Integer> ret = new Pair(size, c);
        return ret;
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
                }   else allFiles.add(new Pair<String, Long>(name, file.length()));
            } else allFiles.add(new Pair<String, Long>(name, (long) -1));
        }
        return allFiles;
    }

    private static void printFiles(int base, boolean form, boolean sum, String[] paths) throws IOException {
        ArrayList<Pair<String, Long>> files = filesPaths(paths);
        if (sum) {
            double summary = 0;
            for (Pair<String, Long> file: files) {
                if (file.getValue() != -1)
                    summary += file.getValue();
            }
            if (form) {
                Pair<Double, Integer> formed = based(summary, base);
                System.out.printf("Total size: %.2f %s", (double)formed.getKey(), bases[formed.getValue()]);
            } else System.out.printf("Total size of files: %.2f", summary / base);
        }
        else {
            for (Pair<String, Long> file: files) {
                if (file.getValue() == -1) System.out.println ("File " + file.getValue() + " do not exist");
                else {
                    if (form) {
                        Pair<Double, Integer> formed = based(file.getValue(), base);
                        System.out.println(file.getValue() + formed.getKey() + bases[formed.getValue()]);
                    }
                    else {
                        System.out.println(file.getKey() + file.getValue());
                    }
                }
            }
        }
    }

    private void fileSize(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        if (arguments.length == 0) {
            parser.printUsage(System.err);
            System.exit(0);
        }
        else {
            try {
                parser.parseArgument(args);
            }
            catch (CmdLineException e) {
                System.err.println(e.getMessage());
                System.err.println("java du [options...] arguments...");
                System.err.println();
            }
        }
    }

    public static void main(final String[] arguments) throws IOException {
        final du d = new du();
        d.fileSize(arguments);
        printFiles(d.siF ? 1000 : 1024, d.hF, d.cF, d.arguments);
    }
}
