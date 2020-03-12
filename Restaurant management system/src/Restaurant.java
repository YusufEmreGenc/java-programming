import java.util.ArrayList;

//It records employers, waiters,tables etc. about management data
public class Restaurant
{
    private ArrayList<Item> items = new ArrayList<Item>();
    private Employer[] employers = new Employer[Employer.getMaxEmployer()];
    private Waiter[] waiters = new Waiter[Waiter.getMaxWaiter()];
    private Table[] tables = new Table[Table.getMaxTables()];

    public void addItem(Item item)      {   items.add(item);    }

    public void addEmployer(Employer employer, int index)   {   employers[index] = employer;     }

    public void addWaiter(Waiter waiter, int index)     {   waiters[index] = waiter;   }

    public void makeTableCreated(String command)
    {
        System.out.println("PROGRESSING COMMAND: create_table");
        String[] dataTable = command.split(";");
        String creatorName = dataTable[0];
        int capacity = Integer.parseInt(dataTable[1]);

        int i = 0;
        for(i = 0; i < Employer.getNumberOfEmployer(); i++)
            if(creatorName.equals(employers[i].getName()))
            {
                if(employers[i].getNumberOfCreatedTable() == Employer.getAllowedMaxTable())
                {
                    System.out.println(creatorName + " has already created " + Employer.getAllowedMaxTable() + " tables!");
                    return;
                }
                if(Table.getTotalNumberOfTable() == Table.getMaxTables())
                {
                    System.out.println("Not allowed to exceed max. number of tables, " + Table.getMaxTables());
                    return;
                }

                tables[Table.getTotalNumberOfTable()] = employers[i].createTable(capacity);
                Table.incrementTotalNumOfTables();
                System.out.println("A new table has successfully been added");
                break;
            }
        if(i == Employer.getNumberOfEmployer())
        {
            System.out.println("There is no employer named " + creatorName);
        }
    }

    public void newOrder(String command)
    {
        System.out.println("PROGRESSING COMMAND: new_order");
        int numberOfItemType = 0;
        String[] dataOrder = command.split(";");
        String waiterName = dataOrder[0];
        int numOfCustomer = Integer.parseInt(dataOrder[1]);
        String[] orders = dataOrder[2].split(":");
        String[] itemNames = new String[Order.getMaxItems()];
        int[] itemAmount = new int[Order.getMaxItems()];
        for(String order : orders)
        {
            itemNames[numberOfItemType] = order.split("-")[0];
            itemAmount[numberOfItemType] = Integer.parseInt(order.split("-")[1]);
            numberOfItemType++;
        }

        int j;
        for(j = 0; j < Waiter.getNumberOfWaiter(); j++)
        {
            if(waiterName.equals(waiters[j].getName()))
            {
                if( Waiter.getMaxTableServices() == waiters[j].getNumberOfTableServicing() )
                {
                    System.out.println("Not allowed to service max. number of tables, " + Waiter.getMaxTableServices());
                    break;
                }

                int k;
                for(k = 0; k < Table.getTotalNumberOfTable(); k++)
                {
                    if( (tables[k].getCapacity() >= numOfCustomer) && !(tables[k].getIsOnService()) && (tables[k].getNumberOfOrder() != Table.getMaxOrders()))
                    {
                        tables[k].setIsOnService(true);
                        tables[k].setWaiterOnService(waiters[j].getName());
                        System.out.println("Table (= ID " + tables[k].getId() + ") has been taken into service");
                        waiters[j].takeOrder(tables[k], itemNames, itemAmount, numberOfItemType, items);
                        waiters[j].incrementnumberOfTableServicing();
                        break;
                    }
                }
                if(k == Table.getTotalNumberOfTable())
                {
                    System.out.println("There is no appropriate table for this order!");
                    return;
                }
                break;
            }
        }
        if(j == Waiter.getNumberOfWaiter())
        {
            System.out.println("There is no waiter named " + waiterName);
            return;
        }
    }

