package com.ageinghippy.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Util {

    public static LocalDate getDateFromUser(String prompt) {
        Scanner scanner = new Scanner(System.in);
        String input;
        LocalDate date = null;
        do {
            System.out.print(prompt +" : ");
            input = scanner.nextLine();

            try {
                date =  LocalDate.parse(input);
            }
            catch (DateTimeParseException e) {
                System.err.println("Invalid date entered. Expected format YYYY-MM-DD");
            }
        } while (date == null);
        return date;
    }

    public static LocalTime getTimeFromUser(String prompt) {
        Scanner scanner = new Scanner(System.in);
        String input;
        LocalTime time = null;
        do {
            System.out.print(prompt+" : ");
            input = scanner.nextLine();

            try {
                time =  LocalTime.parse(input);
            }
            catch (DateTimeParseException e) {
                System.err.println("Invalid time entered. Expected format HH:MI[:SS]");
            }
        } while (time == null);
        return time;
    }

    public static int getIntFromUser(String prompt) {
        Scanner scanner = new Scanner(System.in);
        String input;
        Integer i = null;
        do {
            System.out.print(prompt+" : ");
            input = scanner.nextLine();

            try {
                i =  Integer.parseInt(input);
            }
            catch (NumberFormatException e) {
                System.err.println("Invalid number entered. Expected integer");
            }
        } while (i == null);
        return i;
    }

    public static String getStringFromUser(String prompt) {
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.print(prompt+" : ");
            input = scanner.nextLine();
            if (input.isBlank()) {
                System.err.println("Invalid string entered. Expected it should not be blank");
            }
            System.out.println();
        } while (input.isBlank());
        return input;
    }
}
