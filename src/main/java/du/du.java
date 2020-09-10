package du;
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

class FileInfo {
    public String name;
    public long size;

    public FileInfo(String name, long sum) {
    }
}

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
    /*
    public static void main(String[] args) throws IOException {
        new du().fileSize(args);
    }
     */
    private static String[] bases = new String[]{"B", "KB", "MB", "GB"};

    public Pair<Double, String> based (long size, int base) {
        int c = 0;
        double nsize = size;
        while (c < 3 && nsize > base) {
            nsize /= base;
            c++;
        }
        Pair<Double, String> ret = new Pair(nsize, c);
        return ret;
    }

    public static ArrayList<FileInfo> filesPaths(String[] paths) throws IOException {
        ArrayList<FileInfo> allFiles = new ArrayList<>();
        for (String name: paths) {
            File file = new File(name);
            if (file.exists()) {
                if (file.isDirectory()) {
                    allFiles.add(new FileInfo(name, Files.walk(Paths.get(name))
                            .filter(p -> p.toFile().isFile())
                            .mapToLong(p -> p.toFile().length())
                            .sum()));
                }   else allFiles.add(new FileInfo(name, file.length()));
            } else allFiles.add(new FileInfo(name, -1));
        }
        return allFiles;
    }

    private static void printFiles(int base, boolean form, boolean sum, String[] paths) throws IOException {
        ArrayList<FileInfo> files = filesPaths(paths);
        if (sum) {
            double summary = 0;
            for (FileInfo file: files) {
                if (file.size != -1)
                    summary += file.size;
            }
            if (form) {
                Pair<Integer, String> formed = new Pair(summary, base);
                System.out.printf("Total size: %.2f %s", (double)formed.getKey(), bases[formed.getKey()]);
            } else System.out.printf("Total size of files: %.2f", summary / base);
        }
        else {
            for (FileInfo file: files) {
                if (file.size == -1) System.out.println ("File " + file.name + " do not exist");
                else {
                    if (form) {
                        Pair<Integer, String> formed = new Pair(file.size, base);
                        System.out.println(file.name + formed.getKey() + bases[formed.getKey()]);
                    }
                    else {
                        System.out.println(file.name + file.size);
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
            /*
            for (int i = 0; i < arguments.size(); i++) {
                File file = new File(arguments[i]);
            }

             */
        }
    }

    public static void main(final String[] arguments) throws IOException {
        final du d = new du();
        d.fileSize(arguments);
        printFiles(d.siF ? 1000 : 1024, d.hF, d.cF, d.arguments);
    }
}
