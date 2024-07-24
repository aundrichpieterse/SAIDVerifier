/*
Author: Aundrich Pieterse
Date: 24/07/2024
Description: The SAIDVerifier application validates South African ID numbers,
             checking their format, checksum, and extracting personal details such as date of birth, gender, and citizenship status.
Features: Result Logging: The application saves verification results, including the ID number and verification date, to a file for record-keeping.
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Scanner;

public class SAIDVerifier {

    private static String resultFormat = "detailed"; // Default result format

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        loadUserPreferences(); // Load user preferences on startup
        while (true) {
            displayMenu();
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                handleIDVerification(scanner);
            } else if (choice.equals("2")) {
                System.out.println("Exiting the program. Goodbye!");
                saveUserPreferences(); // Save preferences before exiting
                break;
            } else if (choice.equals("3")) {
                displayHelp();
            } else if (choice.equals("4")) {
                displayAbout();
            } else if (choice.equals("5")) {
                configureUserPreferences(scanner);
            } else {
                System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private static void configureUserPreferences(Scanner scanner) {
    }

    public static void displayMenu() {
        System.out.println("Welcome to the South African ID Checker App");
        System.out.println("[1] Verify the authenticity of your ID number");
        System.out.println("[2] Exit the program");
        System.out.println("[3] Help");
        System.out.println("[4] About");
        System.out.print("Make your choice: ");
    }

    public static void handleIDVerification(Scanner scanner) {
        while (true) {
            System.out.println("Enter your ID Number:");
            String idNumber = scanner.nextLine();

            if (idNumber.length() != 13) {
                System.out.println("Invalid ID format. ID must be exactly 13 digits long.");
            } else if (!idNumber.matches("\\d+")) {
                System.out.println("Invalid ID format. ID must only contain digits.");
            } else if (!check_sum(idNumber)) {
                System.out.println("Invalid ID number. The checksum does not match.");
            } else if (!isValidDate(idNumber.substring(0, 6))) {
                System.out.println("Invalid date in ID number.");
            } else {
                decode_id(idNumber);
                saveResultsToFile(idNumber);
                break; // Exit the loop after successful verification
            }

            System.out.println("----- Press any key to try again or type 'exit' to return to the main menu ------");
            String retry = scanner.nextLine();
            if (retry.equalsIgnoreCase("exit")) {
                break;
            }
        }
    }

    public static boolean isValidDate(String dob) {
        try {
            int year = Integer.parseInt(dob.substring(0, 2));
            int month = Integer.parseInt(dob.substring(2, 4));
            int day = Integer.parseInt(dob.substring(4, 6));

            if (month < 1 || month > 12) {
                return false;
            }

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, day);
            int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

            return day > 0 && day <= maxDay;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void decode_id(String idNumber) {
        String dob = idNumber.substring(0, 6);
        String year = dob.substring(0, 2);
        String month = dob.substring(2, 4);
        String day = dob.substring(4, 6);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100;
        if (Integer.parseInt(year) > currentYear) {
            year = "19" + year;
        } else {
            year = "20" + year;
        }

        int birthYear = Integer.parseInt(year);
        int age = Calendar.getInstance().get(Calendar.YEAR) - birthYear;
        int genderCode = Integer.parseInt(idNumber.substring(6, 10));
        String gender = genderCode >= 5000 ? "Male" : "Female";

        char citizenshipStatus = idNumber.charAt(10);
        String status;
        switch (citizenshipStatus) {
            case '0':
                status = "SA Citizen";
                break;
            case '1':
                status = "Permanent Resident";
                break;
            default:
                status = "Unknown";
                break;
        }

        if (resultFormat.equals("summary")) {
            System.out.println("Born: " + day + "/" + month + "/" + year);
            System.out.println("Age: " + age);
            System.out.println("Gender: " + gender);
            System.out.println("Citizenship Status: " + status);
        } else {
            System.out.println("----------------- Valid ID -----------------");
            System.out.println("Born: " + day + "/" + month + "/" + year);
            System.out.println("Age: " + age);
            System.out.println("Gender: " + gender);
            System.out.println("Citizenship Status: " + status);
            System.out.println("----------------- End of Details -----------------");
            System.out.println(" ");
        }
    }

    public static boolean check_sum(String idNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = idNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(idNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public static void saveResultsToFile(String idNumber) {
        try (PrintWriter out = new PrintWriter(new FileWriter("verification_results.txt", true))) {
            out.println("ID Number: " + idNumber);
            out.println("Date of Verification: " + Calendar.getInstance().getTime());
            out.println("Result Format: " + resultFormat);
            out.println("--------------------------------------");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the results.");
        }
    }

    public static void displayHelp() {
        System.out.println(" ");
        System.out.println("Help - SAIDVerifier Application");
        System.out.println("1. Enter a valid 13-digit South African ID number.");
        System.out.println("2. The application will verify the format, checksum, and date.");
        System.out.println("3. If valid, it will display the date of birth, age, gender, and citizenship status.");
        System.out.println("4. You can retry entering the ID number or return to the main menu.");
        System.out.println("5. The application saves the verification results to a file.");
        System.out.println(" ");
    }

    public static void displayAbout() {
        System.out.println(" ");
        System.out.println("About - SAIDVerifier Application");
        System.out.println("Version: 1.1");
        System.out.println("Author: Aundrich Pieterse");
        System.out.println("Description: This application verifies South African ID numbers, checking format, checksum, and extracting personal information.");
        System.out.println(" ");
    }


    public static void loadUserPreferences() {
    }

    public static void saveUserPreferences() {
    }
}
