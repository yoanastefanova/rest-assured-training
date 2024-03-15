package employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.UUID;

//@JsonIgnoreProperties(value = {"fullname", "elligibleForVote"}, allowSetters = true)
@JsonInclude(Include.NON_NULL)//won't include null values in serialization
public class Employee {

  @JsonIgnore
//  @JsonTypeId
  private final String id;
  private String firstName;
  private String lastName;
  private String gender;
  private int age;
  private double salary;
  private boolean married;
  @JsonIgnore
  private String fullName;
  @JsonIgnore
  private boolean eligibleForVote;


  public Employee() {
    this.id = UUID.randomUUID().toString();
  }

  public Employee(String firstName, String lastName, String gender, int age, double salary,
      boolean married) {
    this.id = UUID.randomUUID().toString();
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.age = age;
    this.salary = salary;
    this.married = married;
    this.eligibleForVote = this.age > 18;
    this.fullName = firstName + " " + lastName;
  }

  // copy constructor is a way of ensuring a deep copy of an object
  public Employee(Employee employee) {
    this(employee.getFirstName(), employee.getLastName(), employee.getGender(), employee.getAge(),
        employee.getSalary(),
        employee.getMarried());
  }

  public void printEmployee() {
    System.out.println(this.getId());
    System.out.println(this.getFirstName());
    System.out.println(this.getLastName());
    System.out.println(this.getGender());
    System.out.println(this.getAge());
    System.out.println(this.getSalary());
    System.out.println(this.getMarried());
    System.out.println(this.getFullName());
    System.out.println(this.getEligibleForVote());

  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }

  public boolean getMarried() {
    return married;
  }

  public void setMarried(boolean married) {
    this.married = married;
  }

  public String getFullName() {
    return this.fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public boolean getEligibleForVote() {
    return this.eligibleForVote;
  }

  public void setEligibleForVote(boolean eligibleForVote) {
    this.eligibleForVote = eligibleForVote;
  }

  public String getId() {
    return id;
  }
}