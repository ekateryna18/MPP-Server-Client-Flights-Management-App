package agency.network.dto;

import java.io.Serializable;

public class EmployeeDTO implements Serializable {
    private String email;
    private String password;
    public EmployeeDTO(String email){
        this(email, "");
    }

    public EmployeeDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString(){
        return "EmployeeDTO["+email+' ' + password+"]";
    }
}