    public void addOrder(String command)
    {
        System.out.println("PROGRESSING COMMAND: add_order");
        int numberOfItemType = 0;
        String[] dataOrder = command.split(";");
        String waiterName = dataOrder[0];
        int tableID = Integer.parseInt(dataOrder[1]);
        String[] orders = dataOrder[2].split(":");
        String[] itemNames = new String[Order.getMaxItems()];
        int[] itemAmount = new int[Order.getMaxItems()];

        for(String order : orders)
        {
            itemNames[numberOfItemType] = order.split("-")[0];
            itemAmount[numberOfItemType] = Integer.parseInt(order.split("-")[1]);
            numberOfItemType++;
        }

        int i;
        for(i= 0; i < Waiter.getNumberOfWaiter(); i++)
        {
            if( waiters[i].getName().equals(waiterName) )
            {
                int j;
                for(j = 0; j < Table.getTotalNumberOfTable(); j++)
                {
                    if( tableID == tables[j].getId() )
                    {
                        if ( !(tables[j].getIsOnService()) || !(tables[j].getWaiterOnService().equals(waiterName)) )
                        {
                            System.out.println("This table is either not in service now or " + waiterName + " cannot be assigned this table!");
                            return;
                        }
                        if( tables[j].getNumberOfOrder() == Table.getMaxOrders() )
                        {
                            System.out.println("Not allowed to exceed max number of orders!");
                            return;
                        }

                        waiters[i].takeOrder(tables[j], itemNames, itemAmount, numberOfItemType, items);
                        break;
                    }
                }
                if( j == Table.getTotalNumberOfTable() )
                {
                    System.out.println("There is no table whose ID = " + tableID);
                    return;
                }

                break;
            }
        }
        if(i == Waiter.getNumberOfWaiter())
        {
            System.out.println("There is no waiter named " + waiterName);
            return;
        }
    }

    public void checkOut(String command)
    {
        System.out.println("PROGRESSING COMMAND: check_out");

        String[] data = command.split(";");
        String waiterName = data[0];
        int tableID = Integer.parseInt(data[1]);

        int i;
        for(i= 0; i < Waiter.getNumberOfWaiter(); i++)
        {
            if( waiters[i].getName().equals(waiterName) )
            {
                int j;
                for(j = 0; j < Table.getTotalNumberOfTable(); j++)
                {
                    if( tableID == tables[j].getId() )
                    {
                        if ( !(tables[j].getIsOnService()) || !(tables[j].getWaiterOnService().equals(waiterName)) )
                        {
                            System.out.println("This table is either not in service now or " + waiterName + " cannot be assigned this table!");
                            return;
                        }

                        waiters[i].checkOut(tables[j]);

                        break;
                    }
                }
                if( j == Table.getTotalNumberOfTable() )
                {
                    System.out.println("There is no table whose ID = " + tableID);
                    return;
                }
                break;
            }
        }
        if(i == Waiter.getNumberOfWaiter())
        {
            System.out.println("There is no waiter named " + waiterName);
            return;
        }
    }

    public void stockStatus()
    {
        System.out.println("PROGRESSING COMMAND: stock_status");
        for (int i = 0; i < items.size(); i++)
        {
            System.out.printf("%s:\t%d\n", items.get(i).getItemName(), items.get(i).getStockAmount());
        }
    }

    public void getTableStatus()
    {
        System.out.println("PROGRESSING COMMAND: get_table_status");
        for ( int i = 0; i < Table.getTotalNumberOfTable(); i++)
        {
            if( tables[i].getIsOnService() )
            {
                System.out.printf("Table %d: Reserved (%s)\n", tables[i].getId(), tables[i].getWaiterOnService());
            }
            else
            {
                System.out.printf("Table %d: Free\n", tables[i].getId());
            }
        }

    }

    public void getOrderStatus()
    {
        System.out.println("PROGRESSING COMMAND: get_order_status");
        for (int i = 0; i < Table.getTotalNumberOfTable(); i++)
        {
            System.out.printf("Table: %d\n\t%d order(s)\n", tables[i].getId(), tables[i].getNumberOfOrder());
            for( int j = 0; j < tables[i].getNumberOfOrder(); j++)
            {
                System.out.printf("\t\t%d item(s)\n", tables[i].getOrder(j).getNumberOfItems());
            }
        }
    }

    public void getEmployerSalary()
    {
        System.out.println("PROGRESSING COMMAND: get_employer_salary");
        for(int i = 0; i < Employer.getNumberOfEmployer(); i++)
        {
            double salary = employers[i].getSalary();
            salary += employers[i].getNumberOfCreatedTable() * employers[i].getSalary() * 0.1;
            System.out.printf("Salary for %s: %.1f\n", employers[i].getName(), salary);
        }
    }

    public void getWaiterSalary()
    {
        System.out.println("PROGRESSING COMMAND: get_waiter_salary");
        for (int i = 0; i < Waiter.getNumberOfWaiter(); i++)
        {
            double salary = waiters[i].getSalary();
            salary += waiters[i].getSalary() * waiters[i].getTotalNumberOfTableServiced() * 0.05;
            System.out.printf("Salary for %s: %.1f\n", waiters[i].getName(), salary);
        }
    }

    public Item getItem( int index )    {   return items.get(index);    }
    public Employer getEmployer(int index)  {   return employers[index];     }
    public Waiter getWaiter(int index)  {   return waiters[index];  }
    public Table getTable( int index )  {   return tables[index];   }
}
