package ch.pearcenet.eventclient;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Person Class
 * @Author Samuel Pearce
 *
 * Person data object
 */
public class Person {

    private long id;

    private String firstname;

    private String lastname;

    private LocalDate date;

    private LocalDateTime createdOn;

    public Person(String firstname, String lastname, LocalDate date) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getCreatedOn() { return createdOn; }
}
