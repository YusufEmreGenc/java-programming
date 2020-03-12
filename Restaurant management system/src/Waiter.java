import java.util.ArrayList;

public class Waiter extends Worker
{
    private static final int MAX_WAITER = 5;
    private static int numberOfWaiter = 0;
    private static final int MAX_TABLE_SERVICES = 3;
    private int numberOfTableServicing = 0;
    private int totalNumberOfTableServiced = 0;


    public Waiter(String name, int salary)
    {
        super(name, salary);
    }

    public void takeOrder(Table table, String[] itemNames, int[] itemAmount, int numberOfItemType, ArrayList<Item> items) {
        boolean flagOrder = true;
        Order order = new Order();
        for (int i = 0; i < numberOfItemType; i++) {
            int j;
            for (j = 0; j < items.size(); j++) {
                if (items.get(j).getItemName().equals(itemNames[i])) {
                    for (int k = 0; k < itemAmount[i]; k++) {
                        if (items.get(j).getStockAmount() > 0) {
                            flagOrder = false;
                            if (order.getNumberOfItems() != Order.getMaxItems()) {
                                Item item = new Item();
                                item.setItemName(items.get(j).getItemName());
                                item.setItemCost(items.get(j).getItemCost());

                                order.setItem(item);
                                System.out.println("Item " + itemNames[i] + " added into order");
                                order.incrementNumberOfItems();
                                items.get(j).decrementStockAmount();
                            }
                            else
                            {
                                totalNumberOfTableServiced++;
                                table.setOrder(order);
                                table.incrementNumberOfOrder();
                                // System.out.println("Not allowed to exceed max number of items in an order, " + Order.getMaxItems());
                                // In Piazza output like below one
                                System.out.println("Not allowed to exceed max number of orders!");
                                return;
                            }
                        }
                        else {
                            System.out.println("Sorry! No " + itemNames[i] + " in the stock!");
                        }
                    }
                    break;
                }
            }
            if (j == items.size()) {
                System.out.println("Unknown item " + itemNames[i]);
            }
        }
        if (flagOrder) return;
        else {
            totalNumberOfTableServiced++;
            table.setOrder(order);
            table.incrementNumberOfOrder();
        }
    }

    public void checkOut(Table table)
    {
        double totalCost = 0;
        ArrayList<Item> items = new ArrayList<>();
        for(int i = 0; i < table.getNumberOfOrder(); i++)
        {
            Order order = table.getOrder(i);
            for(int j = 0; j < order.getNumberOfItems() ; j++)
            {
                Item item = order.getItem(j);

                int k;
                for(k = 0; k < items.size(); k++)
                {
                    if(items.get(k).getItemName().equals(item.getItemName()))
                    {
                        items.get(k).incrementNumOfOrderedItem();
                        break;
                    }
                }
                if(k == items.size())
                {
                    items.add(item);
                    items.get(k).incrementNumOfOrderedItem();
                }
            }
        }

        for( int i = 0; i < items.size(); i++)
        {
            System.out.printf("%s:\t%.3f (x %d) %.3f $\n", items.get(i).getItemName(), items.get(i).getItemCost(), items.get(i).getNumOfOrderedItem(), items.get(i).getItemCost() * items.get(i).getNumOfOrderedItem());
            totalCost += items.get(i).getItemCost() * items.get(i).getNumOfOrderedItem();
        }
        System.out.printf("%s:\t%.3f $\n", "Total", totalCost);

        // Cleaning table for another Service
        for(int i = 0; i < table.getNumberOfOrder(); i++)
        {
            table.setOrder(null, i);
        }
        table.setNumberOfOrder(0);
        table.setIsOnService(false);
        table.setWaiterOnService(null);
        numberOfTableServicing--;
    }

    public static int getMaxWaiter()    {   return MAX_WAITER;   }
    public static int getNumberOfWaiter()  {   return numberOfWaiter;   }
    public static void incrementWaiterNum() {   numberOfWaiter++;   }
    public void incrementnumberOfTableServicing()   {   numberOfTableServicing++;   }
    public void incrementTotalNumberOfTableServiced()   {   totalNumberOfTableServiced++;   }
    public double getTotalNumberOfTableServiced()   {   return totalNumberOfTableServiced;  }
    public static int getMaxTableServices() {   return MAX_TABLE_SERVICES;  }
    public int getNumberOfTableServicing()  {   return numberOfTableServicing;  }
}
