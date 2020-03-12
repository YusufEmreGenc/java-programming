public class Order
{
    private static final int MAX_ITEMS = 10;
    private int numberOfItems = 0;
    private Item[] items = new Item[MAX_ITEMS];

    public void setItem(Item item)
    {
        items[numberOfItems] = item;
    }

    public static int getMaxItems() {   return MAX_ITEMS;   }

    public Item getItem(int index)
    {
        return items[index];
    }

    public int getNumberOfItems() {  return numberOfItems;   }

    public void incrementNumberOfItems() {   numberOfItems++;    }
}
