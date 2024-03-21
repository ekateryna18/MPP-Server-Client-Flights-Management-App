package agency.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@jakarta.persistence.Entity
@Table(name="employee")
//@AttributeOverride(name = "id_employee", column = @Column(name = "id_employee"))
public class Employee extends agency.model.Entity<Integer> {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public Employee(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    public Employee(String email, String password)
    {
        this("", "", email, password);
    }
    public Employee(){
    }

    @Column(name="first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    @Column(name="last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
}
