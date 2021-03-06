package ch.pearcenet.eventclient;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.pearcenet.tui.input.*;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Main Class
 * @Author Samuel Pearce
 *
 * Handles all procedural events
 */
public class Main {

    // CONSTANTS
    public static final String SETTINGS_FILE = "settings.txt";
    public static final String CLIENT_VERSION = "v1.2";
    public static final String CLIENT_AUTHOR = "Samuel Pearce";

    // Global Registry
    public static HashMap<String, String> settingsMap;

    public static void main(String[] args) {

        /*
         * INITIALISATION
         */

        // Load settings
        System.out.print("Loading settings...");
        settingsMap = FileHandler.getProperties(SETTINGS_FILE);
        if (!validateSettings()) log("ERROR", System.lineSeparator() + "Settings file is invalid.");
        System.out.println("Done." + System.lineSeparator());

        AnsiConsole.systemInstall();
        Input.openScanner();
        Input.setPrompt(" > ");

        /*
         * MAIN PROCEDURE
         */

        boolean isRunning = true;
        while (isRunning) {

            System.out.println(ansi().eraseScreen().cursor(1, 1).fg(Ansi.Color.CYAN) +
                    "  _____                 _      _    ____ ___ " + System.lineSeparator() +
                    " | ____|_   _____ _ __ | |_   / \\  |  _ \\_ _|" + System.lineSeparator() +
                    " |  _| \\ \\ / / _ \\ '_ \\| __| / _ \\ | |_) | | " + System.lineSeparator() +
                    " | |___ \\ V /  __/ | | | |_ / ___ \\|  __/| | " + System.lineSeparator() +
                    " |_____| \\_/ \\___|_| |_|\\__/_/   \\_\\_|  |___|" + System.lineSeparator() +
                    ansi().fg(Ansi.Color.WHITE)
            );
            System.out.println("VERSION: " + CLIENT_VERSION);
            System.out.println("CREATOR: " + CLIENT_AUTHOR + System.lineSeparator());

            // Main Menu
            System.out.println(System.lineSeparator() +
                    ansi().fg(Ansi.Color.CYAN) + " Main Menu:" + ansi().fg(Ansi.Color.WHITE) + System.lineSeparator() +
                    "------------" + System.lineSeparator() +
                    " 1) Search people" + System.lineSeparator() +
                    " 2) Show upcoming birthdays" + System.lineSeparator() +
                    " 3) Add person" + System.lineSeparator() +
                    " 4) Add people from a CSV" + System.lineSeparator() +
                    " 5) Quit"
            );
            int opt = Input.getInt(1, 5);
            switch (opt) {
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
                    csvMenu();
                    break;

                case 5:
                    isRunning = false;
                    continue;
            }
            System.out.println(ansi().eraseScreen().cursor(1, 1));

        }

        /*
         * SHUTDOWN
         */
        System.out.println("Quitting...");

        Input.closeScanner();
        AnsiConsole.systemUninstall();

        System.out.println("Thank you for using EventAPI!");

    }

    /**
     * Interactive Menu to search for people
     */
    private static void searchPerson() {

        System.out.println(ansi().eraseScreen().cursor(1, 1) +
                System.lineSeparator() +
                "First name to search by (blank for none):");
        String firstname = Input.getString();

        System.out.println(System.lineSeparator() +
                "Last name to search by (blank for none):");
        String lastname = Input.getString();

        System.out.println(System.lineSeparator() +
                "Date of birth to search by (blank for none) [YYYY-MM-DD]:");

        // Validate date format
        LocalDate date = null;
        boolean isValid = false;
        while (!isValid) {
            isValid = true;
            String input = Input.getString();
            if (input.length() < 1) {
                date = null;
                continue;
            }

            try {
                date = LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Only dates matching the format YYYY-MM-DD or nothing are allowed.");
                isValid = false;
            }
        }

        // Send search to endpoint
        System.out.println("Searching...");
        if (firstname.length() < 1) firstname = null;
        if (lastname.length() < 1) lastname = null;
        List<Person> people = PersonEndpoint.read(firstname, lastname, date, -1L);
        System.out.println("Done. Found " + people.size() + " results.");

        // Search result menu
        boolean inSearchMenu = true;
        while (inSearchMenu) {

            System.out.println(System.lineSeparator() + ansi().eraseScreen().cursor(1, 1).fg(Ansi.Color.CYAN) +
                    " Search results:" + System.lineSeparator() + ansi().fg(Ansi.Color.WHITE) +
                    "-----------------"
            );
            for (Person person : people)
                System.out.println(" [" + person.getId() + "]: " + person.getFirstname() + " " + person.getLastname());

            // Display Menu
            System.out.println(System.lineSeparator() +
                    "What would you like to do?" + System.lineSeparator() +
                    " 1) Select a person" + System.lineSeparator() +
                    " 2) Back to main menu"
            );
            int opt = Input.getInt(1, 2);
            if (opt == 2) {
                inSearchMenu = false;
                continue;
            }

            // View a specific person
            isValid = false;
            int id = -1;
            while (!isValid) {
                System.out.println("Enter the ID (number in []s) of a person from the list, or -1 to return to search menu:");
                id = Input.getInt();
                for (Person p : people)
                    if (id == (int) p.getId())
                        isValid = true;

            }
            personMenu(id);
            people = PersonEndpoint.read(firstname, lastname, date, -1L);
        }
    }

