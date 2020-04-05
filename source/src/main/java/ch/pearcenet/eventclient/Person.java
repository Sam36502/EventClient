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

    public Person() {};

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

    public Person setId(long id) {
        this.id = id;
        return this;
    }

    public Person setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public Person setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public Person setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Person setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    @Override
    public String toString() {
        return "Person " +this.id + ":" + System.lineSeparator() +
                "--------------------------" + System.lineSeparator() +
                " First Name   : " + this.firstname + System.lineSeparator() +
                " Last Name    : " + this.lastname + System.lineSeparator() +
                " Date of Birth: " + this.date + System.lineSeparator() +
                " Entry Created: " + this.createdOn + System.lineSeparator();
    }
}