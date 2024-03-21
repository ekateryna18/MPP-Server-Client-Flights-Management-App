package agency.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;
@MappedSuperclass
public class Entity<IDtype extends Serializable> implements Serializable{
    private static final long serialVersionUID = 123456789L;
    @Id
    @Column(name="id_employee")
    private IDtype ID;
    public IDtype getID(){
        return ID;
    }
    public void setID(IDtype ID){
        this.ID = ID;
    }
}
