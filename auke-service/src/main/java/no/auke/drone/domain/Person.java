package no.auke.drone.domain;

public class Person extends User{
    @ID
    private String id;
    
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
