package ch.pearcenet.eventclient;

import org.fusesource.jansi.Ansi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Person Class
 * @Author Samuel Pearce
 *
 * Person data object
 */
public class Person {

    private long id = -1L;

    private String firstname = null;

    private String lastname = null;

    private LocalDate date = null;

    private LocalDateTime createdOn = null;

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

    public int getAge() {
        return this.getDate().until(LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return System.lineSeparator() + ansi().fg(Ansi.Color.CYAN) +
                " Person Menu:" + ansi().fg(Ansi.Color.WHITE) + System.lineSeparator() +
                "--------------" + System.lineSeparator() +
                "   Database ID : " + (this.getId() != -1L ? this.getId() : "n/a") + System.lineSeparator() +
                "    First Name : " + this.getFirstname() + System.lineSeparator() +
                "     Last Name : " + this.getLastname() + System.lineSeparator() +
                "           Age : " + this.getAge() + System.lineSeparator() +
                " Date of Birth : " + this.getDate().format(
                DateTimeFormatter.ofPattern("dd MMMM, uuuu")) + System.lineSeparator() +
                " Entry Created : " + (this.getCreatedOn() != null ? this.getCreatedOn().format(
                DateTimeFormatter.ofPattern("dd MMMM, uuuu; HH:mm")) : "n/a") + System.lineSeparator();
    }

}
