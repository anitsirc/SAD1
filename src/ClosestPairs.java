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
    static HashMap<Node, Integer> posInX;

    // Goes through each .tsp file in the input folder
    private static void readInput(String folderName) throws Exception {
        final FileNameExtensionFilter tspFilter = new FileNameExtensionFilter("tsp files", "tsp");
        final File folder = new File(folderName);
        for (final File file : folder.listFiles()) {
            if(tspFilter.accept(file)) {
                readFile(file);
            }
        }
    }

    // Parses each file and calculates the closest Pair
    private static void readFile(File file) throws Exception{
        int n = 0;
        Node[] P = null;
        try {
            scanner = new Scanner(file);
            String line;
            // Checks the top lines
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).startsWith("NODE_COORD_SECTION")) {
                if (line.startsWith("DIMENSION")) { // Looks for the number of nodes
                    String[] lineItems = line.replaceAll(" ", "").split(":");
                    n = Integer.parseInt(lineItems[1]);
                    P = new Node[n];
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
                P[i] = new Node(name, x, y);
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
            bg.setScale(3, BigDecimal.ROUND_HALF_UP);
            System.out.println(file.getPath() + ": " + n + " " + bg);
        }
    }

    // Builds Qy and Ry. Loops through the sorted Py. O(n)
    private static Node[][] buildQyAndRy(Node[] Py, Node[] Qx, Node[] Rx, int start, int end) {
        Node[] Qy = new Node[Qx.length];
        Node[] Ry = new Node[Rx.length];
        int middle = (start + end) / 2;
        int posInQ = 0;
        int posInR = 0;
        for(Node p : Py) {
            if (start <= posInX.get(p) && posInX.get(p) < middle ) { // p is in Qx
                Qy[posInQ] = p;
                posInQ++;
            } else if(middle <= posInX.get(p) && posInX.get(p) < end) { // p is in Rx
                Ry[posInR] = p;
                posInR++;
            }
            if (posInQ >= Qx.length && posInR >= Rx.length)
                break;
        }
        return new Node[][]{Qy, Ry};
    }

    // Builds S. An ArrayList consisting of points in P within distance ~ of L. O(n)
    private static ArrayList<Node> buildS(Node[] Qx, Node[] Rx, double maxX, double delta) {
        ArrayList<Node> S = new ArrayList<Node>();
        // Looping in Q
        for (int i = Qx.length - 1; i >= 0; i--) {
            if (Qx[i].point.getX() >= maxX - delta) {
                S.add(Qx[i]);
            } else {
                break;
            }
        }
        // Looping in R
        for (int i = 0; i < Rx.length ; i++) {
            if (Rx[i].point.getX() <= maxX + delta)
                S.add(Rx[i]);
            else
                break;
        }
        return S;
    }

    // Builds Sy. Loops through the sorted Py. O(n)
    private static Node[] sortSy(Node[] Py, ArrayList<Node> S) {
        Node[] Sy = new Node[S.size()];
        HashMap<Node, Integer> SPosInX = new HashMap<Node, Integer>();
        for(int i = 0; i < S.size(); i++) {
            SPosInX.put(S.get(i), i);
        }
        int posInSy = 0;
        for(Node s: Py) {
            if (SPosInX.get(s) != null) {
                Sy[posInSy] = s;
                posInSy++;
            }
        }
        return Sy;
    }

    private static double closestPair(Node[] P){
        // Construct Px and Py
        Node[] Px = P.clone();
        Node[] Py = P.clone();
        posInX = new HashMap<Node, Integer>();
        mergesort.sort(Px, "x");
        mergesort.sort(Py, "y");
        for(int i = 0; i < Px.length; i++) {
            posInX.put(Px[i], i);
        }
        return closestPairRec(Px, Py, 0, Px.length);
    }

    private static double closestPairRec(Node[] Px, Node[] Py, int start, int end) {
        double min_distance = Double.MAX_VALUE;
        // Recursion base case. find closest pair when Px has less than 3 elements
        if (Px.length <= 3) {
            double distance = Px[0].point.distance(Px[1].point);
            min_distance = Double.min(min_distance, distance);
            if (Px.length > 2) {
                distance = Px[0].point.distance(Px[2].point);
                min_distance = Double.min(min_distance, distance);
                distance = Px[1].point.distance(Px[2].point);
                min_distance = Double.min(min_distance, distance);
            }
            return min_distance;
        }

        // Construction of Qx, Rx. O(n)
        Node[] Qx = java.util.Arrays.copyOfRange(Px, 0, Px.length / 2);
        Node[] Rx = java.util.Arrays.copyOfRange(Px, Px.length / 2, Px.length);
        // Construction of Qy, Ry. O(n)
        Node[][] sortedInY = buildQyAndRy(Py, Qx, Rx, start, end);
        Node[] Qy = sortedInY[0];
        Node[] Ry = sortedInY[1];

        // Find min distance and delta.
        int middle = (start + end) / 2;
        double distanceQ = closestPairRec(Qx, Qy, start, middle);
        double distanceR = closestPairRec(Rx, Ry, middle, end);
        double delta = Double.min(distanceQ, distanceR);
        double maxX = Qx[Qx.length - 1].point.getX();

        ArrayList<Node> S = buildS(Qx, Rx, maxX, delta); // Builds S
        Node[] Sy = sortSy(Py, S); // Construct Sy

        //For each point s ~ Sy, compute distance from s to each of next 15 points in Sy
        min_distance = delta;
        double currentDistance;
        for (int j=0; j< Sy.length; j++) {
            Node s = Sy[j];
            int minBound = Integer.min(j + 1, Sy.length);
            int maxBound = Integer.min(j + 15, Sy.length);
            for (int i = minBound; i < maxBound; i ++) {
                currentDistance = s.point.distance(Sy[i].point);
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

    public static class Node {
        public Point2D.Double point;
        public String name;

        public Node(String name, double x, double y) {
            this.name = name;
            this.point = new Point2D.Double(x,y);
        }
    }

    private static class CustomException extends Throwable {
        public CustomException(String message) {
            System.out.println(message);
        }
    }
}