    /**
     * In-Depth Person menu
     */
    private static void personMenu(long id) {
        if (id == -1L) return;

        Person person = PersonEndpoint.readById(id);

        // Person Menu
        boolean onPersonMenu = true;
        while (onPersonMenu) {
            System.out.println(ansi().eraseScreen().cursor(1, 1) + "" + person);

            System.out.println(System.lineSeparator() +
                    "What would you like to do?:" + System.lineSeparator() +
                    " 1) Edit person" + System.lineSeparator() +
                    " 2) Delete person" + System.lineSeparator() +
                    " 3) Return to search"
            );
            int opt = Input.getInt(1, 3);
            switch (opt) {
                case 1:
                    person = updatePerson(id);
                    break;

                case 2:
                    deletePerson(id);
                    break;

                case 3:
                    onPersonMenu = false;
                    break;
            }

        }

    }

    /**
     * Interactive list of upcoming birthdays
     */
    private static void showBirthdays() {
        Month currMonth = LocalDate.now().getMonth();
        Month nextMonth = currMonth.plus(1);

        System.out.println(ansi().eraseScreen().cursor(1, 1) + System.lineSeparator() + "Showing birthdays for this month and next month:");
        List<Person> allPeople = PersonEndpoint.readAll();
        List<Person> people = new ArrayList<>();

        // Filter out other people
        for (Person person: allPeople) {
            if (person.getDate().getMonth() == currMonth ||
                    person.getDate().getMonth() == nextMonth
            ) people.add(person);
        }
        people.sort(new AgeSorter());

        // Display current month birthdays
        System.out.println(System.lineSeparator() +
                " " + ansi().fg(Ansi.Color.CYAN) + currMonth.toString() + ansi().fg(Ansi.Color.WHITE) + System.lineSeparator() +
                "--" + repeat("-", currMonth.toString().length())
        );
        for (Person person: people) {
            if (person.getDate().getMonth() == currMonth)
                System.out.println(" " +
                        ordinal(person.getDate().getDayOfMonth()) + ": " +
                        person.getFirstname() + " " +
                        person.getLastname() +
                        " - Turning " + (person.getAge() + 1)
                );
        }

        // Display next month birthdays
        System.out.println(System.lineSeparator() +
                " " + ansi().fg(Ansi.Color.CYAN) + nextMonth.toString() + ansi().fg(Ansi.Color.WHITE) + System.lineSeparator() +
                "--" + repeat("-", nextMonth.toString().length())
        );
        for (Person person: people) {
            if (person.getDate().getMonth() == nextMonth)
                System.out.println(" " +
                        ordinal(person.getDate().getDayOfMonth()) + ": " +
                        person.getFirstname() + " " +
                        person.getLastname() +
                        " - Turning " + (person.getAge() + 1)
                );
        }

        Input.setPrompt("");
        System.out.println("Press enter to return to main menu.");
        Input.getString();
        Input.setPrompt(" > ");
    }

    private static String repeat(String str, int reps) {
        String res = "";
        for (int i=0; i<reps; i++) {
            res += str;
        }
        return res;
    }

    // Takes and integer and gives its ordinal
    // e.g.: 1 -> 1st, 34 -> 34th
    private static String ordinal(int num) {
        switch (num) {
            case 1:
                return "1st";

            case 2:
                return "2nd";

            case 3:
                return "3rd";

            default:
                return num + "th";
        }
    }

    /**
     * Interactive menu for loading people from a csv
     */
    private static void csvMenu() {
        System.out.println(ansi().eraseScreen().cursor(1, 1) +
                System.lineSeparator() + "Filename to scan:");
        String filename = Input.getString();

        System.out.println(System.lineSeparator() + "What is the row separator? (blank for newline)");
        String rowSep = Input.getString();
        if (rowSep.length() < 1) rowSep = System.lineSeparator() + "|\n";

        System.out.println(System.lineSeparator() + "What is the column separator? (blank for ';')");
        String colSep = Input.getString();
        if (colSep.length() < 1) colSep = ";";

        System.out.println(System.lineSeparator() + "Should the first row be ignored?");
        boolean ignoreHeader = Input.getBool();

        System.out.println(System.lineSeparator() + "File should look like similar to this:");
        System.out.println(" 'John" + colSep + "Smith" + colSep + "1999-01-01" +
                ((System.lineSeparator() + "|\n").equals(rowSep) ? rowSep : "") + "'"
        );

        // Confirm format
        System.out.println("Is this correct?");
        if (!Input.getBool()) {
            System.out.println("Please re-organise your data and try again.");
            return;
        }

        // Load Data
        System.out.println("Loading Data...");
        List<Person> people = FileHandler.loadPersonCsv(filename, rowSep, colSep, ignoreHeader);

        if (people.size() < 1) {
            System.out.println("No people could be loaded.");
        }

        // If the file only contains one person
        if (people.size() == 1) {
            System.out.println("Uploading person...");
            PersonEndpoint.create(people.get(0));
            System.out.println("Done!");
            return;
        }

        System.out.println("First two Rows:" + System.lineSeparator() +
                people.get(0) + System.lineSeparator() +
                people.get(1)
        );
        System.out.println("Does this look correct?");
        if (!Input.getBool()) {
            System.out.println("Please re-organise your data and try again.");
            return;
        }

        // Upload data
        System.out.println("Uploading people...");
        for (Person person: people) {
            PersonEndpoint.create(person);
        }
        System.out.println("Done!");
    }

