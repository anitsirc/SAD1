

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SequenceAlignmentMinimum {
	
	
	int [][] blosum;
	HashMap<Character, Integer>blosumIndex;
	String a;
	String b;
	
	public static void main(String[] args){
		SequenceAlignmentMinimum s = new SequenceAlignmentMinimum(args);
		
		
		
//		System.out.println("Random matrix for words "+ s.a + " and "+ s.b + ":");
//		printMatrix(s.a, s.b, s.M);
	}

	public SequenceAlignmentMinimum(String[] args){
		
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
				String s = names.get(i) + "--"+ names.get(j)+
						findStringDifference(strings.get(names.get(i)), strings.get(names.get(j)));
				System.out.println(s);
			}
		}
		
	}
	
	private String findStringDifference(String a, String b) {
		
		if (a.length()==0 && b.length()==0) return "0";
		
		Object[] result = compute(0, a, b, 0);
		
		String finalResult = (int) result[0] + "\n" + (String) result[1] + "\n" + (String) result[2];
		return finalResult;
	}

	private Object[] compute(int val, String a, String b, int pointer) {
		
		int value = val;
		String first = new String(a);
		String second = new String(b);
		
		if (pointer==first.length()){
			int missingSecond = second.length()-pointer;
			for (int i = 0; i < missingSecond; i++) {
				value -= 4;
				first = "-"+first;
				
			}
			Object[] o = {value, first, second, pointer};
			return o;
			
		} else if (pointer==second.length()){
			int missingFirst = first.length()-pointer;
			for (int i = 0; i < missingFirst; i++) {
				value -= 4;
				second = "-"+first;
			}
			Object[] o = {value, first, second, pointer};
			return o;
		}
		
//		System.out.println("Input: "+a +" and "+b);
//		System.out.println("Pointer: "+pointer);
		String newA = first.substring(0,first.length()-pointer)+"-"+first.substring(first.length()-pointer);
		String newB = second.substring(0,second.length()-pointer)+"-"+second.substring(second.length()-pointer);
		
		Object[] opt1 = compute(value + mismatchCost(a.charAt(a.length()-pointer-1), b.charAt(b.length()-pointer-1)), a, b, pointer +1); //+ mismatch cost
		
		Object[] opt2 = compute(value -4, newA, b, pointer +1); //+ gap cost
		Object[] opt3 = compute(value -4, a,  newB, pointer +1); //+ gap cost
		
		if ((int) opt1[0]> (int) opt2[0] && (int) opt1[0]> (int) opt3[0]) return opt1;
		else if((int) opt2[0]> (int) opt3[0]) return opt2;
		else return opt3;
		
	}

	private int mismatchCost(char charAt, char charAt2) {
		
//		System.out.println(blosumIndex.toString());
//		System.out.println("char1: "+ charAt + ", char2: "+charAt2);
		int index1 = blosumIndex.get(charAt);
		int index2 = blosumIndex.get(charAt2);
		
		int value = blosum[index1][index2];
		
		return value;
	}

	public static void printMatrix(final String a, final String b, int[][] m){
		//Print first line
		System.out.print("|   |  ");
		for (int k=0; k< b.length(); k++){
			System.out.print(String.valueOf(b.charAt(k)) + " |  ");
		}
		System.out.println();
		
		for (int i=0; i< a.length(); i++){
			System.out.print("| "+ String.valueOf(a.charAt(i)) + " | ");
			for (int j=0; j< b.length(); j++){
				
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
	
	public int[][] readBlosum62(File arg){
		
		int[][] blosumArray = null;
		Scanner sc;
		int numOfLines = 0;
		
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
		
		int line = 0;
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
						blosumArray = new int[numOfLines][sub.length];
						
					} else {
						//for the rest of the lines:
						String[] sub = next.split("\\s+");
						int[] intSub = new int[sub.length];
						for (int i=1; i<sub.length; i++) intSub[i-1]=Integer.parseInt(sub[i]);
						blosumArray[line-1] = intSub;
					}
					
					line++;
				}
				
			}
		
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
		return blosumArray;
	}
}
