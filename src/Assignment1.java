//package dk.itu.sad1.assignment1;

import java.util.*;

public class Assignment1 {
    public static Woman[] women;
    public static Man[] men;
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
//        System.out.println("N = " + String.valueOf(n));
    }

    private static void readPersons(Scanner scanner) {
        String line;
        women = new Woman[n];
        men = new Man[n];
        for(int i = 0; i < 2*n; i++) {
            line = scanner.nextLine();
            String[] splitted_line = line.split(" ");
            if (i % 2 == 0) { //men
                men[i/2] = new Man(n, splitted_line[1]);
                singles.add(i/2);
            } else { //woman
                women[i/2] = new Woman(n, splitted_line[1]);
            }
        }
    }

    private static void readPreferences(Scanner scanner) {
        String line;
        for(int i = 0; i < 2*n; i++) {
            line = scanner.nextLine();
            String[] splitted_line = line.split(": ");
            String[] string_line_preferences = splitted_line[1].split(" ");
            int person_id = Integer.parseInt(splitted_line[0]) -1;
            int[] line_preferences = new int[n];

            if (person_id % 2 == 0) { //woman
                for(int j = 0; j < n; j++) {
                    men[person_id/2].preferences.add((Integer.parseInt(string_line_preferences[j]) / 2) -1);
                }
            } else { //man
                for(int j = 0; j < n; j++) {
                    line_preferences[Integer.parseInt(string_line_preferences[j]) / 2] = j;
                }
                women[person_id/2].preferences = line_preferences;
            }
        }
//        System.out.println("Preferences for men:");
//        for (Man m : men) {
//            System.out.println(m.name);
//            System.out.println(Arrays.toString(m.preferences.toArray()));
//        }
//        System.out.println("Preferences for women:");
//        for (Woman w : women) {
//            System.out.println(w.name);
//            System.out.println(Arrays.toString(w.preferences));
//        }
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
        // While there is a man m who is free and hasn't proposed to every woman
        while(!singles.isEmpty()) {
            // Chose a man
            int current_man = singles.peek();
            Man man = men[current_man];
            // Get the highest ranked woman
            Integer woman = man.preferences.poll();
            if (woman == null) {
                throw new CustomException("This problem has no solution");
            }
            // Check if woman is free
            if (women[woman].engaged == -1) { //Free
                women[woman].engaged = current_man; // Take that woman
                men[current_man].engaged = woman;
                singles.remove();
            } else { // Engaged
                int currently_engaged_man = women[woman].engaged;

                // Finds out if women shall upgrade
                if (women[woman].preferences[current_man] < women[woman].preferences[currently_engaged_man]) {
                    // Engage
                    women[woman].engaged = current_man;
                    men[current_man].engaged = woman;
                    singles.remove();
                    // Set m' as single
                    men[currently_engaged_man].engaged = -1;
                    singles.add(currently_engaged_man);
                }
            }
        }

        for (Person w: women) {
            System.out.println(men[w.engaged].name + " -- " + w.name);
        }
    }

    private abstract static class Person {
        public String name;
        public int engaged;

    }
    private static class Man extends Person {
        public Queue<Integer> preferences;

        public Man (int n, String name) {
            this.name = name;
            this.engaged = -1;
            this.preferences = new LinkedList<Integer>();
        }
    }

    private static class Woman extends Person {
        public int[] preferences;

        public Woman (int n, String name) {
            this.name = name;
            this.engaged = -1;
            this.preferences = new int[n];
        }
    }

    private static class CustomException extends Throwable {
        public CustomException(String message) {
            System.out.println(message);
        }
    }
}