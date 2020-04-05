package ch.pearcenet.eventclient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;

import ch.pearcenet.tui.input.*;

/**
 * Main Class
 * @Author Samuel Pearce
 *
 * Handles all procedural events
 */
public class Main {

    // CONSTANTS
    public static final String SETTINGS_FILE = "settings.txt";
    public static final String CLIENT_VERSION = "v1.0";
    public static final String CLIENT_AUTHOR = "Samuel Pearce";

    // Global Registry
    public static HashMap<String, String> settingsMap;

    public static void main(String[] args) {

        /*
         * INITIALISATION
         */

        // Load settings
        System.out.print("Loading settings...");
        settingsMap = FileHandler.getData(SETTINGS_FILE);
        if (!validateSettings()) log("ERROR", System.lineSeparator() + "Settings file is invalid.");
        System.out.println("Done." + System.lineSeparator());

        System.out.println("  _____                 _      _    ____ ___ " + System.lineSeparator() +
                " | ____|_   _____ _ __ | |_   / \\  |  _ \\_ _|" + System.lineSeparator() +
                " |  _| \\ \\ / / _ \\ '_ \\| __| / _ \\ | |_) | | " + System.lineSeparator() +
                " | |___ \\ V /  __/ | | | |_ / ___ \\|  __/| | " + System.lineSeparator() +
                " |_____| \\_/ \\___|_| |_|\\__/_/   \\_\\_|  |___|");
        System.out.println("VERSION: " + CLIENT_VERSION);
        System.out.println("CREATOR: " + CLIENT_AUTHOR + System.lineSeparator());

        // Input Config
        Input.openScanner();
        Input.setPrompt(" > ");

        /*
         * MAIN PROCEDURE
         */

        boolean isRunning = true;
        while (isRunning) {

            // Main Menu
            System.out.println(" Main Menu:" + System.lineSeparator() +
                    "------------" + System.lineSeparator() +
                    " 1) Search people" + System.lineSeparator() +
                    " 2) Show upcoming birthdays" + System.lineSeparator() +
                    " 3) Add person" + System.lineSeparator() +
                    " 4) Update person" + System.lineSeparator() +
                    " 5) Delete person" + System.lineSeparator() +
                    " 6) Settings" + System.lineSeparator() +
                    " 7) Quit"
            );
            switch (Input.getInt(1, 7)) {
                case 1:
                    searchPerson();
                    break;

                case 2:
                    showBirthdays();
                    break;

                case 3:
                    createPerson();
                    break;

                case 4:
                    updatePerson();
                    break;

                case 5:
                    deletePerson();
                    break;

                case 6:
                    settingsMenu();
                    break;

                case 7:
                    isRunning = false;
                    continue;
            }

        }

        /*
         * SHUTDOWN
         */
        System.out.println("Quitting...");

        Input.closeScanner();

        System.out.println("Thank you for using EventAPI!");

    }

    /**
     * Interactive Menu to search for people
     */
    private static void searchPerson() {

    }

    /**
     * Interactive list of upcoming birthdays
     */
    private static void showBirthdays() {

    }

    /**
     * Interactive dialogue to create a new person
     */
    private static void createPerson() {

    }

    /**
     * Interactive dialogue to update an existing person
     */
    private static void updatePerson() {

    }

    /**
     * Interactive dialogue to delete and existing person
     */
    private static void deletePerson() {

    }

    /**
     * Interactive menu to change settings
     */
    private static void settingsMenu() {

    }

    /**
     * Method to check the currently loaded settings
     * @return Whether the loaded settings are valid or not
     */
    public static boolean validateSettings() {
        return settingsMap.containsKey("api.url") &&
                settingsMap.containsKey("api.person") &&
                settingsMap.containsKey("api.person.firstname") &&
                settingsMap.containsKey("api.person.lastname") &&
                settingsMap.containsKey("api.person.dateofbirth") &&
                settingsMap.containsKey("api.person.id") &&
                settingsMap.containsKey("api.person.return.firstname") &&
                settingsMap.containsKey("api.person.return.lastname") &&
                settingsMap.containsKey("api.person.return.dateofbirth") &&
                settingsMap.containsKey("api.person.return.id") &&
                settingsMap.containsKey("api.person.return.createddate") &&
                settingsMap.containsKey("api.timeout");
    }

    /**
     * Logger function
     * @param severity How severe this log message is. Prints to stderr if the severity is 'ERROR'
     * @param msg The message to display
     */
    public static void log(String severity, String msg) {
        String message = "[" +
                LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) +
                "] " +
                severity +
                ": " +
                msg;

        if ("ERROR".equals(severity)) {
            System.err.println(message);
            System.exit(1);
        }
        else System.out.println(message);
    }

}
