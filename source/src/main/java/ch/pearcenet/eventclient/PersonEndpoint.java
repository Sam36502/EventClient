package ch.pearcenet.eventclient;

import jdk.vm.ci.meta.Local;

import java.time.LocalDate;
import java.util.List;

/**
 * Person Endpoint Class
 * @Author Samuel Pearce
 *
 * Handles all CRUD operations for Person endpoint of the Event API
 */
public class PersonEndpoint {

    /**
     * CREATE
     * Inserts a new Person into the Database.
     *
     * @param person New Person to create
     */
    public void create(Person person) {

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
    public List<Person> read(String firstname, String lastname, LocalDate date, long id) {
        return null;
    }

    // Convenience Methods
    public List<Person> readByFirstname(String firstname) { return read(firstname, null, null, -1); }
    public List<Person> readByLastname(String lastname) { return read(null, lastname, null, -1); }
    public List<Person> readByBirthdate(LocalDate date) { return read(null, null, date, -1); }
    public List<Person> readById(long id) { return read(null, null, null, id); }
    public List<Person> readAll() { return read(null, null, null, -1); }

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
    public void update(long id, String firstname, String lastname, LocalDate date) {

    }

    /**
     * DELETE
     * Deletes a Person from the database.
     *
     * @param id The ID of the person to delete
     */
    public void delete(long id) {

    }

}
