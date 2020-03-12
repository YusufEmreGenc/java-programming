import java.io.*;

public class Assignment2
{
    public static void main(String[] args)
    {
        Restaurant restaurant = new Restaurant();
        String command_line;

        try {
            FileReader fileReader = new FileReader("setup.dat");
            BufferedReader bufferedReader = new BufferedReader(fileReader);


            while ((command_line = bufferedReader.readLine()) != null)
            {
                String[] wordsOfCommand = command_line.split(" ");

                if(wordsOfCommand[0].equals("add_item"))
                {
                    String[] dataItem = wordsOfCommand[1].split(";");
                    String nameItem = dataItem[0];
                    double cost = Double.parseDouble(dataItem[1]);
                    int amount = Integer.parseInt(dataItem[2]);
                    restaurant.addItem( new Item(nameItem, cost, amount) );
                }
                else if (wordsOfCommand[0].equals("add_employer"))
                {
                    if(Employer.getNumberOfEmployer() == Employer.getMaxEmployer())
                    {
                        System.out.println("***********************************");
                        System.out.println("PROGRESSING COMMAND: add_employer");
                        System.out.println("Not allowed to exceed max. number of employers, " + Employer.getMaxEmployer());
                        continue;
                    }
                    String[] dataEmployer = wordsOfCommand[1].split(";");
                    String name = dataEmployer[0];
                    int salary = Integer.parseInt(dataEmployer[1]);
                    restaurant.addEmployer( new Employer(name, salary), Employer.getNumberOfEmployer());
                    Employer.incrementEmpNum();
                }
                else if (wordsOfCommand[0].equals("add_waiter"))
                {
                    if(Waiter.getNumberOfWaiter()== Waiter.getMaxWaiter())
                    {
                        System.out.println("***********************************");
                        System.out.println("PROGRESSING COMMAND: add_waiter");
                        System.out.println("Not allowed to exceed max. number of waiters, " + Waiter.getMaxWaiter());
                        continue;
                    }
                    String[] dataWaiter = wordsOfCommand[1].split(";");
                    String name = dataWaiter[0];
                    int salary = Integer.parseInt(dataWaiter[1]);
                    restaurant.addWaiter( new Waiter(name, salary), Waiter.getNumberOfWaiter());
                    Waiter.incrementWaiterNum();
                }
                else
                {
                    System.out.println("Invalid command. It is ignored");
                    continue;
                }
            }

            // commands.dat part
            fileReader = new FileReader("commands.dat");
            bufferedReader = new BufferedReader(fileReader);

            while((command_line = bufferedReader.readLine()) != null)
            {
                System.out.println("***********************************");
                String[] wordsOfCommand = command_line.split(" ");

                if(wordsOfCommand[0].equals("create_table"))
                {
                    restaurant.makeTableCreated(wordsOfCommand[1]);
                }
                else if(wordsOfCommand[0].equals("new_order"))
                {
                    restaurant.newOrder(wordsOfCommand[1]);
                }
                else if(wordsOfCommand[0].equals("add_order"))
                {
                    restaurant.addOrder(wordsOfCommand[1]);
                }
                else if(wordsOfCommand[0].equals("check_out"))
                {
                    restaurant.checkOut(wordsOfCommand[1]);
                }
                else if(wordsOfCommand[0].equals("stock_status"))
                {
                    restaurant.stockStatus();
                }
                else if(wordsOfCommand[0].equals("get_table_status"))
                {
                    restaurant.getTableStatus();
                }
                else if(wordsOfCommand[0].equals("get_order_status"))
                {
                    restaurant.getOrderStatus();
                }
                else if(wordsOfCommand[0].equals("get_employer_salary"))
                {
                    restaurant.getEmployerSalary();
                }
                else if(wordsOfCommand[0].equals("get_waiter_salary"))
                {
                    restaurant.getWaiterSalary();
                }
            }

            bufferedReader.close();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("The file cannot be opened.");
            return;
        }
        catch(IOException ex)
        {
            System.out.println("The file cannot be read.");
            return;
        }
    }
}
