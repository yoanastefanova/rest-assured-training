package serializabletest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import employee.Employee;
import org.testng.annotations.Test;


public class SerializationDeserialization {

  Employee employee = new Employee("Yoana", "Stefanova", "female",
      24, 3656, false);
  Employee employeeOnlyId = new Employee();
  Employee employee1 = new Employee();
  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void printEmployees() {
    employee.printEmployee();
    System.out.println("-------------------------------------");
    employee1.printEmployee();
  }

  @Test
  public void serializationEmployee() throws JsonProcessingException {
    String employeeJson = objectMapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(employee);
    System.out.println("Serialization:\n");
    System.out.println(employeeJson);

    Employee employeeFromJson = objectMapper.readValue(employeeJson, Employee.class);
    employeeFromJson.printEmployee();
    //    System.out.println("------------------------------\n"+ "Deserialization:\n");
//    System.out.println("First name :- "+employeeFromJson.getFirstName());
//    System.out.println("Last name :- "+employeeFromJson.getLastName());
//    System.out.println("Age :- "+employeeFromJson.getAge());
//    System.out.println("Gender :- "+employeeFromJson.getGender());
//    System.out.println("Salary :- "+employeeFromJson.getSalary());
//    System.out.println("Married :- "+employeeFromJson.getMarried());
//    System.out.println("Eligible for vote :- "+employeeFromJson.getEligibleForVote());
//    System.out.println("Full name :- "+employeeFromJson.getFullName());

    String employeeJson1 = objectMapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(employeeOnlyId);
    System.out.println("Serialization:\n");
    System.out.println(employeeJson1);

  }
}
