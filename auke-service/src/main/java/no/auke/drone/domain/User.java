package no.auke.drone.domain;

import no.auke.drone.annotation.Column;

public class User {
    @ID
    @Column
    private String id;

    @ID
    @Column
    private String username;

    @Column
    private String password;

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return username.equals("admin") && password.equals("admin");
    }

}
