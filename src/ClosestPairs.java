/**
 * Group F
 * Lab Exercise 3. Closest Pairs in the Plane
 * Sofus Albertsen
 * Janett Holst
 * Cristina Matonte
 * Carlos Vinas
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Integer;
import java.lang.System;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ClosestPairs {
    static Scanner scanner;

    private static void readInput(String folderName) throws Exception {
        final FileNameExtensionFilter tspFilter = new FileNameExtensionFilter("tsp files", "tsp");
        final File folder = new File(folderName);
        for (final File file : folder.listFiles()) {
            if(tspFilter.accept(file)) {
                readFile(file);
            }
        }
    }

    private static void readFile(File file) throws Exception{
        int n = 0;
        try {
            scanner = new Scanner(file);
            String line;
            // Checks the top lines
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).startsWith("NODE_COORD_SECTION")) {
                if (line.startsWith("DIMENSION")) {
                    String[] lineItems = line.replaceAll(" ", "").split(":");
                    n = Integer.parseInt(lineItems[1]);
                }
            }
            // Scans the nodes
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.trim().startsWith("EOF") || line.trim().isEmpty())
                    break;
                String[] lineItems = line.trim().split("\\s+");
                String name = lineItems[0];
                Double x = Double.parseDouble(lineItems[1]);
                Double y = Double.parseDouble(lineItems[2]);
            }
        } finally {
            scanner.close();
        }

        System.out.printf("%s: %d %f\n", file.getPath(), n, 300.01);
    }

    public static void main(String[] args) throws Exception {
        String folder = args.length > 0 ? args[0] : "../data";
        readInput(folder);
    }

    private static class CustomException extends Throwable {
        public CustomException(String message) {
            System.out.println(message);
        }
    }
}