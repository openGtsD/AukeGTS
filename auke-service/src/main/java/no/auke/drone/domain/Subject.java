package no.auke.drone.domain;

/**
 * Created by huyduong on 3/24/2015.
 */
public interface Subject {
    void register(Observer observer);
    void remove(Observer observer);
    void notifyAllItems();
}
