import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

/**
 * Group F Lab Exercise 6. Flow Behind Enemy Lines Sofus Albertsen Janett Holst
 * Cristina Matonte Carlos Vinas
 */
public class FlowBehindEnemyLines {
	public static int nodeNumber;
	public static int edgeNumber;
	public static String[] nodes;
	public static int[][] edges;
	public static int[][] directedEdges;
	public static ArrayList<ArrayList<int[]>> links;
	static HashSet<int[]> searched ;

	public static void main(String[] args) {
		try {
			readInput();
			System.out.println(String.format("Nodes: %d, Edges: %d",
					nodeNumber, edgeNumber));
			System.out.println("Nodes: " + Arrays.toString(nodes));
			System.out.println("Edges:");
			for (int[] edge : directedEdges) {
				System.out.println(Arrays.toString(edge));
			}
			edgesFromVertex();
			dfs();
		} catch (FileNotFoundException e) {
			System.out.println("No such file");
		}
	}

	public static void readInput() throws FileNotFoundException {
		searched = new HashSet<int[]>();
		Scanner sc = new Scanner(new File("data/rail.txt"));
		nodeNumber = Integer.parseInt(sc.nextLine());
		nodes = new String[nodeNumber];
		for (int i = 0; i < nodeNumber; i++) {
			nodes[i] = sc.nextLine();
		}
		edgeNumber = Integer.parseInt(sc.nextLine());

		// [from,to,cap,flow]
		directedEdges = new int[edgeNumber * 2][4];
		for (int i = 0; i < edgeNumber; i++) {
			String[] line = sc.nextLine().split("\\s+");
			int cap = Integer.parseInt(line[2]);
			directedEdges[2 * i] = new int[] { Integer.parseInt(line[0]),
					Integer.parseInt(line[1]),
					(cap < 0 ? Integer.MAX_VALUE : cap), 0 };
			directedEdges[2 * i + 1] = new int[] { Integer.parseInt(line[1]),
					Integer.parseInt(line[0]),
					(cap < 0 ? Integer.MAX_VALUE : cap), 0 };
		}
		sc.close();
	}

	public static void fordFulkerson() {

	}

	// returning all the edges that goes out from the
	public static void edgesFromVertex() {
		links = new ArrayList<ArrayList<int[]>>(nodes.length);
		for (int i = 0; i < nodes.length; i++) {
			links.add(new ArrayList<int[]>());
		}
		ArrayList<int[]> tmp;
		for (int[] i : directedEdges) {
			tmp = links.get(i[0]);
			tmp.add(i);
			links.set(i[0], tmp);
		}
		links.forEach(k -> {
			k.forEach(v -> {
				System.out.println(Arrays.toString(v));
			});
		});
	}

	public static int[][] dfs() {
		Stack<int[]>tmp= dfs(0, new Stack<int[]>());	
		tmp.forEach(v->{
		System.err.println(Arrays.toString(v));	
		});
		return null;
	}

	public static Stack<int[]> dfs(int currentVertex, Stack<int[]> path ) {
		ArrayList<int[]> currentEdges = links.get(currentVertex);
//System.out.println(currentEdges.size());
		for (int[] currentEdge : currentEdges) {
			path.add(currentEdge);
			//If we have searched this edge before.
			if (searched.contains(currentEdge)){
				path.pop();
			} else{
				//add to searched
				searched.add(currentEdge);
				searched.add(opposite(currentEdge));
//				System.out.println(searched);
			//if we are at the sink and there is still capacity left
				if (currentEdge[1] == 54 && currentEdge[3] < currentEdge[2]) {
					return path;
				//else if there is still capacity left, call dfs
				} else if (currentEdge[3] < currentEdge[2]) {
	//				path=
//					System.out.println(searched);
							return dfs(currentEdge[1],path);
				} else {
					path.pop();
				}
			}
		}
		return path;
	}

	private static int[] opposite(int[] currentEdge) {
		ArrayList<int[]> tmp =links.get(currentEdge[1]);
		for (int[] is : tmp) {
			if (is[1]==currentEdge[0]) return is;
		}
		return null;
	}

}
