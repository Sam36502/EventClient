package ch.pearcenet.eventclient;

import java.util.Comparator;

/**
 * Age Sorter Comparator
 * @Author Samuel Pearce
 */
public class AgeSorter implements Comparator<Person> {

    @Override
    public int compare(Person o1, Person o2) {
        return o1.getDate().compareTo(o2.getDate());
    }

}
