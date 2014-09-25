import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Group F
 * Lab Exercise 6. Flow Behind Enemy Lines
 * Sofus Albertsen
 * Janett Holst
 * Cristina Matonte
 * Carlos Vinas
 */
public class FlowBehindEnemyLines {
    public static int nodeNumber;
    public static int edgeNumber;
    public static String[] nodes;
    public static int[][] edges;

    public static void main(String[] args) {
        try {
            readInput();
            System.out.println(String.format("Nodes: %d, Edges: %d", nodeNumber, edgeNumber));
            System.out.println("Nodes: " + Arrays.toString(nodes));
            System.out.println("Edges:");
            for(int[] edge: edges) {
                System.out.println(Arrays.toString(edge));
            }
        } catch (FileNotFoundException e) { System.out.println("No such file"); }
    }

    public static void readInput() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("../data/rail.txt"));
        nodeNumber = Integer.parseInt(sc.nextLine());
        nodes = new String[nodeNumber];
        for (int i = 0; i < nodeNumber; i++) {
            nodes[i] = sc.nextLine();
        }
        edgeNumber = Integer.parseInt(sc.nextLine());
        edges = new int[edgeNumber][3];
        for (int i = 0; i < edgeNumber; i++) {
            String[] line = sc.nextLine().split("\\s+");
            edges[i] = new int[] {Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2])};
        }
    }
}