    /**
     * Interactive dialogue to create a new person
     */
    private static void createPerson() {
        String firstname = "";
        while (firstname.length() < 1) {
            System.out.println(ansi().eraseScreen().cursor(1, 1) +
                    System.lineSeparator() +
                    "New Person's first name:");
            firstname = Input.getString();
        }

        String lastname = "";
        while (lastname.length() < 1) {
            System.out.println(System.lineSeparator() +
                    "New Person's last name:");
            lastname = Input.getString();
        }

        System.out.println(System.lineSeparator() +
                "New Person's Date of birth [YYYY-MM-DD]:");

        // Validate date format
        LocalDate date = null;
        boolean isValid = false;
        while (!isValid) {
            isValid = true;
            String input = Input.getString();

            try {
                date = LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Only dates matching the format YYYY-MM-DD are allowed.");
                isValid = false;
            }
        }

        // Create new Person
        System.out.println("Creating new Person...");
        PersonEndpoint.create(new Person(firstname, lastname, date));
        System.out.println("Done!");
    }

    /**
     * Interactive dialogue to update an existing person
     */
    private static Person updatePerson(long id) {

        Person updated = PersonEndpoint.readById(id);
        boolean confirmed = false;
        while (!confirmed) {
            System.out.println(ansi().eraseScreen().cursor(1, 1) +
                    System.lineSeparator() +
                    "Person's new first name (blank to leave unchanged):");
            String firstname = Input.getString();

            System.out.println(System.lineSeparator() +
                    "Person's new last name (blank to leave unchanged):");
            String lastname = Input.getString();

            System.out.println(System.lineSeparator() +
                    "Person's new date of birth (blank to leave unchanged) [YYYY-MM-DD]:");

            // Validate date format
            LocalDate date = null;
            boolean isValid = false;
            while (!isValid) {
                isValid = true;
                String input = Input.getString();
                if (input.length() < 1) {
                    date = null;
                    continue;
                }

                try {
                    date = LocalDate.parse(input);
                } catch (DateTimeParseException e) {
                    System.out.println("Only dates matching the format YYYY-MM-DD or nothing are allowed.");
                    isValid = false;
                }
            }

            // Create updated person
            if (firstname.length() > 0) updated.setFirstname(firstname);
            if (lastname.length() > 0) updated.setLastname(lastname);
            if (date != null) updated.setDate(date);

            System.out.println(updated);
            System.out.println(System.lineSeparator() + "Is this correct?");
            confirmed = Input.getBool();
        }

        System.out.print("Updating...");
        PersonEndpoint.update(id, updated.getFirstname(), updated.getLastname(), updated.getDate());
        System.out.println("Done!");

        return updated;
    }

    /**
     * Interactive dialogue to delete and existing person
     */
    private static void deletePerson(long id) {
        Person toDelete = PersonEndpoint.readById(id);
        String fullname = toDelete.getFirstname() + " " + toDelete.getLastname();

        // Confirmation
        System.out.println(System.lineSeparator() +
                "Are you sure you want to delete " + fullname + "?");
        boolean confirmed = Input.getBool();
        if (!confirmed) {
            System.out.println(ansi().fg(Ansi.Color.RED) + "Cancelling deletion..." + ansi().fg(Ansi.Color.WHITE));
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                Main.log("WARN", "Thread sleep exception occurred.");
            }
            return;
        }

        // Double confirmation
        System.out.println(System.lineSeparator() +
                "Type their full name to confirm:");
        String input = Input.getString();
        if (!fullname.equals(input)) {
            System.out.println(ansi().fg(Ansi.Color.RED) + "Cancelling deletion..." + ansi().fg(Ansi.Color.WHITE));
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                Main.log("WARN", "Thread sleep exception occurred.");
            }
            return;
        }

        PersonEndpoint.delete(id);
        System.out.println(ansi().fg(Ansi.Color.RED) + "Deleting Person..." + ansi().fg(Ansi.Color.WHITE));
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            Main.log("WARN", "Thread sleep exception occurred.");
        }
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
