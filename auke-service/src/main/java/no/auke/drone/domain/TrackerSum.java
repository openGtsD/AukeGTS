package no.auke.drone.domain;

/**
 * Created by huyduong on 6/29/2015.
 */
public interface TrackerSum {
    int getNumtrackers();

    void setNumtrackers(int numtrackers);

    public void incrementTrackers();
}
