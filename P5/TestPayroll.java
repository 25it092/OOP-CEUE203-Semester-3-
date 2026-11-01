abstract class Employee{
    protected String name;
    protected int id;
    public Employee(String name, int id){
        this.name = name;
        this.id = id;
    }
    public abstract double monthlySalary();
    public String getName(){
        return name;
    }
    public int getID(){
        return id;
    }
}

class FullTime extends Employee{
    private double fixedSalary;
    public FullTime(String name, int id, double fixedSalary){
        super(name, id);
        this.fixedSalary = fixedSalary;
    }
    public double monthlySalary(){
        return fixedSalary;
    }
} 

class PartTime extends Employee{
    private double hours;
    private double rate;
    public PartTime(String name, int id, double hours, double rate){
        super(name, id);
        this.hours = hours;
        this.rate = rate;
    }
    public double monthlySalary(){
        return (hours * rate);
    }
}

class Intern extends Employee{
    private double stipend;
    public Intern(String name, int id, double stipend){
        super(name, id);
        this.stipend = stipend;
    }
    public double monthlySalary(){
        return stipend;
    }
}

public class TestPayroll{
    public static void main(String[] args){
        Employee[] employees = {
            new FullTime("Ashley", 101, 30000),
            new PartTime("Bob", 102, 8, 100),
            new Intern("Chandler", 103, 10000)
        };
        double totalPay = 0;
        System.out.println();
        for(Employee E : employees){
            double salary = E.monthlySalary();
            totalPay = totalPay + salary;

            System.out.println(" -Emp ID: " + E.getID() + ", Emp Name: " + E.getName() + ", Salary: " + salary 
            + " (of type: " + E.getClass().getSimpleName() + ")");

            if(E instanceof Intern){
                System.out.print(" (Note: *Internship stipend rules* )");
            }
            //System.out.println();
        }
        System.out.println("\n---------------------------------------------------------------------");
        System.out.println("\n ->Total Payroll: " + totalPay + "\n");
    }
}
