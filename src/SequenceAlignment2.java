

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SequenceAlignment2 {
	
	Integer[][] M;
	Integer [][] blosum;
	HashMap<Character, Integer>blosumIndex;
	String a;
	String b;
	
	public static void main(String[] args){
		SequenceAlignment2 s = new SequenceAlignment2(args);
		
		
		
//		System.out.println("Random matrix for words "+ s.a + " and "+ s.b + ":");
//		printMatrix(s.a, s.b, s.M);
	}

	public SequenceAlignment2(String[] args){
		
		System.out.println("Input strings: ");
		HashMap<String, String> strings = readInput(new File(args[0]));
		for (String name: strings.keySet()){

            String key =name.toString();
            String value = strings.get(name).toString();  
            System.out.println("key: "+key);  
            System.out.println("value: " + value);  

		} 
		
		System.out.println();
		blosum = readBlosum62(new File(args[1]));
		String blosumChars = "ARNDCQEGHILKMFPSTWYVBZX*";
		printMatrix(blosumChars, blosumChars, blosum);
		System.out.println();
		blosumIndex = new HashMap<Character, Integer>();
		
		//Make hashmap with indices if the Letters in the Blosum matrix.
		for (int i = 0; i < blosumChars.length(); i++) {
			blosumIndex.put(blosumChars.charAt(i), i);	
		}
		
		
		ArrayList<String> names = new ArrayList<String>(strings.keySet());
		
		for (int i=0; i<names.size(); i++){
			for (int j=i+1; j<names.size();j++){
				findStringDifference(names.get(i), names.get(j), strings.get(names.get(i)), strings.get(names.get(j)));
				
			}
		}
		
		findStringDifference("Snark", "Bandersnatch", "KQRIKAAKABK", "KAK");
		
	}
	
	private void findStringDifference(String nameA, String nameB, String a, String b) {
		
		M = new Integer[a.length()][b.length()]; //The cost measured in gap distance between letters.
		
		for (int i = 0; i < a.length(); i++)
            for (int j = 0; j < b.length(); j++)
            	for (int l=0; i<l; l++) M[i][j] = null;
            	
		int result = compute(0, a, b, a.length(), b.length());
		String s = nameA + "--"+ nameB+ ": "+ result;
		System.out.println(s);
		
		printMatrix(a, b, M);
		
		String[] alterations = traceback(a, b, a.length(), b.length(), result);
		System.out.println(alterations[0]);
		System.out.println(alterations[1]);
	}

	private String[] traceback(String aInit, String bInit, int a, int b, int result) {
		
		//OPT(a, b) = min [ mis + OPT(a-1, b-1)   ,   gap + OPT(a-1, b)	, 	gap + OPT(a, b-1)  ];
		
		int value = result;
		
		String[] trace = findSolution(aInit, bInit, 0,0, "", "");
		
		return trace;
	}

	private String[] findSolution(String aInit, String bInit, int a, int b, String alteredA, String alteredB) {
		
		int d = (-4);
		
		String aI = aInit;
		String bI = bInit;
		String altA = alteredA;
		String altB = alteredB;
		int x = a;
		int y = b;
		System.out.println("x: "+ x + ", y: "+y);
		
		if (x==0 || y==0){
			int opt1 = mismatchCost(aInit.charAt(x), bInit.charAt(y))+M[x][y];
			int opt2 = d + M[x+1][y];
			int opt3 = d + M[x][y+1];
			
			if (opt1 >= opt2 && opt1 >= opt3){
				altA += aInit.substring(x,x+1);
				altB += bInit.substring(y,y+1);
				x+=1;
				y+=1;
				System.out.println("Chose opt0");
				System.out.println("A: " + altA + ", B: "+altB);
				
			} else if(opt2>= opt3){
				altA += "-";
				altB += bInit.substring(y,y+1);
				y+=1;
				System.out.println("Chose opt2");
				System.out.println("A: " + altA + ", B: "+altB);
			} else {
				altA += aInit.substring(x,x+1);
				altB += "-";
				x+=1;
				System.out.println("Chose opt3");
				System.out.println("A: " + altA + ", B: "+altB);
			}
		}
		
		else if(a>=(aInit.length()-1) && b >= (bInit.length()-1)){
			altA += aInit.substring(x,x+1);
			altB += bInit.substring(y,y+1);
			return new String[] {altA, altB};
			
		} else if (a>=(aInit.length()-1)){
			for (int i = b; i < (bInit.length()-1); i++) {
				altA += "-";
				altB += bInit.substring(y,y+1);
				y+=1;
			}
			
		} else if(b >= (bInit.length()-1)){
			for (int i = a; i < (aInit.length()-1); i++) {
				altA += aInit.substring(x,x+1);
				altB += "-";
				x+=1;
			}
		}
		
		else {
			int opt1 = mismatchCost(aInit.charAt(x), bInit.charAt(y))+M[x+1][y+1];
			int opt2 = d + M[x+1][y];
			int opt3 = d + M[x][y+1];
			
			if (opt1 >= opt2 && opt1 >= opt3){
				altA += aInit.substring(x,x+1);
				altB += bInit.substring(y,y+1);
				x+=1;
				y+=1;
				System.out.println("Chose opt1");
				System.out.println("A: " + altA + ", B: "+altB);
				
			} else if(opt2>= opt3){
				altA += "-";
				altB += bInit.substring(y,y+1);
				y+=1;
				System.out.println("Chose opt2");
				System.out.println("A: " + altA + ", B: "+altB);
			} else {
				altA += aInit.substring(x,x+1);
				altB += "-";
				x+=1;
				System.out.println("Chose opt3");
				System.out.println("A: " + altA + ", B: "+altB);
			}
			
		} 
		
		
		return findSolution(aI, bI, x, y, altA, altB);
		
	}

	private Integer compute(int val, String y, String x, int ym, int xn) {
		
//		System.out.println("Calling compute("+val+", "+y+", "+x+", "+ym + ", "+ xn+ ")");
		
		int value = val;
		
		if (ym<1){
			value += ((-4)*xn);
//			int missingSecond = second.length()-pointer;
//			for (int i = 0; i < missingSecond; i++) {
//				value -= 4;
//				first = "-"+first;
//				
			return value;
			
		} else if (xn<1){
			value += ((-4)*ym);
			return value;
			
		} else if(M[ym-1][xn-1]!= null){
//			System.out.println("hit!");
			return M[ym-1][xn-1];
		}
		
//		System.out.println("Input: "+a +" and "+b);
//		System.out.println("Pointer: "+pointer);
//		String newA = first.substring(0,first.length()-pointer)+"-"+first.substring(first.length()-pointer);
//		String newB = second.substring(0,second.length()-pointer)+"-"+second.substring(second.length()-pointer);
		
		int opt1 = mismatchCost(y.charAt(ym-1), x.charAt(xn-1)) + compute(value, y, x, ym-1, xn-1); //+ mismatch cost
		int opt2 = (-4) + compute(value, y, x, ym-1, xn); //+ gap cost
		int opt3 = (-4) + compute(value, y, x, ym, xn-1); //+ gap cost
		
		
		if (opt1> opt2 && opt1> opt3) {
			M[ym-1][xn-1]=opt1;
			return opt1;
		}
		else if(opt2> opt3) {
			M[ym-1][xn-1]=opt2;
			return opt2;
		}
		else {
			M[ym-1][xn-1]=opt3;
			return opt3;
		}
		
	}

	private int mismatchCost(char charAt, char charAt2) {
		
//		System.out.println(blosumIndex.toString());
//		System.out.println("char1: "+ charAt + ", char2: "+charAt2);
		int index1 = blosumIndex.get(charAt);
		int index2 = blosumIndex.get(charAt2);
		
		int value = blosum[index1][index2];
		
		return value;
	}

	public static void printMatrix(final String a, final String b, Integer[][] m){
		//Print first line
		System.out.print("|   |  ");
		for (Integer k=0; k< b.length(); k++){
			System.out.print(String.valueOf(b.charAt(k)) + " |  ");
		}
		System.out.println();
		
		for (Integer i=0; i< a.length(); i++){
			System.out.print("| "+ String.valueOf(a.charAt(i)) + " | ");
			for (Integer j=0; j< b.length(); j++){
				
				String temp = String.valueOf(m[i][j]);
				if (temp.length()==1) System.out.print(" ");
				
				System.out.print(temp);
				System.out.print(" | ");
				
			}
			System.out.println();
		}
	}
	
	public HashMap<String, String> readInput(File arg){
		
		HashMap<String, String> map = new HashMap<String, String>();
		String currentString = "-";
		String genome ="";
		
		Scanner sc;
		try {
			sc = new Scanner(arg);
			while (sc.hasNextLine()){
				String next = sc.nextLine();
				if (String.valueOf(next.charAt(0)).equals(">")){
					
					map.put(currentString, genome);
					genome="";
					
					String sub = next.substring(1);
					map.put(sub, null);
					currentString = sub;
				}
				else {
					genome+=next;
					
					if (sc.hasNextLine()==false){
						map.put(currentString, genome);
					}
				}
				
			}
			
			map.remove("-");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return map;
		
	}
	
	public Integer[][] readBlosum62(File arg){
		
		Integer[][] blosumArray = null;
		Scanner sc;
		Integer numOfLines = 0;
		
		try {
			sc = new Scanner(arg);
			while (sc.hasNext()){
				String next = sc.nextLine();
				if (String.valueOf(next.charAt(0)).equals("#")==false){
					numOfLines ++;
				}
			}
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
		numOfLines -=1;
		System.out.println("Array Height: "+ numOfLines);
		
		Integer line = 0;
		try {
			sc = new Scanner(arg);
			while (sc.hasNext()){
				String next = sc.nextLine();
				if (String.valueOf(next.charAt(0)).equals("#")==false){
					//first line:
					if (line ==0){
						String newString = next.substring(3);
						String[] sub = newString.split("\\s+");
						System.out.println("Length of Array: " + (sub.length));
						blosumArray = new Integer[numOfLines][sub.length];
						
					} else {
						//for the rest of the lines:
						String[] sub = next.split("\\s+");
						Integer[] intSub = new Integer[sub.length];
						for (Integer i=1; i<sub.length; i++) intSub[i-1]=Integer.parseInt(sub[i]);
						blosumArray[line-1] = intSub;
					}
					
					line++;
				}
				
			}
		
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
		return blosumArray;
	}
}
