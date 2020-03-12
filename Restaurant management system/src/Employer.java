public class Employer extends Worker
{
    private static final int MAX_EMPLOYER = 5;
    private static int numberOfEmployer = 0;
    private static final int ALLOWED_MAX_TABLE = 2;
    private int numberOfCreatedTable = 0;

    public Employer(String name, int salary) {
        super(name, salary);
    }

    public Table createTable(int capacity)
    {
        Table table = new Table(Table.getTotalNumberOfTable(), capacity, false, getName());
        numberOfCreatedTable++;
        return table;
    }

    public static int getMaxEmployer()     {   return MAX_EMPLOYER;   }
    public static int getNumberOfEmployer()     {   return numberOfEmployer;   }
    public static void incrementEmpNum()   {    numberOfEmployer++;     }
    public int getNumberOfCreatedTable()    {   return numberOfCreatedTable;    }
    public static int getAllowedMaxTable()  {   return ALLOWED_MAX_TABLE;   }
}
