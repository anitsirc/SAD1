/**
 * Group F
 * Lab Exercise 2. WordLadders
 * Sofus Albertsen
 * Janett Holst
 * Cristina Matonte
 * Carlos Viñas
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class WordLadders {

	public static HashMap<String, Word> words = new HashMap<>();

	private static void readInput(String file) throws CustomException {
		Scanner scanner = null;
		int i;
		try {
			scanner = new Scanner(new File(file));

			while (scanner.hasNext()) {
				i = bfs(scanner.next(), scanner.next());
				System.out.println("" + i);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	private static void readData(String file) throws CustomException {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(file));
			while (scanner.hasNext()) {
				addWord(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	public static void main(String[] args) throws CustomException {
		readData(args[0]);
		readInput(args[1]);

	}

	public static int bfs(String from, String to) {

		if (from.equals(to)) {
			return 0;
		}
		HashMap<String, Integer> searched = new HashMap<String, Integer>();
		searched.put(from, 0);
		Queue<String> q = new LinkedList<String>();
		q.add(from);
		while (!q.isEmpty()) {
			String currentWord = q.poll();
			for (Word linkWord : words.get(currentWord).link) {
				if (!searched.containsKey(linkWord.name)) {
					if (linkWord.name.equalsIgnoreCase(to)) {
						
						return (searched.get(currentWord) + 1);
					}
					searched.put(linkWord.name, searched.get(currentWord) + 1);
					q.add(linkWord.name);
				}
			}
		}
		//System.out.println(searched);
		return -1;
	}


	public static void addWord(String word1) {
		Word new_word = new Word(word1);
		// loop all the existing words in the ladder to find potential matches.
		for (Iterator it = words.values().iterator(); it.hasNext();) {
			Word oldWord = (Word) it.next();
			// look through the last 4 letters in the new word
			if (checkLadder(new_word.name, oldWord.name)) {
				new_word.link.add(oldWord);
			}
			// look through the last 4 letters in the old word
			if (checkLadder(oldWord.name, new_word.name)) {
				oldWord.link.add(new_word);
			}
		}
		words.put(word1, new_word);
	}

	public static boolean checkLadder(String linkFrom, String linkTo) {
		boolean isLadder = false;
		String temp = linkTo;
		for (int i = 1; i < 5; i++) {
			// System.out.println("word :" + linkFrom +" letter at:" + i +
			// " char= "+linkFrom.substring(i, i+1));
			temp = temp.replaceFirst(linkFrom.substring(i, i + 1), "");
		}
		isLadder = (temp.length() == 1);
		return isLadder;
	}

	private static class CustomException extends Throwable {
		public CustomException(String message) {
			System.out.println(message);
		}
	}

	private static class Word {

		public String name;
		private ArrayList<Word> link = new ArrayList<Word>();

		public Word(String word) {
			name = word;
		}

	}
}