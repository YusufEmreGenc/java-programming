public class Table
{
    private static final int MAX_ORDERS = 5;
    private int numberOfOrder = 0;
    private Order[] orders = new Order[MAX_ORDERS];
    private static final int MAX_TABLES = 5;
    private static int totalNumberOfTable = 0;
    private int id;
    private int capacity;
    private boolean isOnService;
    private String waiterOnService;
    private String creatorEmployer;

    public Table(int id, int capacity, boolean isOnService, String creatorEmployer)
    {
        this.id = id;
        this.capacity = capacity;
        this.isOnService = isOnService;
        this.creatorEmployer = creatorEmployer;
    }

    public void setOrder(Order order)   {   orders[numberOfOrder] = order;  }

    public void setOrder(Order order, int index)   {   orders[index] = order;  }

    public void setWaiterOnService(String waiterName)   {   waiterOnService = waiterName;   }

    public int getNumberOfOrder() { return numberOfOrder;   }

    public void setNumberOfOrder(int value) {   numberOfOrder = value;  }

    public static int getMaxOrders() {  return MAX_ORDERS;  }

    public int getCapacity() {  return capacity;    }

    public boolean getIsOnService() {  return isOnService; }

    public int getId() {    return id;  }

    public void setIsOnService(boolean status)  {   isOnService = status;   }

    public String getWaiterOnService() {    return waiterOnService; }

    public String getCreatorEmployer() {    return creatorEmployer; }

    public void incrementNumberOfOrder()    {   numberOfOrder++;    }

    public Order getOrder(int index)
    {
        return orders[index];
    }

    public static int getMaxTables()    {   return MAX_TABLES;  }

    public static int getTotalNumberOfTable()   {   return totalNumberOfTable;  }

    public static void incrementTotalNumOfTables()  {   totalNumberOfTable++;   }
}
