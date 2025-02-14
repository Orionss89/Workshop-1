package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    static final String FILE_NAME = "tasks.csv";
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    static String[][] tasks;
    static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        tasks = loadDataToTab(FILE_NAME);

        while (true) {
            printOptions();
            String input = SCANNER.nextLine().trim();
            switch (input) {
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask(getTheNumber());
                    break;
                case "list":
                    printTab(tasks);
                    break;
                case "exit":
                    saveTabToFile(FILE_NAME, tasks);
                    System.out.println(ConsoleColors.RED + "Exit program" + ConsoleColors.RESET);
                    System.exit(0);
                    break;
                default:
                    System.out.println(ConsoleColors.PURPLE + "Invalid option, please try again." + ConsoleColors.RESET);
            }
        }
    }

    public static void printOptions() {
        System.out.println(ConsoleColors.BLUE + "Please select an option:" +ConsoleColors.RESET);
        for (String option : OPTIONS) {
            System.out.println(option);
        }
    }

    public static String[][] loadDataToTab(String fileName) {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            System.out.println("File not exist.");
            System.exit(0);
        }

        String[][] tab = null;
        try {
            List<String> strings = Files.readAllLines(path);
            if (strings.isEmpty()) {
                System.out.println("File is empty.");
                return new String[0][0];
            }
            tab = new String[strings.size()][strings.get(0).split(",").length];
            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return tab;
    }


    public static void saveTabToFile(String fileName, String[][] tab) {
        Path dir = Paths.get(fileName);
        String[] lines = new String[tasks.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }
        try {
            Files.write(dir, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void addTask() {
        System.out.println("Please add task description:");
        String description = SCANNER.nextLine();
        System.out.println("Please add task due date:");
        String dueDate = SCANNER.nextLine();
        System.out.println("Is your task important: true/false");
        String isImportant = SCANNER.nextLine();
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[]{description, dueDate, isImportant};
    }

    private static void removeTask(int index) {
        try {
            if (index < tasks.length) {
                tasks = ArrayUtils.remove(tasks, index);
                System.out.println("Task successfully removed.");
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Element not exist in tab");
        }
    }

    public static int getTheNumber() {
        System.out.println("Please enter a task number to remove:");
        while (true) {
            String input = SCANNER.nextLine().trim();
            if (NumberUtils.isParsable(input)) {
                int number = Integer.parseInt(input);
                if (number >= 0) {
                    return number;
                }
            }
            System.out.println("Invalid input. Please enter a number >= 0.");
        }
    }

    public static void printTab(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }
}
