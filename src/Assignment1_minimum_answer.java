//package dk.itu.sad1.assignment1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Assignment1_minimum_answer {
    public static Person[] women;
    public static Person[] men;
    public static Queue<Integer> singles = new LinkedList<Integer>();
    public static int n;

    private static void readN(Scanner scanner) throws CustomException {
        String line = scanner.nextLine();
        while (line.startsWith("#")) {
            line = scanner.nextLine();
        }
        if (!line.startsWith("n=")) throw new CustomException("Unknown file format");
        String n_pattern = "n=(\\d+)";
        n = Integer.parseInt(line.replaceAll(n_pattern, "$1"));
        System.out.println("N = " + String.valueOf(n));
    }

    private static void readPersons(Scanner scanner) {
        String line;
        women = new Person[n];
        men = new Person[n];
        for(int i = 0; i < 2*n; i++) {
            line = scanner.nextLine();
            String[] splitted_line = line.split(" ");
            if (i % 2 == 0) { //men
                men[i/2] = new Person(n, splitted_line[1]);
                singles.add(i/2);
            } else { //woman
                women[i/2] = new Person(n, splitted_line[1]);
            }
        }
    }

    private static void readPreferences(Scanner scanner) {
        String line;
        for(int i = 0; i < 2*n; i++) {
            line = scanner.nextLine();
            String[] splitted_line = line.split(": ");
            String[] string_line_preferences = splitted_line[1].split(" ");
            int[] line_preferences = new int[n];

            if (i % 2 == 0) { //men
                for(int j = 0; j < n; j++) {
                    line_preferences[j] = (Integer.parseInt(string_line_preferences[j]) / 2) -1;
                }
                men[i/2].preferences = line_preferences;
            } else { //woman
                for(int j = 0; j < n; j++) {
                    line_preferences[j] = (Integer.parseInt(string_line_preferences[j]) / 2);
                }
                women[i/2].preferences = line_preferences;
            }
        }
        System.out.println("Preferences for men:");
        for (Person m : men) {
            System.out.println(m.name);
            System.out.println(Arrays.toString(m.preferences));
        }
        System.out.println("Preferences for women:");
        for (Person w : women) {
            System.out.println(w.name);
            System.out.println(Arrays.toString(w.preferences));
        }
    }

    private static void readInput() throws CustomException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);
            readN(scanner);
            readPersons(scanner);
            scanner.nextLine();
            readPreferences(scanner);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public static void main(String[] args) throws CustomException{
        readInput();

        while(!singles.isEmpty()) {
            int current_man = singles.peek();
            Person man = men[current_man];
            int woman = -1;
            for(int i = 0; i < n; i++) { // Looping through current man preferences
                boolean dumped = false;
                if (man.preferences[i] != -1) {
                    woman = man.preferences[i];
                    // Check if woman is free
                    if (women[woman].engaged == -1) { //Free
                        women[woman].engaged = current_man;
                        men[current_man].engaged = woman;
                        singles.remove();
                        break;
                    } else { // engaged
                        int currently_engaged_man = women[woman].engaged;

                        for(int j = 0; j<n; j++) { // Finds out if women shall upgrade
                            if (women[woman].preferences[j] == currently_engaged_man) { //Do not change man
                                break;
                            } else if (women[woman].preferences[j] == current_man) { //Upgrade
                                women[woman].engaged = current_man;
                                men[current_man].engaged = woman;
                                singles.remove();
                                dumped = true;


                                for(int k = 0; k < n; k++) {
                                    if (men[currently_engaged_man].preferences[k] != -1) {
                                        men[currently_engaged_man].preferences[k] = -1;
                                        break;
                                    } else if (k == n - 1) {
                                        throw new CustomException("This problem cannot be solved");
                                    }
                                }
                                men[currently_engaged_man].engaged = -1;
                                singles.add(currently_engaged_man);

                                break;
                            }
                        }
                    }
                    if (dumped) {
                        break;
                    }
                }

            }

        }

//        System.out.println("Final matches: ");
//        for (Person w: women) {
//            System.out.println(men[w.engaged].name + " -- " + w.name);
//        }

        System.out.println("Final matches: ");
        for (Person m: men) {
            System.out.println(m.name + " -- " + women[m.engaged].name);
        }


    }

    private static class Person {
        public String name;
        public int engaged;
        public int[] preferences;

        public Person(int n, String name) {
            this.name = name;
            engaged = -1;
            preferences = new int[n];
        }
    }

    private static class CustomException extends Throwable {
        public CustomException(String message) {
            System.out.println(message);
        }
    }
}