public class Item
{
    private String itemName;
    private int numOfOrderedItem = 0;
    private double itemCost;
    private int stockAmount;

    public Item(String itemName, double itemCost, int stockAmount) {
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.stockAmount = stockAmount;
    }

    public Item()   {}

    public void incrementNumOfOrderedItem() {   numOfOrderedItem++; }

    public String getItemName() {   return itemName;    }

    public int getNumOfOrderedItem() {  return numOfOrderedItem;    }

    public double getItemCost() {   return itemCost;    }

    public int getStockAmount() {   return stockAmount; }

    public void setItemName(String itemName) {  this.itemName = itemName;   }

    public void setNumOfOrderedItem(int numOfOrderedItem) { this.numOfOrderedItem = numOfOrderedItem;   }

    public void setItemCost(double itemCost) {  this.itemCost = itemCost;   }

    public void setStockAmount(int stockAmount) {   this.stockAmount = stockAmount; }

    public void decrementStockAmount()  {   stockAmount--;  }
}
