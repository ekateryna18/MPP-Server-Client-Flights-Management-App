package agency.repository;

import agency.model.Employee;

public interface IEmployeeRepo extends Repository<Integer, Employee> {
    Employee authentificate(String email, String password);
}
