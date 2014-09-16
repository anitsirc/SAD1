/**
 * Group F
 * Lab Exercise 3. Closest Pairs in the Plane
 * Sofus Albertsen
 * Janett Holst
 * Cristina Matonte
 * Carlos Vinas
 */

import java.awt.geom.Point2D;
import java.io.File;
import java.lang.Integer;
import java.lang.System;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ClosestPairs {
    static Scanner scanner;
    static Mergesort mergesort = new Mergesort();

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
        Point2D.Double[] P = null;
        try {
            scanner = new Scanner(file);
            String line;
            // Checks the top lines
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).startsWith("NODE_COORD_SECTION")) {
                if (line.startsWith("DIMENSION")) {
                    String[] lineItems = line.replaceAll(" ", "").split(":");
                    n = Integer.parseInt(lineItems[1]);
                    P = new Point2D.Double[n];
                }
            }
            // Scans the nodes
            int i = 0;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.trim().startsWith("EOF") || line.trim().isEmpty())
                    break;
                String[] lineItems = line.trim().split("\\s+");
                String name = lineItems[0];
                Double x = Double.parseDouble(lineItems[1]);
                Double y = Double.parseDouble(lineItems[2]);
                P[i] = new Point2D.Double(x,y);
                i++;
            }
        } finally {
            scanner.close();
        }
        double minDistance = closestPair(P);
        if (minDistance == (int) minDistance) {
            System.out.printf("%s: %d %d\n", file.getPath(), n, (int) minDistance);

        } else {
            BigDecimal bg = new BigDecimal(Double.toString(minDistance), new MathContext(15));
//            bg.setScale(3, BigDecimal.ROUND_HALF_UP);
            System.out.println(file.getPath() + ": " + n + " " + bg);
        }
    }

    public static double closestPair(Point2D.Double[] P){
        // Construct Px and Py
        Point2D.Double[] Px = P.clone();
        Point2D.Double[] Py = P.clone();
        mergesort.sort(Px, "x");
        mergesort.sort(Py, "y");
        return closestPairRec(Px, Py);
    }

    public static double closestPairRec(Point2D.Double[] Px, Point2D.Double[] Py) {
        // findClosestPair
        double min_distance = Double.MAX_VALUE;
        if (Px.length <= 3) {
            double distance = Px[0].distance(Px[1]);
            min_distance = Double.min(min_distance, distance);
            if (Px.length > 2) {
                distance = Px[0].distance(Px[2]);
                min_distance = Double.min(min_distance, distance);
                distance = Px[1].distance(Px[2]);
                min_distance = Double.min(min_distance, distance);
            }
            return min_distance;
        }

        // Construct Qx, Qy, Rx, Ry
        // TODO: make this O(n)
        Point2D.Double[] Qx = java.util.Arrays.copyOfRange(Px, 0, Px.length / 2);
        Point2D.Double[] Rx = java.util.Arrays.copyOfRange(Px, Px.length / 2, Px.length);
        Point2D.Double[] Qy = Qx.clone();
        Point2D.Double[] Ry = Rx.clone();
        mergesort.sort(Qy, "y");
        mergesort.sort(Ry, "y");

        double distanceQ = closestPairRec(Qx, Qy);
        double distanceR = closestPairRec(Rx, Ry);
        double delta = Double.min(distanceQ, distanceR);
        double maxX = Qx[Qx.length - 1].getX();
        //Building S
        ArrayList<Point2D.Double> S = new ArrayList<Point2D.Double>();
        // Looping in Q
        for (int i = Qx.length - 1; i >= 0; i--) {
          if (Qx[i].getX() >= maxX - delta) {
              S.add(Qx[i]);
          } else {
              break;
          }
        }
        // Looping in R
        for (int i = 0; i < Rx.length ; i++) {
            if (Rx[i].getX() <= maxX + delta)
                S.add(Rx[i]);
            else
                break;
        }

        // Construct Sy
        // TODO: on O(n) time
//        Point2D.Double [] Sy = (Point2D.Double []) S.toArray();
        Point2D.Double [] Sy = new Point2D.Double[S.size()];
        for(int i= 0; i < S.size(); i++) {
            Sy[i] = S.get(i);
        }
        mergesort.sort(Sy, "y");
        min_distance = delta;
        double currentDistance;
        for (int j=0; j< Sy.length; j++) {
            Point2D.Double s = Sy[j];
            int minBound = Integer.min(j + 1, Sy.length);
            int maxBound = Integer.min(j + 15, Sy.length);
            for (int i = minBound; i < maxBound; i ++) {
                currentDistance = s.distance(Sy[i]);
                if (currentDistance < min_distance) {
                    min_distance = currentDistance;
                }
            }
        }
        return min_distance;
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