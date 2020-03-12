public class Worker
{
    private String name;
    private double salary;
    private boolean authorization;      // states permission to create a new table or to take any order

    public Worker(String name, double salary) {
        this.name = name;
        this.salary = salary;
        this.authorization = true;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public boolean isAuthorization() {
        return authorization;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setAuthorization(boolean authorization) {
        this.authorization = authorization;
    }
}
