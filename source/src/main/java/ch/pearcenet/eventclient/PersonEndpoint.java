package ch.pearcenet.eventclient;

import org.json.JSONArray;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Person Endpoint Class
 * @Author Samuel Pearce
 *
 * Handles all CRUD operations for Person endpoint of the Event API
 */
public class PersonEndpoint {

    private static String PERSON_URL = Main.settingsMap.get("api.url") + Main.settingsMap.get("api.person");

    /**
     * CREATE
     * Inserts a new Person into the Database.
     *
     * @param person New Person to create
     */
    public static void create(Person person) {

        // Convert person to parameter list
        HashMap<String, String> params = new HashMap<>();
        params.put(Main.settingsMap.get("api.person.firstname"), person.getFirstname());
        params.put(Main.settingsMap.get("api.person.lastname"), person.getLastname());
        params.put(Main.settingsMap.get("api.person.dateofbirth"), person.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

        RequestHandler.sendRequest("POST", PERSON_URL, params);
    }

    /**
     * READ
     * Retrieves a list of People from the
     * Database with optional search criteria
     *
     * @param firstname Optional: Searches for people with this first name
     * @param lastname Optional: Searches for people with this last name.
     * @param date Optional: Searches for people with this date of birth.
     * @param id Optional: Searches for people with this ID. (-1, if not being used)
     * @return List of people that match these search criteria
     */
    public static List<Person> read(String firstname, String lastname, LocalDate date, long id) {

        // Fill parameter map
        HashMap<String, String> params = new HashMap<>();
        if (firstname != null) params.put(Main.settingsMap.get("api.person.firstname"), firstname);
        if (lastname != null) params.put(Main.settingsMap.get("api.person.lastname"), lastname);
        if (date != null) params.put(Main.settingsMap.get("api.person.dateofbirth"), date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        if (id != -1L) params.put(Main.settingsMap.get("api.person.id"), "" + id);

        // Send request
        String response = RequestHandler.sendRequest("GET", PERSON_URL, params);

        // Parse JSON response
        List<Person> resultArr = new ArrayList<>();
        JSONArray people = new JSONArray(response);

        // Iterate through each person
        if (people.length() < 1) return resultArr;
        for (int i=0; i<people.length(); i++) {
            JSONArray curr = people.getJSONArray(i);

            // Parse fields from json with format defined in the settings
            long rId = curr.getLong(Integer.parseInt(Main.settingsMap.get("api.person.return.id")));
            String rFirstname = curr.getString(Integer.parseInt(Main.settingsMap.get("api.person.return.firstname")));
            String rLastname = curr.getString(Integer.parseInt(Main.settingsMap.get("api.person.return.lastname")));
            LocalDate rDate = LocalDate.parse(curr.getString(Integer.parseInt(Main.settingsMap.get("api.person.return.dateofbirth"))));
            LocalDateTime rCreated = LocalDateTime.parse(curr.getString(Integer.parseInt(Main.settingsMap.get("api.person.return.createddate"))),
                    DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));

            // Add Parsed person to the array
            Person newPerson = new Person()
                    .setId(rId)
                    .setFirstname(rFirstname)
                    .setLastname(rLastname)
                    .setDate(rDate)
                    .setCreatedOn(rCreated);
            resultArr.add(newPerson);
        }

        return resultArr;
    }

    // Convenience Methods
    public static List<Person> readByFirstname(String firstname) { return read(firstname, null, null, -1L); }
    public static List<Person> readByLastname(String lastname) { return read(null, lastname, null, -1L); }
    public static List<Person> readByBirthdate(LocalDate date) { return read(null, null, date, -1L); }
    public static List<Person> readAll() { return read(null, null, null, -1L); }

    public static Person readById(long id) {
        List<Person> people = read(null, null, null, id);
        for (Person p: people) System.out.println(p.getFirstname());
        if (people.size() > 0)
            return people.get(0);
        else
            return null;
    }

    /**
     * UPDATE
     * Updates an entry in the Database with
     * new data.
     *
     * @param id Required: The ID of the person to update
     * @param firstname Optional: The new first name to set
     * @param lastname Optional: The new last name to set
     * @param date Optional: The new date of birth to set
     */
    public static void update(long id, String firstname, String lastname, LocalDate date) {

        // Fill parameter map
        HashMap<String, String> params = new HashMap<>();
        if (firstname != null) params.put(Main.settingsMap.get("api.person.firstname"), firstname);
        if (lastname != null) params.put(Main.settingsMap.get("api.person.lastname"), lastname);
        if (date != null) params.put(Main.settingsMap.get("api.person.dateofbirth"), date.format(DateTimeFormatter.ISO_LOCAL_DATE));

        RequestHandler.sendRequest("PUT", PERSON_URL, params);
    }

    // Convenience methods
    public static void updateFirstname(long id, String firstname) { update(id, firstname, null, null); }
    public static void updateLastname(long id, String lastname) { update(id, null, lastname, null); }
    public static void updateDate(long id, LocalDate date) { update(id, null, null, date); }

    /**
     * DELETE
     * Deletes a Person from the database.
     *
     * @param id The ID of the person to delete
     */
    public static void delete(long id) {

        HashMap<String, String> params = new HashMap<>();
        params.put(Main.settingsMap.get("api.person.id"), "" + id);
        RequestHandler.sendRequest("DELETE", PERSON_URL, params);
    }

}
