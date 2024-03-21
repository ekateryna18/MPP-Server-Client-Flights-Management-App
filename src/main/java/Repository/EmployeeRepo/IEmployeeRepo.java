package Repository.EmployeeRepo;

import Model.Employee;
import Repository.Repository;

public interface IEmployeeRepo extends Repository<Integer, Employee> {
    Employee authentificate(String email, String password);
}
