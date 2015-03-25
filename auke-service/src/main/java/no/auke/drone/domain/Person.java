package no.auke.drone.domain;

import java.io.Serializable;

public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String phone, email, IM;
    
    public Person(){
        
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIM() {
        return IM;
    }

    public void setIM(String iM) {
        IM = iM;
    }
    
    
}
